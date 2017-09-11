import beans.UserInfo;
import dao.UserinfoDaoImp;
import org.junit.Test;

import java.util.List;

/**
 * Created by NewObject on 2017/8/11.
 */
public class TestUserinfo {


    @Test
    public void add() {
        UserInfo user = new UserInfo("卫平", "1995102111@163.com", "qwerasdf");
        new UserinfoDaoImp().add(user);

    }


    @Test
    public void getIdAfterAddTest() {
        UserInfo user = new UserInfo("HelloWorld", "15935746@qq.com", "123456789");
        new UserinfoDaoImp().getIdAfterAdd(user);
        System.out.println("user.toString() = " + user.toString());
    }


    @Test
    public void removeTest() {
        UserInfo user = new UserInfo();
        user.setId(2L);
        new UserinfoDaoImp().remove(user);
    }


    @Test
    public void updateTest() {
        UserInfo user = new UserInfo();
        user.setId(6l);
        user.setUsername("Weiping");
        user.setEmail("627512534@qq.com");
        new UserinfoDaoImp().update(user);
    }


    @Test
    public void queryAllTest() {
        List<UserInfo> list = new UserinfoDaoImp().queryAll();
        for (int i = 0; i < list.size(); i++) {
            UserInfo userInfo = list.get(i);
            System.out.println(userInfo.toString());
        }
    }


    @Test
    public void queryByIdTest() {
        UserinfoDaoImp daoImp = new UserinfoDaoImp();
        UserInfo user = daoImp.queryById(4l);
        System.out.println(user.toString());
    }


    @Test
    public void queryByFieldTest() {
        UserinfoDaoImp daoImp = new UserinfoDaoImp();
        List<UserInfo> list = daoImp.queryByField("163.com");
        for (int i = 0; i < list.size(); i++) {
            UserInfo userInfo = list.get(i);
            System.out.println(userInfo.toString());
        }
    }
}
