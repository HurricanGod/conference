package services;

import beans.ConferenceInfo;
import dao.IConferenceInfoDao;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import utils.MybatisSessionManage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by NewObject on 2017/8/14.
 */
public class ConferenceInfoService {
    private IConferenceInfoDao dao;
    @Before
    public void startTest() {
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        dao = session.getMapper(IConferenceInfoDao.class);
    }

    @Test
    public void queryConferenceInfoByForEachListTest() {
        /**
         * @decription: list类型的in查询
         * 使用in查询前先在配置文件判断 list.size > 0
         */
        List<String> list = new ArrayList<>();
        list.add("杭州");
        list.add("大连市");
        List<ConferenceInfo> conferences = dao.queryConferenceInfoByForEachList(list);
        for (ConferenceInfo element : conferences) {
            System.out.println(element.toString());
        }
    }


    @Test
    public void queryConferenceInfoByForEachTest() {
        /**
         * @decription: array类型的in查询，
         * 使用in查询前先在配置文件判断 array.length > 0
         */
        String[] cities = {"杭州", "大连市"};
        List<ConferenceInfo> list = dao.queryConferenceInfoByForEach(cities);
        for (ConferenceInfo element : list) {
            System.out.println(element.toString());
        }
    }


    @Test
    public void addConferenceInfoTest() {
        ConferenceInfo conference = new ConferenceInfo();
        conference.setCnname("浙江大学“转录调控数据分析及R语言作图”研习班（第二期）招生通知");
        conference.setLocation("杭州");
        conference.setWebsite("http://meeting.sciencenet.cn/index.php?s=/Category/reading_display&rid=9899");
        Date startdate = Date.valueOf("2017-07-13");
        conference.setStartdate(startdate);
        Date enddate = Date.valueOf("2017-07-16");
        conference.setEnddate(enddate);
        conference.setTag("转录,测序");
//        conference.setSponsor("Interscience Institute of Management and Technology");
        dao.addConferenceInfo(conference);

    }

    @Test
    public void queryConferenceInfoByIfTest() {
        ConferenceInfo conference = new ConferenceInfo();
        conference.setId(null);
//        conference.setLocation("杭州");
        List<ConferenceInfo> conferences = dao.queryConferenceInfoByIf(conference);
        for (ConferenceInfo element : conferences) {
            System.out.println(element.toString());
        }
    }
    @Test
    public void queryConferenceInfoByIfUseMapTest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", null);
        map.put("location", "杭州");
        List<ConferenceInfo> conferences = dao.queryConferenceInfoByIf(map);
        for (ConferenceInfo element : conferences) {
            System.out.println(element.toString());
        }
    }




}
