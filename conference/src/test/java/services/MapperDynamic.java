package services;

import beans.WorkInfo;
import dao.IWorkInfoDao;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import utils.MybatisSessionManage;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by NewObject on 2017/8/14.
 */
public class MapperDynamic {
    private IWorkInfoDao dao;

    @Before
    public void startTest() {
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        dao = session.getMapper(IWorkInfoDao.class);
    }

    @Test
    public void queryWorkInfoByNumberIndexTest() {
        String position = "自动化";
        Integer userid = 3;
        List<WorkInfo> list = dao.queryWorkInfoByNumberIndex(userid, position);
        for (WorkInfo element : list) {
            System.out.println(element.toString());
        }
    }

    @Test
    public void addWorkInfoTest() {

        WorkInfo workInfo = new WorkInfo();
        workInfo.setCompany("北京蓝光科技有限公司");
        workInfo.setDuty("系统测试");
        workInfo.setPosition("自动化测试");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 1, 15);
        workInfo.setDeparture(calendar.getTime());
        dao.addWorkInfo(workInfo);
    }


    @Test
    public void queryWorkInfoByManyConditionsTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("idCon", 3);
        map.put("posCon", "自动化");
        List<WorkInfo> workExperiences = dao.queryWorkInfoByManyConditions(map);
        for (int i = 0; i < workExperiences.size(); i++) {
            WorkInfo workInfo = workExperiences.get(i);
            System.out.println(workInfo.toString());
        }
    }

    @Test
    public void dateTimeTest() {
        Date date = new Date();
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(dateFormat.format(date));
        System.out.println(datetimeFormat.format(date));

        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTime());
        calendar.set(2016, 10, 25);
        date = calendar.getTime();
        System.out.println(date);
        String dateStr = dateFormat.format(date);
        System.out.println(dateStr);
    }
}
