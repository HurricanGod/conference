import datetime
import time
import psutil

from src.process.worker import Saver
from src.spider.logmanage import loadLogger
from src.util.fileoperate import *
from src.spider.analysesources import HtmlCodeHandler
from src.spider.subpagehandle import SubPage
from multiprocessing import Queue
from multiprocessing import Pool
import sys

from src.util.formathelper import Conference, DateFormatHelper
from src.util.mongodbhelper import MongoDBCRUD
from src.util.typeconverter import Converter


if __name__ == '__main__':
    # 清空日志内容
    clearContent('file/log/processdebug.log')
    clearContent('file/log/processinfo.log')
    clearContent('file/log/debug.log')
    clearContent('file/log/info.log')

    startTime = datetime.datetime.now()

    # 加载日志配置文件
    logger = loadLogger('file/log/applogconfig.ini')

    # 读取爬虫配置文件
    configFileNames = getFileNames('file/config/')
    findConf = lambda name: name[name.rindex('.') + 1:].lower() == 'conf'
    findTxt = lambda name: name[name.rindex('.') + 1:].lower() == 'txt'
    # 获取file/config/目录下所有的*.conf配置文件
    confConfigFileNames = list(filter(findConf, configFileNames))
    # 获取file/config/目录下所有的*.txt配置文件
    txtConfigFileNames = list(filter(findTxt, configFileNames))
    logger.debug('\nfile/config/目录下的.conf文件有：\n{}\n'.format(confConfigFileNames))
    logger.debug('\nfile/config/目录下的.txt文件有：\n{}\n'.format(txtConfigFileNames))
    savejsonfilenames = []  # 初次抓取的目标信息保存的路径

    # 生成初次爬取信息的临时文件名
    for i in range(1, len(confConfigFileNames) + 1):
        name = 'file/log/{}.json'.format(i)
        savejsonfilenames.append(name)
    processlist = []

    #  用于记录debug级别日志的队列
    debuglogqueue = Queue()

    # for i in range(0, len(confConfigFileNames)):
    for i in range(0, 2):
        processname = 'process{}'.format(i)
        sourcecodedealProcess = HtmlCodeHandler(confConfigFileNames[i],
                                                savejsonfilenames[i],
                                                processname, debuglogqueue)
        processlist.append(sourcecodedealProcess)
        sourcecodedealProcess.start()
    for p in processlist:
        p.join()
    logger.info("所有子进程执行完")
    while not debuglogqueue.empty():
        # logger.info(logqueue.get())
        debuglogqueue.get()

    # itemdics为二维列表，每一个元素存放'file/log/*.json'里的字典
    itemdics = []
    for jsonfile in savejsonfilenames:
        try:
            jsonitem = readDictionary(jsonfile)
            itemdics.append(jsonitem)
        except Exception as e:
            print("Exception:\t{}".format(sys.exc_info()))

    # websiteConfInfos二维列表，每个元素保存的是websiteX.conf配置文件[协议://域名]和[sub_page]下的信息
    websiteConfInfos = []
    # websiteDomains存放着websiteX.conf配置文件[协议://域名]里的内容(X=1,2,3……)，
    # 比如有个配置文件里第一个为[http://meeting.sciencenet.cn]，
    # websiteDomains=[http://meeting.sciencenet.cn,……]
    websiteDomains = []
    for filename in confConfigFileNames:
        dicList = readConfig(filename)
        sections = readSectionsInConfig(filename)
        websiteDomains.append(sections[0])
        if len(dicList) > 2:
            firstcfg = dict(dicList[0])  # 读取配置文件第1节配置信息
            secondcfg = dict(dicList[2])  # 读取配置文件第3节子页配置信息
            websiteConfInfos.append([firstcfg, secondcfg])
        else:
            firstcfg = dict(dicList[0])  # 读取配置文件第1节配置信息
            secondcfg = dict(dicList[1])  # 读取配置文件第3节子页配置
            websiteConfInfos.append([firstcfg, secondcfg])

    # 创建进程池
    processpool = Pool(processes=6)
    itemqueue = Queue()

    resultFileName = 'file/log/result.json'
    removeFile(resultFileName)
    writeToFile(resultFileName, '[\n')

    #  创建1个用于保存解析完成后的会议信息进程, 解析的会议字典满50条保存一次
    saver = Saver(itemqueue, "saveprocess", 35, resultFileName)
    #  将 saver 进程设置为守护进程
    saver.daemon = True
    saver.start()

    # for i in range(0, len(websiteDomains)):
    for i in range(0, 2):
        domain = websiteDomains[i]
        confDictionary1 = websiteConfInfos[i][0]
        confDictionary2 = websiteConfInfos[i][1]
        processName = 'process{}'.format(i)
        keywordConfFile = txtConfigFileNames[i]
        for j in range(0, len(itemdics[i])):
            item = SubPage(confDictionary1, confDictionary2, itemdics[i][j], domain,
                           processName, keywordConfFile, debuglogqueue, itemqueue)
            processpool.apply_async(item.exec(), ())

    processpool.close()
    processpool.join()

    while not debuglogqueue.empty():
        logger.debug(debuglogqueue.get())

    # 设置共享内存变量 flag 的值，通知 saver 进程爬取信息进程已经结束
    saver.flag.value = True
    print("\n\t抓取数据的进程任务结束\n")
    logger.info("\t\t抓取数据的进程任务结束\n")
    logger.debug("\t\t抓取数据的进程任务结束\n")

    while not saver.finish.value:
        pass

    writeToFile(resultFileName, '\n{}\n]')

    finishTime = datetime.datetime.now()
    logger.info("程序总共运行时间为 {}".format(finishTime - startTime))
    print("\nDone!\n")
    logger.debug("\nDone!\n")
