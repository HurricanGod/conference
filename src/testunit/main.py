import re
from multiprocessing import Queue
from multiprocessing import Pool
import random

import time

from src.process.worker import Counsumer
from src.spider.logmanage import loadLogger


def doMatch(s: str, queue: Queue):
    index = random.randint(0, len(s)-4)
    queue.put(s[index:index+2])
    time.sleep(1)


if __name__ == '__main__':
    processpool = Pool(processes=8)
    logger = loadLogger("applogconfig.ini")
    print("finish setting")
    queue = Queue(10)
    logqueue = Queue()
    counsumer = Counsumer(queue, "consumer_process")
    # counsumer.daemon = True
    counsumer.start()
    param = "0123456789abcdefghijklmnopqrstuvwxyz"
    for i in range(15):
        processpool.apply_async(doMatch(param, queue))
    processpool.close()
    processpool.join()
    print("main counsumer.flag = {}".format(counsumer.flag))
    counsumer.flag.value = True
    counsumer.join()
    while not logqueue.empty():
        print(logqueue.get())
    print("main process done!")
