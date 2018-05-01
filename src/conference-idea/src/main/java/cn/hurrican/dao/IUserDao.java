package cn.hurrican.dao;

import cn.hurrican.beans.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NewObject on 2017/10/30.
 */
public interface IUserDao {

    /**
     * 把用户个人信息保存到数据库并返回受影响的行
     * @param user 用于存放用户设置的信息：用户名、密码(可选)、邮箱、id
     *             保存时不用 id 值，这里采用 id 自增长策略；
     *             成功保存后生成的 id 会被映射到 参数 user 里
     *
     * @return 受影响的行数
     */
    Integer insertOneUser(User user);


    /**
     * 查询所有用户
     * @return User集合
     */
    List<User> queryAllUser();


    /**
     * 根据用户名和密码查询是否存在指定用户
     * 主要用于登录检查
     * @param map sql语句的动态参数，有3个参数名，分别为：
     *              username(必选)：用户名
     *              pwd(必选)：密码
     *              role(可选)：用户类型标志，0为管理员，1为普通用户
     * @return 用户名存在且密码正确则返回 1，否则返回 0
     */
    Integer queryUserByNameAndPassword(HashMap<String,Object> map);


    void updateUserSetting(User user);

    List<User> selectUserSetting(Map<String,Object> params);

    List<User> queryUserByRole(Integer roleType);
}
