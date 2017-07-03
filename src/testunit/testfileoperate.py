import unittest

from src.util.fileoperate import readConfig


class TestMethodInFileOPerateModule(unittest.TestCase):
    def setUp(self):
        print("start test")

    def testReadconfig(self):
        filename = "../file/config/website.conf"
        dic = readConfig(filename)
        for i in dic:
            for key, value in i.items():
                print("key = {k}, value = {v}".format(k=key, v=value))
            break
