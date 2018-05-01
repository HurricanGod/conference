package cn.hurrican.utils;

import cn.hurrican.constant.QQEmailConfig;
import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.lang.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class QQEmailUtils {



    /**
     * 用户名密码验证，需要实现抽象类Authenticator的抽象方法PasswordAuthentication，
     * SMTP验证类(内部类)，继承javax.mail.Authenticator
     */
    static class MyAuthenticator extends Authenticator {
        String username;
        String password;

        public MyAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }


    /**
     * 腾讯企业邮箱发送邮件
     * @param subject 邮件主题
     * @param qqEmailConfig 腾讯邮箱配置信息
     * @param sendHtml 发送的内容
     * @param receiveUser 邮件接收者
     * @param filePath 附件路径
     * @return
     */
    public static Boolean sendEmail(String subject, QQEmailConfig qqEmailConfig,
                             String sendHtml, String receiveUser, List<String> filePath)
            throws GeneralSecurityException, UnsupportedEncodingException, MessagingException {
        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", qqEmailConfig.getProtocol());// 协议
        prop.setProperty("mail.smtp.host", qqEmailConfig.getHost());// 服务器
        prop.setProperty("mail.smtp.port", qqEmailConfig.getPort().toString());// 端口
        // 使用smtp身份验证
        prop.setProperty("mail.smtp.auth", qqEmailConfig.getAuth().toString());
        // 使用SSL开启安全协议
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);

        prop.put("mail.smtp.ssl.enable", qqEmailConfig.getEnableSSL());
        prop.put("mail.smtp.ssl.socketFactory", sf);
        Session session = Session.getDefaultInstance(prop, new MyAuthenticator(
                qqEmailConfig.getUsername(), qqEmailConfig.getPassword()));
        // 开启DEBUG模式,在控制台中或日志中有日志信息输出
        session.setDebug(qqEmailConfig.getOpenDebug());
        MimeMessage message = new MimeMessage(session);
        InternetAddress from;
        if (StringUtils.isNotBlank(qqEmailConfig.getSenderNickname())) {
            from = new InternetAddress(MimeUtility.encodeWord(qqEmailConfig.getSenderNickname())
                            + "<" + qqEmailConfig.getUsername() + ">"); // 发件人,昵称
        } else {
            from = new InternetAddress(MimeUtility.encodeWord(qqEmailConfig.getSenderNickname()));
        }
        message.setFrom(from);
        // 创建收件人列表
        if (StringUtils.isNotBlank(receiveUser)) {
            // 替换收件人的分隔符（防止中文下产生无法分割成收件人数组的'，'替换成','）
            String receiver = receiveUser.replaceAll("，", ",");
            String[] arr = receiver.split(",");
            if (arr.length > 0) {
                Address[] address=new Address[arr.length];// 收件人
                for (int i = 0; i < arr.length; i++) {
                    address[i] = new InternetAddress(arr[i]);
                }
                message.setRecipients(Message.RecipientType.TO, address);
                message.setSentDate(new Date());// 发送时间
                message.setSubject(subject);// 邮件主题
                // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
                Multipart multipart = new MimeMultipart();
                BodyPart contentPart = new MimeBodyPart();
                contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
                multipart.addBodyPart(contentPart);// 添加邮件正文
                // 附件操作
                if (filePath != null && filePath.size() > 0) {
                    for (String filename : filePath) {
                        if(!new File(filename).exists()){
                            continue;
                        }
                        BodyPart attachmentBodyPart = new MimeBodyPart();
                        DataSource source = new FileDataSource(filename);
                        attachmentBodyPart.setDataHandler(new DataHandler(
                                source));
                        // MimeUtility.encodeWord可以避免文件名乱码
                        attachmentBodyPart.setFileName(MimeUtility
                                .encodeWord(source.getName()));
                        multipart.addBodyPart(attachmentBodyPart);
                    }
                }
                message.setContent(multipart);// 将multipart对象放到message中
                message.saveChanges();// 保存邮件
                Transport.send(message,address);
                System.out.println("send  email success!");
            }
        }
        return true;
    }


    public static void main(String[] args) throws GeneralSecurityException,
            UnsupportedEncodingException, MessagingException {
        QQEmailConfig config = new QQEmailConfig();
        config.setAuth(true);
        config.setEnableSSL(true);
        config.setOpenDebug(true);
        config.setSenderNickname("来自本机的main方法");
        config.setProtocol("smtp");
        config.setHost("smtp.exmail.qq.com");
        config.setPassword("Get Password From Teacher");
        config.setUsername("wx@zsn.cc");
        config.setPort(465);

        boolean sendSuccess = sendEmail("腾讯企业邮箱测试", config, "这是一封测试邮件",
                "1807363937@qq.com", null);
        System.out.println("\nsendSuccess = " + sendSuccess);

    }
}
