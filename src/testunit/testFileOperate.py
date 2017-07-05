import unittest

from src.spider.analysesources import initConfigForm, pageParsing
from src.util.fileoperate import readConfig


class TestMethodInFileOPerateModule(unittest.TestCase):
    def setUp(self):
        print("start test")

    def testReadconfig(self):
        """
        单元测试：
        测试函数：readConfig()
        测试函数：initConfigForm()
        """
        filename = "../file/config/website2.conf"
        dicList = readConfig(filename)
        for i in range(0, len(dicList)):
            print("- " * 50)
            dic = dicList[i]
            if i == 1:
                dic = initConfigForm(dic)
                print(dic)
            for key, value in dic.items():
                print("key = {k}, value = {v}".format(k=key, v=value))

    def testPageParsing1(self):
        filename = "../file/config/website1.conf"
        dicList = readConfig(filename)
        if len(dicList) == 2:
            pageParsing(dicList[0], dicList[1])
        else:
            pageParsing(dicList[0])

    def testPageParsing2(self):
        filename = "../file/config/website2.conf"
        dicList = readConfig(filename)
        if len(dicList) == 2:
            pageParsing(dicList[0], dicList[1])
        else:
            pageParsing(dicList[0])
