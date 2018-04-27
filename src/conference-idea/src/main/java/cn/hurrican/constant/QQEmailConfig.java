package cn.hurrican.constant;

import lombok.Data;

@Data
public class QQEmailConfig {

    private String protocol;
    private String host;
    private Integer port;
    private Boolean auth;
    private String username;
    private String password;
    private Boolean enableSSL;
    private Boolean openDebug;
    private String senderNickname;
}
