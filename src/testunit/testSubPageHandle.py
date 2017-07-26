import random
import unittest
from src.spider.subpagehandle import SubPage
from src.util.fileoperate import readConfig, readDictionary, readSectionsInConfig


class TestMethodsInSubPageHandleModule(unittest.TestCase):
    def setUp(self):
        self.keyfile = "../file/config/keyword3.txt"
        filename = "../file/config/website3.conf"
        name = '../file/log/3.json'
        # 读取配置文件
        dicList = readConfig(filename)
        # 获取网站域名
        self.domain = readSectionsInConfig(filename)[0]
        dicts = readDictionary(name)  # 获取从会议网站初次抓取的信息
        index = random.randint(0, len(dicts) - 1)
        self.newdic = dicts[index]  # 随机抽取1条从会议网站抓取的信息以字典的方式保存
        if len(dicList) > 2:
            self.firstcfg = dict(dicList[0])  # 读取配置文件第1节配置信息
            self.secondcfg = dict(dicList[2])  # 读取配置文件第3节子页配置信息
        else:
            self.firstcfg = dict(dicList[0])  # 读取配置文件第1节配置信息
            self.secondcfg = dict(dicList[1])  # 读取配置文件第3节子页配置信息
        print('待测试URL：\n{}'.format(dicts[index]))
        print('网站域名：{}'.format(self.domain))

    def testExtractKeywords(self):
        subpage = SubPage(self.firstcfg, self.newdic, self.secondcfg, 'process1', self.domain, self.keyfile)
        subpage.extractKeywords()
        subpage.showkeydic()

    def testGetContentKeywords(self):
        subpage = SubPage(self.firstcfg, self.newdic, self.secondcfg, 'process1', self.domain, self.keyfile)
        keys = subpage.keywordmap
        for k, v in keys.items():
            print("key={k},value={v}".format(k=k, v=v))
