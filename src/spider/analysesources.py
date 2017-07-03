from src.util.fileoperate import *
from src.util.urllibhelper import SpiderApi


cfgList = readConfig("../file/config/website.conf")
for cfgDic in cfgList:
    pass


def pageParsing(cfg: dict):
    baseurl = str(cfg.get("base_url"))
    way = cfg.get("way")
    index = 1
    end = 0
    if cfg.get("begin") != 'null':
        index = int(cfg.get("begin"))
    if cfg.get("end") != 'null':
        end = int(cfg.get("end"))
    while index <= end:
        url = baseurl.format(index)
        if way == 'get':
            html = SpiderApi.getPageSourceCode(url)

        index += 1


class SourcesHandler():
    pass
