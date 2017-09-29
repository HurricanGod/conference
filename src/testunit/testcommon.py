import unittest

from src.model.testmodel import Person


class CommonTest(unittest.TestCase):
    def referenceParamPro(self, currentset: set):
        print("currentset.size = {}".format(len(currentset)))
        person3 = Person("WangWu", 19)
        s = set()
        s.add(person3)
        currentset = currentset - s
        currentset.clear()
        print("currentset.size = {}".format(len(currentset)))

    def setUp(self):
        super().setUp()
        print("start")

    def testClass(self):
        person1 = Person("Zhangsan", 19)
        person2 = Person("Zhangsan", 20)
        person3 = Person("Zhangsan", 19)

        print("person1 == person2 = {}".format(person1 == person2))
        print("person1 == person3 = {}".format(person1 == person3))

    def testClassSet1(self):
        person1 = Person("Zhangsan", 19)
        person2 = Person("Lisi", 20)
        person3 = Person("WangWu", 19)
        s = set()
        s.add(person1)
        s.add(person2)
        s.add(person3)

        print("before call referenceParamPro():set.length = {}".format(len(s)))

        self.referenceParamPro(s)
        print("after call referenceParamPro():set.length = {}".format(len(s)))

    def testClassSet2(self):
        person1 = Person("Zhangsan", 19)
        person2 = Person("Lisi", 20)
        person3 = Person("WangWu", 19)
        s = set()
        s.add(person1)
        s.add(person2)
        s.add(person3)

        print("before call clear():set.length = {}".format(len(s)))

        s.clear()
        print("after call clear():set.length = {}".format(len(s)))

    def testClassVariable(self):
        print("1. Person.count = {}".format(Person.count))
        person1 = Person("Zhangsan", 19)
        Person.count = 1
        print("2. Person.count = {}".format(Person.count))
        person2 = Person("WangWu", 19)
        Person.setCount(3)
        person3 = Person("aqian", 22)
        person3.getCountValue()
