package cn.hurrican.controller;

import cn.hurrican.beans.User;
import cn.hurrican.cache.UserCache;
import cn.hurrican.constant.AppConstant;
import cn.hurrican.constant.BusinessCode;
import cn.hurrican.dtl.ResMessage;
import cn.hurrican.dtl.UserVo;
import cn.hurrican.exception.NecessaryParameterNullException;
import cn.hurrican.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

/**
 * Created by NewObject on 2017/10/30.
 */
@Controller
@RequestMapping(value = "/conference/user")
public class UserController {

    @Resource(name = "userService")
    private UserService service;

    @Autowired
    private UserCache userCache;


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
        return new Serializable() {public boolean isLogin = flag;};
    }

    @RequestMapping(value = "/updateUserSetting.do",produces="application/json;charset=UTF-8")
    @ResponseBody
    public ResMessage updateUserSetting(UserVo user){
        ResMessage resMessage = new ResMessage();
        if(user.getUid() == null){
            return resMessage.msg("uid不允许为null").retCodeEqual(BusinessCode.ParametersIsNullError.getCode());
        }
        if(AppConstant.UPDATE_EMAIL_ACTION.equals(user.getAction())){
            String verificationCode = user.getVerificationCode();
            String emailVerifyCode = userCache.getEmailVerifyCode(user.getUid());
            if(emailVerifyCode == null || verificationCode == null || !emailVerifyCode.equals(verificationCode)){
                return resMessage.msg("验证码错误！");
            }
        }
        service.updateUserSetting(user);
        return resMessage.retCodeEqual(BusinessCode.Ok.getCode()).msg("成功保存个人设置");
    }


    @RequestMapping(value = "/queryUserByRole.do", produces="application/json;charset=UTF-8")
    @ResponseBody
    public ResMessage queryUserByRole(Integer role){
        ResMessage resMessage = new ResMessage();
        List<User> users = service.queryUserByRole(role);
        resMessage.put("users", users);
        return resMessage;
    }
}
