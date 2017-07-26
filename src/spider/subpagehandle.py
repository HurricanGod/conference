import re
from bs4 import BeautifulSoup
from bs4 import NavigableString
from functools import reduce
from random import randint

from pyquery import PyQuery as Pq
from src.util.fileoperate import *
from src.spider.analysesources import getHtmlCore
from multiprocessing import Process
from src.util.urllibhelper import SpiderApi
from src.util.urllibhelper import Urldeal


class SubPage(Process):
    def __init__(self, cfg: dict, keydic: dict, subpagecfg: dict, processname, domainname: str, keyfile: str):
        super().__init__()
        self.oldcfg = cfg
        self.keydic = keydic  # 首次抓取会议网站信息得到的目标字典
        self.subpagecfg = subpagecfg
        self.name = processname
        self.websitedomain = domainname
        self.keyfile = keyfile
        self.keywordmap = self.__getcontentkeywords()  # file/config/keyword.txt下[key]节下的字典
        self.othermap = self.__getothermap()  # file/config/keyword.txt下[other]节下的字典

    def __getcontentkeywords(self) -> dict:
        keysmap = readConfig(self.keyfile)
        keys = dict(keysmap[0])
        for k, v in keys.items():
            if str(v).count(',') > 0:
                val = str(v).split(',')
                keys[k] = val
            else:
                keys[k] = [v]
        return keys

    def __getothermap(self) -> dict:
        configmap = readConfig(self.keyfile)
        if len(configmap) > 1:
            return configmap[1]
        else:
            return None

    def extractKeywords(self):
        url = str(self.keydic.get('website'))
        if url is not None:
            if url.startswith(self.websitedomain):
                html = SpiderApi.getPageSourceCode(url)
                contentselector = self.subpagecfg.get('contentselector')
                keyselector = self.subpagecfg.get('keyselector1')
                while True:
                    soup = BeautifulSoup(html, 'lxml')
                    SpiderApi.deleteNoise(soup)  # 删除style、script等标签
                    tablesoup = BeautifulSoup(html, 'lxml')
                    content = soup.select(contentselector)
                    if len(content) > 0:
                        tmpsoup = BeautifulSoup(str(content[0]), 'lxml')
                        content = content[0].get_text()
                        writeToFile(self.name + '.txt', content)
                        lines = formatReadlines(self.name + '.txt')
                        removeFile(self.name + '.txt')
                        # print(content)
                        # print('*  ' * 50)
                        table = tablesoup.select(keyselector)
                        if len(table) > 0:
                            tablehtml = table[0]
                            elements = []
                            for row in tablehtml.children:
                                if not isinstance(row, NavigableString):
                                    rowcontent = str(row.get_text()).replace('\t', '') \
                                        .replace('\r\n', '').replace('\n', '')
                                    elements.append(rowcontent)
                            for ele in elements:
                                print(ele)
                            self.matchKeywords(elements)
                            self.extractWebsiteField(lines, tmpsoup)
                        break
                    else:
                        html = getHtmlCore(html)
                        doc = Pq(html)
                        html = str(doc(contentselector))

    def matchKeywords(self, contentlist: list):
        punctuations = [',', '.', ':', '?', '!', ';', '，', '。', '：', '？', '！', '；']
        filterurls = ["http://www.sciencepublishinggroup.com",
                      "http://www.easychair.org",
                      'https://easychair.org',
                      "http://conf.cnki.net",
                      "http://mp.weixin.qq.com",
                      "http://www.engii.org/RegistrationSubmission/default.aspx"]
        for line in contentlist:
            candidates = []  # 候选词
            for k, v in self.keywordmap.items():
                for kwd in v:
                    index = str(line).find(kwd)
                    if index >= 0:
                        index += len(kwd)
                        end = len(line)
                        substr = str(line)[index:end]
                        if len(substr) > 0:
                            candidates.append(substr)
                if len(candidates) > 0:
                    fvar = lambda x, y: x if len(x) > len(y) else y
                    item = reduce(fvar, candidates)  # 在候选词中选出1个最长的作为关键字对应的value
                    if k == 'tag':
                        self.keydic[k] = item
                    elif k == 'location':
                        for punctution in punctuations:
                            if item.find(punctution) >= 0:
                                item = item.replace(punctution, '')
                        self.keydic[k] = item
                    elif k == 'sponsor':
                        for punctution in punctuations:
                            if item.find(punctution) >= 0:
                                item = item.replace(punctution, '')
                        self.keydic[k] = item
                    elif k == 'startdate':
                        if self.othermap is not None:
                            pstr = self.othermap.get('startdateformat1')
                            pattern = re.compile(pstr)
                            datestr = pattern.findall(item)
                            pstr = self.othermap.get('rstartdateformat1')
                            if len(datestr) > 0 and isinstance(datestr, list):
                                if pstr is not None:
                                    pattern = re.compile(pstr)
                                    for d in datestr:
                                        t = pattern.findall(d)
                                        if len(t) > 0:
                                            self.keydic[k] = t[0]
                                            break
                                else:
                                    self.keydic[k] = datestr[0]
                    elif k == 'enddate':
                        if self.othermap is not None:
                            pstr = self.othermap.get('enddateformat1')
                            pattern = re.compile(pstr)
                            datestr = pattern.findall(item)
                            pstr = self.othermap.get('renddateformat1')
                            if len(datestr) > 0 and isinstance(datestr, list):
                                if pstr is not None:
                                    pattern = re.compile(pstr)
                                    for d in datestr:
                                        t = pattern.findall(d)
                                        if len(t) > 0:
                                            self.keydic[k] = t[0]
                                            break
                                else:
                                    self.keydic[k] = datestr[0]
                    elif k == 'deadline':
                        if self.othermap is not None:
                            pstr = self.othermap.get('deadlineformat1')
                            pattern = re.compile(pstr)
                            datestr = pattern.findall(item)
                            pstr = self.othermap.get('rdeadlineformat1')
                            if len(datestr) > 0 and isinstance(datestr, list):
                                if pstr is not None:
                                    pattern = re.compile(pstr)
                                    for d in datestr:
                                        t = pattern.findall(d)
                                        if len(t) > 0:
                                            self.keydic[k] = t[0]
                                            break
                                else:
                                    self.keydic[k] = datestr[0]
                    elif k == 'website':
                        # 正则匹配URL
                        urlpattern = re.compile(r'http[s]?:[A-Za-z0-9./=?&-]+')
                        urls = urlpattern.findall(item)
                        # 遍历符合URL规则的网址
                        for url in urls:
                            # 如果网址里包含原来抓取网站的域名则不修改原来的website
                            if str(url).find(self.websitedomain) >= 0:
                                pass
                            else:
                                isValidity = True
                                # 判断网址是否在过滤集里
                                for filterurl in filterurls:
                                    if str(url).find(filterurl) >= 0:
                                        isValidity = False
                                        break
                                # 不在过滤集又不包含初次抓取网站的域名的则把它赋值给website
                                if isValidity:
                                    if Urldeal.isVisitable(url):  # 判断正则匹配的URL是否可以访问
                                        self.keydic[k] = url
                                    break
                    elif k == 'acceptance':
                        if self.othermap is not None:
                            pstr = self.othermap.get('acceptanceformat1')
                            pattern = re.compile(pstr)
                            datestr = pattern.findall(item)
                            pstr = self.othermap.get('racceptanceformat1')
                            if len(datestr) > 0 and isinstance(datestr, list):
                                if pstr is not None:
                                    pattern = re.compile(pstr)
                                    for d in datestr:
                                        t = pattern.findall(d)
                                        if len(t) > 0:
                                            self.keydic[k] = t[0]
                                            break
                                else:
                                    self.keydic[k] = datestr[0]
                    elif k == 'enname':
                        for punctution in punctuations:
                            if item.find(punctution) >= 0:
                                item = item.replace(punctution, '')
                        self.keydic[k] = item
                    break

    def showkeydic(self):
        for k, v in self.keydic.items():
            print("{k} : {v}".format(k=k, v=v))

    def extractWebsiteField(self, contentLines: list, soup: BeautifulSoup):
        """
        根据初次爬取的会议获取的站内URL进一步获取会议官网地址
        :param contentLines: 访问站内URL获取的详细内容(纯文本，不含html源码)，类型为list，每个元素为1行
        :param soup: 访问站内URL获取的html源码，类型为BeautifulSoup对象
        """
        urlpattern = re.compile(r'http[s]?://[A-Za-z0-9./=?&-]+')
        urlset = set()  # 存放候选URL的集合
        for line in contentLines:
            urls = urlpattern.findall(line)
            # 删除不能访问的URL
            for i in urls:
                if not Urldeal.isVisitable(i):
                    urls.remove(i)
            # 删除包含指定前缀的URL
            for i in urls:
                if Urldeal.shouldbefilterout(i):
                    urls.remove(i)
            # 若URL包含首次抓取网站的前缀则删除该URL
            for i in urls:
                if str(i).find(self.websitedomain) >= 0:
                    urls.remove(i)
            urlset = urlset.union(urls)
        aelements = soup.find_all('a')
        for a in aelements:
            #  判断<a>标签是否有href属性
            if a.get("href") is not None:
                # 获取<a>标签的href属性
                href = a.get("href")
                # 正则匹配href属性，判断是否以http开头符合URL规则
                matcher = urlpattern.match(href)
                # 判断是否匹配成功
                if matcher is not None:
                    # 取匹配的第一个URL
                    url = matcher.group(0)
                    # 判断URL是否可访问并且不在过滤集里
                    if Urldeal.isVisitable(url) and not Urldeal.shouldbefilterout(url):
                        # URL可访问并且不在过滤集里并且不含抓取网站URL前缀则添加到URL集合里
                        if url.find(self.websitedomain) < 0:
                            urlset.add(url)
        oldwebsite = str(self.keydic.get('website'))
        if oldwebsite is not None and oldwebsite.find(self.websitedomain) >= 0:
            length = len(urlset)
            if length > 0:
                urllist = list(urlset)
                self.keydic['urls'] = urllist
                self.keydic['website'] = urllist[randint(0, length-1)]
