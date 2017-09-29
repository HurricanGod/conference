import ctypes
import sys
from multiprocessing import Process

from multiprocessing import Queue

from multiprocessing import Value

import time

from src.spider.logmanage import loadLogger
from src.util.fileoperate import writeToJson, writeToFile
from src.util.mongodbhelper import MongoDBCRUD
from src.util.typeconverter import Converter


class Counsumer(Process):
    log = loadLogger("applogconfig.ini")

    def __init__(self, queue: Queue, name: str):
        super().__init__()
        self.queue = queue
        self.name = name
        self.flag = Value(ctypes.c_bool, False)

    def run(self):
        super().run()
        print("{} process stared!".format(self.name))
        print("flag = {}".format(self.flag))
        print(Counsumer.log)
        arraylist = []
        while True:
            if self.flag.value:
                print("counsumer.flag = {}".format(self.flag))
                for j in range(100):
                    Counsumer.log.debug("* " * 1000)
                count = MongoDBCRUD.query_collection_count("person")
                print("collection conference has {} document".format(count))

                try:
                    for i in range(50):
                        MongoDBCRUD.insert({"name": "Sam{}".format(i), "age": 29+i}, "person")
                except Exception as e:
                    print("操作数据库时发生异常：\n{}\n".format(e))
                    Counsumer.log.debug("操作数据库时发生异常：\n{}\n".format(e))
                while not MongoDBCRUD.execresultqueue.empty():
                    Counsumer.log.debug(MongoDBCRUD.execresultqueue.get())
                break
            while not self.queue.empty():
                arraylist.append(self.queue.get())
                if len(arraylist) >= 6:
                    print("*" * 50)
                    print(arraylist)
                    arraylist.clear()
        print("comsumer process exit!")


class Saver(Process):
    logger = loadLogger("file/log/processlogconfig.ini")

    def __init__(self, queue: Queue, processname: str, num: int, savefilaname: str):
        super().__init__()
        self.queue = queue  # 存放解析完成后的会议信息字典队列
        self.processname = processname  # 进程名
        self.num = num  # 从队列里取出的数据达到 num 时把 num 条数据保存下来
        self.savefilename = savefilaname
        self.flag = Value(ctypes.c_bool, False)
        self.finish = Value(ctypes.c_bool, False)

    def run(self):
        # self.infologqueue.put("\n\tsave conference process is starting\n\n")
        Saver.logger.info("\n\tsave conference process is starting\n\n")
        print("process pid={} start!".format(self.pid))
        currentset = set()
        while True:
            if self.flag.value:
                print("self.flag = {}".format(self.flag.value))
                Saver.logger.info("self.flag = {}\n".format(self.flag.value))
                Saver.logger.info("集合中还剩下 {} 条数据".format(len(currentset)))
                if len(currentset) > 0:
                    try:
                        count = MongoDBCRUD.query_collection_count("conference")
                        self.savecore(count, currentset)
                    except Exception as e:
                        Saver.logger.error("发生异常：\n{}\n".format(e))
                        print("操作数据库时发生异常：\n{}\n".format(e))
                    finally:
                        execresultqueue = MongoDBCRUD.execresultqueue
                        while not execresultqueue.empty():
                            Saver.logger.info(execresultqueue.get())
                self.finish.value = True
                break
            while not self.queue.empty():
                #  从队列中取出解析后会议信息字典
                dic = self.queue.get()

                #  将字典转换为Conference 对象
                c = Converter.convert_dict_to_entry(dic)

                #  将字典对象添加到当前集合里
                currentset.add(c)
                try:
                    writeToJson(dic, self.savefilename)
                    writeToFile(self.savefilename, ',\n')
                except Exception as e:
                    Saver.logger.error("写入文件时出现异常，异常信息如下：\n{}\n".format(e))
                if len(currentset) >= self.num:
                    try:
                        count = MongoDBCRUD.query_collection_count("conference")
                        self.savecore(count, currentset)
                        currentset.clear()
                        Saver.logger.debug("\t\t清空集合后，集合个数为:{}\n".format(len(currentset)))
                    except Exception as e:
                        Saver.logger.error("操作数据库时出现了异常：\n{}\n".format(e))
                        Saver.logger.info("集合中还剩下 {} 条数据\n".format(len(currentset)))
                        print(e)
        print("saver process exit!")

    def savecore(self, count, currentset):
        #  对抓取的数据中去除与数据库重复部分
        skip = 0  # skip表示每次跳过的数据条数
        while skip <= count:
            #  每次查询 500 条数据，只取 cnName、enName、website 3个字段
            recordlist = MongoDBCRUD.query("conference", {},
                                           {"cnName": 1, "enName": 1, "website": 1, "_id": 0},
                                           skip, 500)
            presistenceset = set()
            for record in recordlist:
                entry = Converter.convert_dict_to_entry(record)
                presistenceset.add(entry)

            Saver.logger.info("当前抓取的数据数量为：{}".format(len(currentset)))
            print("当前抓取的数据数量为：{}".format(len(currentset)))

            currentset = currentset - presistenceset

            Saver.logger.info("抓取的数据去重后的数量为：{}".format(len(currentset)))
            print("抓取的数据去重后的数量为：{}\n\n".format(len(currentset)))
            skip += 500

        # 将去重后的数据保存到数据库
        MongoDBCRUD.saveSet(currentset, "conference")
        dbexecresqueue = MongoDBCRUD.execresultqueue
        while not dbexecresqueue.empty():
            Saver.logger.debug(dbexecresqueue.get())
