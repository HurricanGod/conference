import re
from bs4 import BeautifulSoup
import unittest
from pyquery import PyQuery as pq

from src.spider.analysesources import initConfigForm, HtmlCodeHandler
from src.util.fileoperate import readConfig
from src.util.urllibhelper import SpiderApi


# url = "http://www.meeting.edu.cn/meeting/notice/meetingAction!userview1.action?page=8"
# data = {"page": 7, "subjectselect1": 100, "firstSubjectId": 0}
# html = SpiderApi.getPageSourceCodeByPost(url, data)
# print(html)

# confurl = "http://conf.cnki.net/advanceSearch.aspx"
# confdata = {
#         # "__EVENTTARGET": "pageLabel",
#         # "__EVENTARGUMENT": "turnPageSearch",
#         # "__ASYNCPOST": "true",
#         # "colLogical": "and", "submitType": "turnPageSearch",
#         # "submitType": "turnPageSearch",
#         "curZj": "I",
#         "curPage": 7
#         }
# html = SpiderApi.getPageSourceCodeByPost(confurl, confdata)
# soup1 = BeautifulSoup(html, 'lxml')
# selector = "#meetinglist_left > div:nth-child(2) > div.content680 > table:nth-child(1)"
# print(html)
# doc = pq(html)
# rows = doc(selector)
# print(rows.text())
# for row in rows.items("tr:gt(1)"):
#     print(row.text())


class TestSpider(unittest.TestCase):
    def setUp(self):
        print("start!")
        self.filename = "../file/config/website5.conf"
        self.savefilename = "../file/log/5.json"
        self.dicList = readConfig(self.filename)
        for i in range(0, len(self.dicList)):
            print("- " * 50)
            dic = self.dicList[i]
            for key, value in dic.items():
                print("key = {k}, value = {v}".format(k=key, v=value))

    def testExtractInfo(self):
        instance = HtmlCodeHandler.getInstance(self.filename, self.savefilename)
        mappers = instance.pageParsing(instance.cfg, filename=instance.savefile)
        if mappers is not None:
            for mapper in mappers:
                print(mapper)
