import pymysql
import sys


class Mysql(object):
    __slots__ = ()
    host = "118.89.59.66"
    port = 3306
    user = "root"
    password = "admin123?"
    db = "pythondb"
    charset = "utf8"

    def __init__(self):
        pass

    @staticmethod
    def addToDb(sql, params):
        connect = pymysql.connect(host=Mysql.host, port=Mysql.port, user=Mysql.user, passwd=Mysql.password,
                                  db=Mysql.db, charset=Mysql.charset)
        cur = connect.cursor()
        affect_line = 0
        try:
            cur.execute(sql, params)
            connect.commit()
            affect_line = cur.rowcount
        except Exception as e:
            print("sql执行异常：{e}".format(e=e))
            connect.rollback()
        else:
            connect.close()
            return affect_line

    @staticmethod
    def queryData(sql, params=None):
        connect = pymysql.connect(host=Mysql.host, port=Mysql.port, user=Mysql.user, passwd=Mysql.password,
                                  db=Mysql.db, charset=Mysql.charset)
        cur = connect.cursor()
        rows = None
        try:
            if params is not None:
                cur.execute(sql, params)
            else:
                cur.execute(sql)
            connect.commit()
            rows = cur.fetchall()
        except Exception as e:
            print("sql执行出现了异常%s" % e)
            connect.rollback()
        finally:
            connect.close()
        return rows

    @staticmethod
    def updateTable(sql, params):
        connect = pymysql.connect(host=Mysql.host, port=Mysql.port, user=Mysql.user, passwd=Mysql.password,
                                  db=Mysql.db, charset=Mysql.charset)
        cur = connect.cursor()
        affect_line = 0
        try:
            cur.execute(sql, params)
            connect.commit()
            affect_line = cur.rowcount
        except:
            print("异常信息：%s" % sys.exc_info())
        finally:
            connect.close()
        return affect_line

    @staticmethod
    def deleteById(tableName, value, primaryKey="id"):
        connect = pymysql.connect(host=Mysql.host, port=Mysql.port, user=Mysql.user, passwd=Mysql.password,
                                  db=Mysql.db, charset=Mysql.charset)
        cur = connect.cursor()
        affect_line = 0
        sql = "delete from %s where %s = %s"
        try:
            cur.execute(sql, (tableName, primaryKey, value))
            connect.commit()
            affect_line = cur.rowcount
        except:
            print("异常信息：%s" % sys.exc_info())
        finally:
            connect.close()
        return affect_line
