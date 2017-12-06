package cn.hurrican.controller;

import cn.hurrican.beans.User;
import cn.hurrican.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * Created by NewObject on 2017/10/30.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource(name = "userService")
    private UserService service;


    @RequestMapping(value = "/init.do",
            produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Object initUserSetting(User user){

        Integer uid = service.insertOneUserAndReturnUserId(user);
        return new Serializable() {public Integer id = uid;};
    }

    @RequestMapping(value = "/queryUser.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryAllUsers(){

        return service.queryAllUserService();
    }


    @RequestMapping(value = "/doLogin.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object doLogin(User user, HttpServletRequest request){

        String username = user.getUsername();
        String pwd = user.getUserpwd();
        boolean flag = service.queryAccountAndPasswordIsMatch(username, pwd);

        if (flag) {
            HttpSession session = request.getSession();
            session.setAttribute("name",username);
        }
        return new Serializable() {public boolean isSuccess = flag;};
    }


    @RequestMapping(value = "/isLogin.do",produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object hasLogin(HttpServletRequest request){
        HttpSession session = request.getSession();
        boolean flag = session.getAttribute("name") == null;
        return new Serializable() {boolean isLogin = flag;};
    }
}
