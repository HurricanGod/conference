## 小程序相关接口文档

**更新时间** ： `2018年4月27日 16:01:25`

-------

+ <a href="#save">保存小程序用户openid</a>


+ <a href="#setAppid">设置小程序appid和appsecret</a>


+ <a href="#emailCheck">获取邮箱校验验证码</a>


+ <a href="#sendEmail">发送邮件</a>


+ <a href="#checkSessionl">**检查是否需要调用wx.login()接口**</a>

-----

### <a name="save">保存小程序用户openid</a>
***Url*** ：`/conference/applet/weixinLogin.do`



**请求参数** ：

| 必要参数 |   参数名    |   类型   |          描述          |
| :--: | :------: | :----: | :------------------: |
|  是   |   code   | String | wx.login()调用后获取的code |
|  否   | nickname | String |    用户昵称，首次调用建议带上     |
|  否   | headimg  | String |   用户头像地址，首次调用建议带上    |



**响应** ：

<a name="ResMessage">`ResMessage`对象</a>

<img align="right" src="https://github.com/HurricanGod/conference/blob/master/doc/img/ResMessage.png"/>

+ `log` —— 通常用于定位问题的日志信息


+ `retCode` —— 状态码，一般情况下0表示正常返回


+ `message` —— 返回给服务端的提示信息


+ `model` —— 用于存放各种不同类型数据的集合


------

### <a name="setAppid">设置小程序appid和appsecret</a>

***Url*** ：`/conference/applet/setAppletConfig.do`





**请求参数** ：

| 必要参数 |  参数名   |   类型   |    描述    |
| :--: | :----: | :----: | :------: |
|  是   | appid  | String | 小程序appid |
|  是   | secret | String |          |



**响应** ：

<a href="#ResMessage">`ResMessage`对象</a>



------

<a name="emailCheck">获取邮箱校验验证码</a>

***Url*** ：`/conference/publish/checkEmail.do`





**请求参数** ：

| 必要参数 |  参数名  |   类型   |  描述  |
| :--: | :---: | :----: | :--: |
|  是   | email | String | 邮箱地址 |
|      |       |        |      |

**说明** ：

**目前使用的是QQ邮箱发送验证码，需要定期生成授权码，若没有收到邮件可能是授权码过期了！**

-----

<a name="sendEmail">发送邮件</a>

***Url*** ：`/conference/common/sendEmail.do`



**请求参数** ：

| 必要参数 |      参数名       |   类型   |  描述  |
| :--: | :------------: | :----: | :--: |
|  是   |    subject     | String | 邮件主题 |
|  是   |    content     | String | 邮件正文 |
|  是   | `receiveUsers` | String | 邮箱地址 |
|      |                |        |      |



**响应** ：

<a href="#ResMessage">`ResMessage`对象</a>

```json
{
  "retCode":0,
  "message":null,
  "log":null,
  "model":{
    "successSendEmail":true
  }
}
```

**备注** ：

+ 正常返回字段`log`为null，服务器发生异常时则会返回相应的异常提示
+ `ResMessage.model.successSendEmail`为Boolean类型，表示是否成功发送发送邮件



----

<a name="checkSessionl">**检查是否需要调用wx.login()接口**</a>

***Url*** ：`/conference/applet/checkSession.do`



**请求参数** ：

| 必要参数 | 参数名  |   类型    |  描述  |
| :--: | :--: | :-----: | :--: |
|  是   | uid  | Integer |      |
|      |      |         |      |



**响应** ：

<a href="#ResMessage">`ResMessage`对象</a>

```json
{
  "retCode":0,
  "message":null,
  "log":null,
  "model":{
    "sessionIsExpired":true
  }
}
```

备注 ：

- `sessionIsExpired`表示会话是否过期

