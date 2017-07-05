import re
from bs4 import BeautifulSoup

from pyquery import PyQuery as Pq
from src.util.fileoperate import *
from src.util.urllibhelper import SpiderApi
import multiprocessing


cfgList = readConfig("../file/config/website1.conf")
for cfgDic in cfgList:
    pass


def initConfigForm(form: dict) -> dict:
    """
    解析*.conf配置文件[post_data]部分的表单
    :param form: 从*.conf读取的配置文件信息，以字典的方式存储
    :return: 解析后的字典
    """
    for key in form.keys():
        v = str(form.get(key))
        # 将配置文件里的"{}"表达式解析为list对象
        if v.count("{") + v.count("}") >= 2 and v.startswith("{"):
            v = v[1:-1]  # 去除字符串第1个字符和最后1个字符
            if v.count(",") > 0:
                array = v.split(",")
                form[key] = array
            elif v.count("-") > 0:
                scope = v.split("-")
                array = []
                for i in range(int(scope[0]), int(scope[1]) + 1):
                    array.append(i)
                form[key] = array
    return form


def pageParsing(cfg: dict, form=None):
    baseurl = str(cfg.get("base_url"))
    way = cfg.get("way")
    index = 1
    end = 0
    innerindex = 0
    if cfg.get("begin") != 'null':
        index = int(cfg.get("begin"))
    if cfg.get("end") != 'null':
        end = int(cfg.get("end"))
    primaryInfo = []
    while index <= end:
        url = baseurl.format(index)
        print("http for url = {}".format(url))
        if way == 'get':
            html = SpiderApi.getPageSourceCode(url)
        else:
            form = initConfigForm(form)
            form_data = {}
            for key, val in form.items():
                if isinstance(val, list):
                    form_data[key] = val[innerindex]
                else:
                    form_data[key] = val
            print(form_data)
            cookie = cfg.get("cookie")
            if cookie != 'null':
                html = SpiderApi.getPageSourceCodeByPost(url, form_data, cookie)
            else:
                html = SpiderApi.getPageSourceCodeByPost(url, form_data)
            innerindex += 1
        primaryInfo += extractPrimaryInfoByPQuery(html, cfg)
        index += 1
    for t in primaryInfo:
        print(t)


def extractPrimaryInfoByPQuery(html: str, cfg: dict):
    getHtmlCore(html)
    doc = Pq(html)
    selector = cfg.get("tableselector")
    rowselector = cfg.get("rowselector")
    columnEle = cfg.get("column")
    href = int((cfg.get("href"))) if cfg.get("href") != "null" else None
    cnname = int((cfg.get("cnname"))) if cfg.get("cnname") != "null" else None
    enname = int((cfg.get("enname"))) if cfg.get("enname") != "null" else None
    tag = int((cfg.get("tag"))) if cfg.get("tag") != "null" else None
    location = int((cfg.get("location"))) if cfg.get("location") != "null" else None
    sponsor = int((cfg.get("sponsor"))) if cfg.get("sponsor") != "null" else None
    startdate = int((cfg.get("startdate"))) if cfg.get("startdate") != "null" else None
    enddate = int((cfg.get("enddate"))) if cfg.get("enddate") != "null" else None
    deadline = int((cfg.get("deadline"))) if cfg.get("deadline") != "null" else None
    acceptance = int((cfg.get("acceptance"))) if cfg.get("acceptance") != "null" else None
    rows = doc(selector).find(rowselector)
    # print(rows)
    keyinfo = []
    for row in rows.items():
        index = 0
        mapper = {}
        for cell in row.items(columnEle):
            cellstr = str(cell)
            soup = BeautifulSoup(cellstr, "lxml")
            if index == href:
                a = soup.find("a")
                mapper["website"] = a.get("href")
            if index == cnname:
                mapper["cnName"] = str(soup.get_text()).strip().replace(" ", "")
            elif index == enname:
                mapper["enName"] = str(soup.get_text()).strip().replace(" ", "")
            elif index == tag:
                mapper["tag"] = str(soup.get_text()).strip().replace(" ", "")
            elif index == location:
                mapper["location"] = str(soup.get_text()).strip().replace(" ", "")
            elif index == sponsor:
                mapper["sponsor"] = str(soup.get_text()).strip().replace(" ", "")
            elif index == startdate:
                mapper["startdate"] = str(soup.get_text()).strip().replace(" ", "")
            elif index == enddate:
                mapper["enddate"] = str(soup.get_text()).strip().replace(" ", "")
            elif index == deadline:
                mapper["deadline"] = str(soup.get_text()).strip().replace(" ", "")
            elif index == acceptance:
                mapper["acceptance"] = str(soup.get_text()).strip().replace(" ", "")
            index += 1
        keyinfo.append(mapper)
    return keyinfo


def extractPrimaryInfoByBeautifulSoup(html: str, cfg: dict):
    pass


def getHtmlCore(html: str):
    """
    提取html源码里含有待提取信息的html源码
    :param html:
    """
    sources = html.split("</html>")
    for s in sources:
        print(s)
        soup = BeautifulSoup(s, 'lxml')


class HtmlCodeHandler(multiprocessing.Process):
    def __init__(self, name, proqueue):
        super().__init__()
        self.name = name
        self.products = proqueue

    def run(self):
        pass
