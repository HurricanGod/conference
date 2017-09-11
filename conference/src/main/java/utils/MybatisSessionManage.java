package utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by NewObject on 2017/8/11.
 */
public class MybatisSessionManage {
    private static InputStream stream = null;
    private static SqlSessionFactory factory = null;
    static {
        try {
            stream = Resources.getResourceAsStream("mybatis.xml");
            if (stream != null) {
                factory = new SqlSessionFactoryBuilder().build(stream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static SqlSession getSqlSession(){
        return factory.openSession();
    }

    public static SqlSession getSqlSessionAutoCommit(){
        return factory.openSession(true);
    }
}
