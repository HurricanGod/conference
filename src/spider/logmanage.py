import configparser
import logging
import os
from logging.config import fileConfig


def loadLogger(logconfigpath: str, logname='appLogger')-> logging.Logger:
    """
    读取日志配置文件并创建Logger对象
    """
    if not os.path.exists(logconfigpath):
        print("未找到日志配置文件applogconfig.ini")
        return None
    cf = configparser.ConfigParser()
    cf.read(logconfigpath, encoding="utf-8")
    fileConfig(cf)
    return logging.getLogger(logname)