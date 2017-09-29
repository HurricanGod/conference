import re
from bs4 import BeautifulSoup
from bs4 import NavigableString
from functools import reduce
from multiprocessing import Process
from multiprocessing import Queue
from random import randint

import jieba.analyse
from pyquery import PyQuery as Pq

from src.spider.analysesources import getHtmlCore
from src.util.fileoperate import *
from src.util.urllibhelper import SpiderApi
from src.util.urllibhelper import Urldeal


class SubPage():
    def __init__(self, cfg: dict, subpagecfg: dict, keydic: dict,  domainname: str,
                 processname, keyfile: str, logqueue: Queue, itemqueue: Queue):
        super().__init__()
        self.oldcfg = cfg  # 配置文件website.conf第1节配置信息
        self.subpagecfg = subpagecfg  # 配置文件website.conf[sub_page]下的配置信息
        self.websitedomain = domainname  # website.conf下第一节[]里配置的域名
        self.keydic = keydic  # 首次抓取会议网站信息得到的目标字典
        self.name = processname  # 临时文件名称，每个进程名必须唯一
        self.keyfile = keyfile  # file/config目录下的keyword.txt文件的相对路径名
        self.keywordmap = self.__getcontentkeywords()  # file/config/keyword.txt下[key]节下的字典
        self.othermap = self.__getothermap()  # file/config/keyword.txt下[other]节下的字典
        self.logqueue = logqueue
        self.itemqueue = itemqueue

    def exec(self):
        try:
            self.extractKeywords()
            self.showkeydic()
        except Exception as e:
            self.logqueue.put("exception:\t{}\n".format(e))

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
                    allcontent = soup.text
                    allcontent = allcontent.replace('\n', '').replace('\r', '')
                    # 提取startdate、enddate字段
                    self.extractDateByRegular(allcontent)
                    # 精确提取location字段
                    self.preciseExtractLocation(allcontent)
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
                            # for ele in elements:
                            #     print(ele)
                            self.matchKeywords(elements)
                            self.extractWebsiteField(lines, tmpsoup)
                            self.extractTagFiles(allcontent)
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
            # candidates = []  # 候选词
            for k, v in self.keywordmap.items():
                candidates = []  # 候选词
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
                                item = item.replace(punctution, ' ')
                        self.keydic[k] = item
                    elif k == 'sponsor':
                        for punctution in punctuations:
                            if item.find(punctution) >= 0:
                                item = item.replace(punctution, '')
                        self.keydic[k] = item
                    elif k == 'startdate':
                        if self.othermap is not None:
                            # 先按照首选的开始时间格式进行粗略匹配
                            # 如果粗略匹配有匹配的字符串则尝试进行第一次精确匹配
                            # 粗略匹配没有匹配的字符串则按照第二个日期格式进行第二次粗略匹配
                            self.extractStartDateByKeywords(item, k)
                    elif k == 'enddate':
                        if self.othermap is not None:
                            # 先按照首选的结束时间格式进行粗略匹配
                            # 如果粗略匹配有匹配的字符串则尝试进行第一次精确匹配
                            # 粗略匹配没有匹配的字符串则按照第二个日期格式进行第二次粗略匹配
                            isexit = False
                            for sindex in range(1, 3):
                                # isexit用于标记是否找到匹配的日期，找到了记为True
                                if isexit:
                                    break
                                dateformat = 'enddateformat{}'.format(sindex)
                                pstr = self.othermap.get(dateformat)
                                # 如果第i个enddateformat未配置退出循环
                                if pstr is None:
                                    break
                                pattern = re.compile(pstr)
                                datestr = pattern.findall(item)
                                # 如果粗略匹配有匹配的字符串
                                if len(datestr) > 0 and isinstance(datestr, list):
                                    # 尝试进行精确匹配，查找是否有配置renddateformat
                                    realdateformat = 'renddateformat{}'.format(sindex)
                                    pstr = self.othermap.get(realdateformat)
                                    # 如果有配置renddateformat选项则进行精确匹配
                                    if pstr is not None:
                                        pattern = re.compile(pstr)
                                        for d in datestr:
                                            t = pattern.findall(d)
                                            if len(t) > 0:
                                                self.keydic[k] = t[0]
                                                isexit = True
                                                break
                                    # 未配置renddateformat则取粗略结果作为最终结果
                                    # 不再尝试其它日期格式
                                    else:
                                        self.keydic[k] = datestr[0]
                                        isexit = True
                                        break
                                else:
                                    continue
                    elif k == 'deadline':
                        if self.othermap is not None:
                            # 先按照首选的截止时间格式进行粗略匹配
                            # 如果粗略匹配有匹配的字符串则尝试进行第一次精确匹配
                            # 粗略匹配没有匹配的字符串则按照第二个日期格式进行第二次粗略匹配
                            isexit = False
                            for sindex in range(1, 3):
                                # isexit用于标记是否找到匹配的日期，找到了记为True
                                if isexit:
                                    break
                                dateformat = 'deadlineformat{}'.format(sindex)
                                pstr = self.othermap.get(dateformat)
                                # 如果第i个deadlineformat未配置退出循环
                                if pstr is None:
                                    break
                                pattern = re.compile(pstr)
                                datestr = pattern.findall(item)
                                # 如果粗略匹配有匹配的字符串
                                if len(datestr) > 0 and isinstance(datestr, list):
                                    # 尝试进行精确匹配，查找是否有配置rdeadlineformat
                                    realdateformat = 'rdeadlineformat{}'.format(sindex)
                                    pstr = self.othermap.get(realdateformat)
                                    # 如果有配置rdeadlineformat选项则进行精确匹配
                                    if pstr is not None:
                                        pattern = re.compile(pstr)
                                        for d in datestr:
                                            t = pattern.findall(d)
                                            if len(t) > 0:
                                                self.keydic[k] = t[0]
                                                isexit = True
                                                break
                                    # 未配置rdeadlineformat则取粗略结果作为最终结果
                                    # 不再尝试其它日期格式
                                    else:
                                        self.keydic[k] = datestr[0]
                                        isexit = True
                                        break
                                else:
                                    continue
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
                            # 先按照首选的截止时间格式进行粗略匹配
                            # 如果粗略匹配有匹配的字符串则尝试进行第一次精确匹配
                            # 粗略匹配没有匹配的字符串则按照第二个日期格式进行第二次粗略匹配
                            isexit = False
                            for sindex in range(1, 3):
                                # isexit用于标记是否找到匹配的日期，找到了记为True
                                if isexit:
                                    break
                                dateformat = 'acceptanceformat{}'.format(sindex)
                                pstr = self.othermap.get(dateformat)
                                # 如果第i个acceptanceformat未配置退出循环
                                if pstr is None:
                                    break
                                pattern = re.compile(pstr)
                                datestr = pattern.findall(item)
                                # 如果粗略匹配有匹配的字符串
                                if len(datestr) > 0 and isinstance(datestr, list):
                                    # 尝试进行精确匹配，查找是否有配置racceptanceformat
                                    realdateformat = 'racceptanceformat{}'.format(sindex)
                                    pstr = self.othermap.get(realdateformat)
                                    # 如果有配置racceptanceformat选项则进行精确匹配
                                    if pstr is not None:
                                        pattern = re.compile(pstr)
                                        for d in datestr:
                                            t = pattern.findall(d)
                                            if len(t) > 0:
                                                self.keydic[k] = t[0]
                                                isexit = True
                                                break
                                    # 未配置racceptanceformat则取粗略结果作为最终结果
                                    # 不再尝试其它日期格式
                                    else:
                                        self.keydic[k] = datestr[0]
                                        isexit = True
                                        break
                                else:
                                    continue
                    elif k == 'enname':
                        for punctution in punctuations:
                            if item.find(punctution) >= 0:
                                item = item.replace(punctution, '')
                        self.keydic[k] = item
                    # break

    def extractDateByRegular(self, allcontent: str):
        isexit1 = False
        # isexit1用于标记startdate是否找到匹配的日期，找到了记为True
        isexit2 = False
        # isexit2用于标记enddate是否找到匹配的日期，找到了记为True
        for sindex in range(1, 3):
            if isexit1 and isexit2:
                break
            startdateformat = 'startdateformat{}'.format(sindex)
            enddateformat = 'enddateformat{}'.format(sindex)
            startdatepatternstr = self.othermap.get(startdateformat)
            enddatepatternstr = self.othermap.get(enddateformat)
            if not isexit1:
                # 如果第i个startdateformat未配置退出循环
                if startdatepatternstr is not None:
                    # 编译开始日期模式串
                    pattern = re.compile(startdatepatternstr)
                    # 在整个网页寻找匹配的日期串
                    # pp2 = re.compile(r'[0-9]{1,2}(?:-[0-9]{1,2})? +[a-zA-Z]{3,} +[0-9]{4}')
                    startdatestr = pattern.findall(allcontent)
                    # 如果粗略匹配有匹配的字符串
                    if len(startdatestr) > 0 and isinstance(startdatestr, list):
                        # 尝试进行精确匹配，查找是否有配置rstartdateformat
                        realdateformat = 'rstartdateformat{}'.format(sindex)
                        startdatepatternstr = self.othermap.get(realdateformat)
                        # 如果有配置rstartdateformat进行精确匹配
                        if startdatepatternstr is not None:
                            pattern = re.compile(startdatepatternstr)
                            for d in startdatestr:
                                t = pattern.findall(d)
                                if len(t) > 0:
                                    self.keydic['startdate'] = t[0]
                                    isexit1 = True
                                    break
                        # 未配置rstartdateformat则取粗略结果作为最终结果
                        # 不再尝试匹配其它日期格式
                        else:
                            # 如果目标字典里没有存放键为startdate的项
                            if self.keydic.get('startdate') is None:
                                self.keydic['startdate'] = startdatestr[0]
                            isexit1 = True
            if not isexit2:
                if enddatepatternstr is not None:
                    pattern = re.compile(enddatepatternstr)
                    enddatestr = pattern.findall(allcontent)
                    # 如果粗略匹配有匹配的字符串
                    if len(enddatestr) > 0 and isinstance(enddatestr, list):
                        # 尝试进行精确匹配，查找是否有配置rstartdateformat
                        realdateformat = 'renddateformat{}'.format(sindex)
                        enddatepatternstr = self.othermap.get(realdateformat)
                        # 如果有配置renddateformat进行精确匹配
                        if enddatepatternstr is not None:
                            pattern = re.compile(enddatepatternstr)
                            for d in enddatestr:
                                t = pattern.findall(d)
                                if len(t) > 0:
                                    self.keydic['enddate'] = t[0]
                                    isexit2 = True
                                    break
                        # 未配置renddateformat则取粗略结果作为最终结果
                        # 不再尝试匹配其它日期格式
                        else:
                            if self.keydic.get('enddate') is None:
                                self.keydic['enddate'] = enddatestr[0]
                            isexit2 = True

    def preciseExtractLocation(self, allcontent: str):
        """
        精确提取location字段
        :param allcontent: html页面全部内容
        """
        locationformat1 = self.othermap.get('locationformat1')
        # 检查keyword.txt配置文件里是否配置了locationformat1字段
        # 未配置什么都不做
        # 配置了则去检查提取的location字段是否符合配置的正则式
        if locationformat1 is not None:
            locationpattern = re.compile(locationformat1)
            location = self.keydic.get('location')
            # 如果location字段已经提取，则根据配置的正则式进一步精确
            if location is not None:
                locations = locationpattern.findall(location)
                if len(locations) > 0:
                    self.keydic['location'] = locations[0]
            # 如果location字段未提取，则在全部内容中尝试提取
            else:
                locationcandidates = locationpattern.findall(allcontent)
                if len(locationcandidates) > 0:
                    self.keydic['location'] = locationcandidates[0]

    def extractStartDateByKeywords(self, item, k):
        isexit = False
        for sindex in range(1, 3):
            # isexit用于标记是否找到匹配的日期，找到了记为True
            if isexit:
                break
            dateformat = 'startdateformat{}'.format(sindex)
            pstr = self.othermap.get(dateformat)
            # 如果第i个startdateformat未配置退出循环
            if pstr is None:
                break
            pattern = re.compile(pstr)
            datestr = pattern.findall(item)
            # 如果粗略匹配有匹配的字符串
            if len(datestr) > 0 and isinstance(datestr, list):
                # 尝试进行精确匹配，查找是否有配置rstartdateformat
                realdateformat = 'rstartdateformat{}'.format(sindex)
                pstr = self.othermap.get(realdateformat)
                # 如果有配置rstartdateformat进行精确匹配
                if pstr is not None:
                    pattern = re.compile(pstr)
                    for d in datestr:
                        t = pattern.findall(d)
                        if len(t) > 0:
                            self.keydic[k] = t[0]
                            isexit = True
                            break
                # 未配置rstartdateformat则取粗略结果作为最终结果
                # 不再尝试匹配其它日期格式
                else:
                    self.keydic[k] = datestr[0]
                    isexit = True
                    break
            else:
                continue

    def showkeydic(self):
        if len(self.keydic) > 2:
            self.itemqueue.put(self.keydic)
        for k, v in self.keydic.items():
            print("{k} : {v}".format(k=k, v=v))
            # self.logqueue.put("{k} : {v}".format(k=k, v=v))

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
                self.keydic['website'] = urllist[randint(0, length - 1)]

    def extractTagFiles(self, content: str):
        keytags1 = jieba.analyse.extract_tags(content, 10)
        tags1 = ' '.join(keytags1)
        keytags2 = jieba.analyse.textrank(content, 10)
        tags2 = ' '.join(keytags2)
        print('* ' * 20)
        print(tags1)
        print('\n')
        print(tags2)
        tagset1 = set(keytags1)
        tagset2 = set(keytags2)
        candidatetag = tagset1 & tagset2
        print('\n关键词交集为：{}'.format(candidatetag))
        # self.logqueue.put('\n关键词交集为：{}'.format(candidatetag))
        title = self.keydic.get('cnName')
        title = title if title is not None else self.keydic.get('enName')
        if title is not None:
            keytags1 = set(jieba.analyse.extract_tags(title, 3))
            keytags2 = set(jieba.analyse.textrank(title, 3))
            candidatetag = candidatetag | (keytags1 & keytags2)
            if len(candidatetag) == 0:
                candidatetag = keytags1 | keytags2
            # self.logqueue.put('候选关键词为：{}'.format(candidatetag))
            print('候选关键词为：{}'.format(candidatetag))
        if self.keydic.get('tag') is None:
            tag = ','.join(candidatetag)
            self.keydic['tag'] = tag
