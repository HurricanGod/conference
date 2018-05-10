package cn.hurrican.service;

import cn.hurrican.beans.PublishMessage;
import cn.hurrican.beans.User;
import cn.hurrican.constant.QQEmailConfig;
import cn.hurrican.dao.IPublishDao;
import cn.hurrican.utils.CrawlerUtils;
import cn.hurrican.utils.DateTimeUtils;
import cn.hurrican.utils.QQEmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CrawlerService {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private UserService userService;

    @Resource(name = "IPublishDao")
    private IPublishDao dao;

    @Autowired
    private QQEmailConfig qqEmailConfig;

    public void sendRequestObtainMainConferenceContent(PublishMessage message){

        taskExecutor.submit(() -> {
            String uri = message.getUri();
            Map<String, String> map = CrawlerUtils.getSourceCode(uri);
            String statusCode = map.get("statusCode");
            if(!"200".equals(statusCode)){
                System.out.println("http 响应状态码不为200，失败下载网页源码");
                sendConferencePublishEmailNoticeToAdmin(message);
                return;
            }
            Map<String, Object> result = CrawlerUtils.parseHtml(map.get("sourceCode"));
            Set<String> mainContent = (Set<String>) result.get("mainContent");
            String contentString = mainContent.stream().reduce((e1, e2) -> e1 + "\n" + e2).orElse("");
            System.out.println("contentString = " + contentString);

            message.setMainContent(contentString);
            message.setStatusCode(Integer.valueOf(statusCode));

            dao.updatePublishConferenceSelective(message);

            sendConferencePublishEmailNoticeToAdmin(message);
            System.out.println("执行异步任务完成！");

        });
    }

    private void sendConferencePublishEmailNoticeToAdmin(PublishMessage message) {
        List<User> users = userService.queryAdminAccount();
        users.forEach(ele ->{
            if(ele.getAcceptNotice() && ele.getEmail() != null){
                StringBuilder builder = new StringBuilder();
                builder.append("通知：<br/>").append("用户于 ").append(DateTimeUtils.format(new Date()))
                .append(" 发布了会议！<br/>会议官网地址为： ").append(message.getUri())
                .append("<br/>经初步检测访问会议网站返回的状态码为： ").append(message.getStatusCode())
                .append("<br/>会议官网大致内容如下：<br/>").append(message.getMainContent())
                .append("<br/><br/>请及时登录后台配置会议关键信息解析规则");

                try {
                    QQEmailUtils.sendEmail("用户发布会议通知", qqEmailConfig, builder.toString(),
                            ele.getEmail(), null);
                } catch (GeneralSecurityException | UnsupportedEncodingException | MessagingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
