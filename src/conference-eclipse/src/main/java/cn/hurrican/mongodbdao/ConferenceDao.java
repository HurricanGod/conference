package cn.hurrican.mongodbdao;

import cn.hurrican.beans.ConferenceInfo;
import cn.hurrican.utils.AbstractBaseMongoTemplete;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static cn.hurrican.utils.DateUtils.calculateDateByCalendar;

/**
 * Created by NewObject on 2017/10/4.
 */
@Component(value = "conferenceMongoDao")
public class ConferenceDao extends AbstractBaseMongoTemplete {

    /**
     *
     * @param skip
     * @param limit
     * @param collectionName
     * @return
     */
    public List<ConferenceInfo> findAllLimit(Integer skip, Integer limit, String collectionName){

        Query query = new Query();
        query.skip(skip);
        query.limit(limit);

        /**
         *  根据 startdate 字段按升序排序
         */
        Sort sort = new Sort(Sort.Direction.ASC, "startdate");
        query.with(sort);

        List<ConferenceInfo> list = mongoTemplate.find(query, ConferenceInfo.class, collectionName);
        return list;
    }


    /**
     * 查询最近的会议信息，以系统当前日期为参考点，查询结果按 "startdate" 字段升序排序
     * @param number number为负数表示前number天举行的会议信息
     * @param collectionName 数据库中的集合名
     * @return
     */
    public List<ConferenceInfo> selectConferenceLastly(Integer number, String collectionName){
        Date current = new Date();
        Date ending = calculateDateByCalendar(current, number);

        Criteria criteria = Criteria.where("startdate").gte(current)
                                    .lte(ending);
        Query query = new Query();
        query.addCriteria(criteria);

        Sort sort = new Sort(Sort.Direction.ASC, "startdate");
        query.with(sort);

        List<ConferenceInfo> list = mongoTemplate.find(query, ConferenceInfo.class, collectionName);

        return list;


    }

    /**
     * 查询最近的会议信息，以系统当前日期为参考点
     * @param number number为负数表示前number天举行的会议信息
     * @param collectionName 数据库中的集合名
     * @param properties 查询结果按照可变参数 properties指定的字段 升序排序
     * @return
     */
    public List<ConferenceInfo> selectConferenceLastly(Integer number, String collectionName,
                                                    String... properties){
        Date current = new Date();
        Date ending = calculateDateByCalendar(current, number);

        Criteria criteria = Criteria.where("startdate").gte(current)
                .lte(ending);
        Query query = new Query();
        query.addCriteria(criteria);

        Sort sort = new Sort(Sort.Direction.ASC, properties);
        query.with(sort);

        List<ConferenceInfo> list = mongoTemplate.find(query, ConferenceInfo.class, collectionName);

        return list;


    }

    public List<ConferenceInfo> selectTag(String collectionName){
        DBObject dbObject = new BasicDBObject();
        DBObject fieldObject = new BasicDBObject();
        fieldObject.put("tag", 1);
        fieldObject.put("name",1);
        fieldObject.put("_id", 0);

        Query query = new BasicQuery(dbObject, fieldObject);

        Sort sort = new Sort(Sort.Direction.ASC, "startdate");
        query.with(sort);

        List<ConferenceInfo> list = mongoTemplate.find(query, ConferenceInfo.class, collectionName);


        return list;
    }


}
