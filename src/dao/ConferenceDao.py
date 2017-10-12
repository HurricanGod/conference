from src.util.mongodbhelper import MongoDBCRUD


def queryAllConferenceFromMongo() -> set:
    count = MongoDBCRUD.query_collection_count("conference")
    resultlist = MongoDBCRUD.query("conference", {},
                                   {"cnName": 1, "enName": 1, "website": 1, "_id": 0, "tag": 1, "location": 1,
                                    "sponsor": 1,
                                    "startdate": 1, "enddate": 1, "deadline": 1, "acceptance": 1}, 0, count)
