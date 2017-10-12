import re
from bs4 import BeautifulSoup
import unittest
from pyquery import PyQuery as pq

from src.spider.analysesources import initConfigForm, HtmlCodeHandler
from src.util.fileoperate import readConfig
from src.util.mysqlhelper import Mysql


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

    def testQueryCrawledURI(self):
        crawledURI = Mysql.queryData("select uri from CrawledURI")
        for uri in crawledURI:
            print("type:\t{}\t uri = {}".format(type(uri[0]), uri[0]))
