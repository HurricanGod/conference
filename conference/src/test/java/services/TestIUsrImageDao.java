package services;

import beans.UserInfo;
import beans.UsrImage;
import dao.IUsrImageDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by NewObject on 2017/8/17.
 */
public class TestIUsrImageDao {


    @Test
    public void queryImageInfoByUserIdTest() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        // 创建第一个SqlSession并创建dao实例
        SqlSession session = factory.openSession(true);
        IUsrImageDao dao = session.getMapper(IUsrImageDao.class);
        System.out.println("二级缓存测试\n第一次查询");
        Set<UsrImage> images = dao.queryImageInfoByUserId(4);
        for (UsrImage element : images) {
            System.out.println(element.toString());
        }

        // 执行完成后关闭SqlSession
        session.close();

        System.out.println("第二次查询");
        // 再次创建SqlSession对象并获取dao实例
        session = factory.openSession(true);
        dao = session.getMapper(IUsrImageDao.class);
        // 再次执行一样的查询
        images = dao.queryImageInfoByUserId(4);
        for (UsrImage element : images) {
            System.out.println(element.toString());
        }

    }

    @Test
    public void addImageTest() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession(true);
        IUsrImageDao dao = session.getMapper(IUsrImageDao.class);
        UsrImage image = new UsrImage("mybatis3.png", "c:/user/image/mybatis3.png");
        image.setUpload_date( Calendar.getInstance().getTime());
        UserInfo user = new UserInfo();
        user.setId(4);
        image.setUser(user);
        dao.addImage(image);
    }
}
