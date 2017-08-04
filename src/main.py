from src.spider.logmanage import loadLogger
from src.util.fileoperate import *
from src.spider.analysesources import HtmlCodeHandler
from src.spider.subpagehandle import SubPage
from multiprocessing import Queue
from multiprocessing import Pool


if __name__ == '__main__':
    logger = loadLogger('file/log/applogconfig.ini')
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
    for i in range(1, len(confConfigFileNames) + 1):
        name = 'file/log/{}.json'.format(i)
        savejsonfilenames.append(name)
    processlist = []
    logqueue = Queue()
    for i in range(0, len(confConfigFileNames)):
        processname = 'process{}'.format(i)
        sourcecodedealProcess = HtmlCodeHandler(confConfigFileNames[i],
                                                savejsonfilenames[i],
                                                processname, logqueue)
        processlist.append(sourcecodedealProcess)
        sourcecodedealProcess.start()
    for p in processlist:
        p.join()
    logger.info("所有子进程执行完")
    while not logqueue.empty():
        # logger.info(logqueue.get())
        logqueue.get()

    # itemdics为二维列表，每一个元素存放'file/log/*.json'里的字典
    itemdics = []
    for jsonfile in savejsonfilenames:
        jsonitem = readDictionary(jsonfile)
        itemdics.append(jsonitem)
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
    processpool = Pool(processes=10)
    itemqueue = Queue()
    for i in range(0, len(websiteDomains)):
        domain = websiteDomains[i]
        confDictionary1 = websiteConfInfos[i][0]
        confDictionary2 = websiteConfInfos[i][1]
        processName = 'process{}'.format(i)
        keywordConfFile = txtConfigFileNames[i]
        for j in range(0, len(itemdics[i])):
            item = SubPage(confDictionary1, confDictionary2, itemdics[i][j], domain,
                           processName, keywordConfFile, logqueue, itemqueue)
            processpool.apply_async(item.exec(), ())
    processpool.close()
    processpool.join()
    while not logqueue.empty():
        logger.debug(logqueue.get())
    resultFileName = 'file/log/result.json'
    writeToFile(resultFileName, '[\n')
    while not itemqueue.empty():
        writeToJson(itemqueue.get(), resultFileName)
        if not itemqueue.empty():
            writeToFile(resultFileName, ',\n')
    writeToFile(resultFileName, '\n]')
