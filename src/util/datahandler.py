from src.util.formathelper import Conference
from src.util.mongodbhelper import MongoDBCRUD


class SetOperator(object):
    @classmethod
    def delete_repeat(cls, currentset: set) -> set:
        count = MongoDBCRUD.query_collection_count("conference")
        skip = 0
        limit = 200
        while skip < count:
            datalist = MongoDBCRUD.query("conference", {}, {"cnName": 1, "enName": 1, "website": 1, "_id": 0}, skip,
                                         limit)
            partset = set()
            for ele in datalist:
                data = dict(ele)
                c = Conference(data)
                partset.add(c)
            currentset = currentset - partset
            skip += limit
        return currentset
