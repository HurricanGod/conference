package cn.hurrican.service;

import cn.hurrican.utils.DateTimeUtils;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by NewObject on 2017/11/18.
 */
public class JavaEmailSender {


    public static void sendEmail(String toEmailAddress,String emailTitle,
                                 String emailContent, String password)throws Exception{
        Properties props = buildEmailProperties();
        //创建会话
        Session session = Session.getInstance(props);

        //发送的消息，基于观察者模式进行设计的
        Message msg = new MimeMessage(session);
        msg.setSubject(emailTitle);

        StringBuilder builder = new StringBuilder();
        builder.append("\n"+emailContent);
        builder.append("\n时间 " + DateTimeUtils.format(new Date()));
        msg.setText(builder.toString());
        msg.setFrom(new InternetAddress("1257164130@qq.com"));

        Transport transport = session.getTransport();
        transport.connect("smtp.qq.com", "1257164130@qq.com", password);
        //发送消息
        transport.sendMessage(msg, new Address[]{new InternetAddress(toEmailAddress)});
        transport.close();
    }

    private static Properties buildEmailProperties() throws GeneralSecurityException {
        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        /**
         * SSL认证，注意腾讯邮箱是基于SSL加密的，所有需要开启才可以使用
         **/
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        return props;
    }
}
