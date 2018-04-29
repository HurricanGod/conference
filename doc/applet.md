## <a name="top">小程序相关接口文档</a>

**更新时间** ： `2018年4月27日 16:01:25`

-------

+ <a href="#save">保存小程序用户openid</a>


+ <a href="#setAppid">设置小程序appid和appsecret</a>


+ <a href="#emailCheck">获取邮箱校验验证码</a>


+ <a href="#sendEmail">发送邮件</a>


+ <a href="#checkSessionl">**检查是否需要调用wx.login()接口**</a>


+ <a href="#init">**小程序初始化接口**</a>


+ <a href="#updateUserSetting">**修改小程序个人设置**</a>





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


+ `retCode` —— 状态码，0表示正常返回


+ `message` —— 返回给服务端的提示信息


+ `model` —— 用于存放各种不同类型数据的集合




<p align="right"><a href="#top">返回顶部</a></p>

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



<p align="right"><a href="#top">返回顶部</a></p>

------

<a name="emailCheck">获取邮箱校验验证码</a>

***Url*** ：`/conference/publish/checkEmail.do`





**请求参数** ：

| 必要参数 |  参数名  |   类型   |  描述  |
| :--: | :---: | :----: | :--: |
|  是   | email | String | 邮箱地址 |
|      |       |        |      |



<p align="right"><a href="#top">返回顶部</a></p>

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




<p align="right"><a href="#top">返回顶部</a></p>

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

- `sessionIsExpired`b表示会话是否过期




<p align="right"><a href="#top">返回顶部</a></p>

-----

<a name="init">**小程序初始化接口**</a>

***Url*** ：`/conference/common/init.do`

**此接口将会成为小程序端获取大部分数据的重量级接口**



**请求参数** ：

| 必要参数 | 参数名  |   类型    |  描述  |
| :--: | :--: | :-----: | :--: |
|  是   | uid  | Integer |      |
|      |      |         |      |

**响应** ：

<a href="#ResMessage">`ResMessage`对象</a>

**样例** ：

```json
{
    "retCode": 0,
    "message": null,
    "log": null,
    "model": {
        "conferenceType": [
            "人工智能",
            "网络与信息安全",
            "物联网",
            "计算机网络",
            "计算机软件"
        ],
        "userSetting": {
            "uid": 66,
            "username": null,
            "userpwd": null,
            "userrole": 2,
            "email": null,
            "followType": null,
            "extendJson": null,
            "lastUpdateTime": 1524940617087
        }
    }
}
```

**主要字段说明** ：（设服务端返回的`ResMessage`对象的变量名为data）

|             字段              | 描述                                       |
| :-------------------------: | :--------------------------------------- |
| `data.model.conferenceType` | 会议类型，相当于会议实体的Tag，列表类型                    |
|  `data.model.userSetting`   | 用户个人设置实体<br/>`followType`：感兴趣的会议类型，可以有多个，以`,`隔开<br/>`email`：用户设置的验证邮箱，每次修改邮箱变更后需要进行邮箱验证<br/>`extendJson`：`json`格式字符串，用户设置里面如果想保存更多的设置信息可以存到这个字段里<br/>`uid`：小程序用户唯一ID |
|                             |                                          |
|                             |                                          |



<p align="right"><a href="#top">返回顶部</a></p>

----

<a name="updateUserSetting">**修改小程序个人设置**</a>

***Url*** ：`/conference/user/updateUserSetting.do`



**请求参数** ：

| 必要参数 | 参数名  |               类型               |  描述  |
| :--: | :--: | :----------------------------: | :--: |
|  是   | user | <a href="#uservo">`UserVo`</a> |      |
|      |      |                                |      |

<a name="uservo">`UserVo实体`</a>

![uservo]()



**响应** ：

<a href="#ResMessage">`ResMessage`对象</a>， `retCode`为0表示成功

**返回样例** ：

```json
{"retCode":0,"message":"成功保存个人设置","log":null,"model":{}}
```



**修改小程序个人设置活动图如下所示** ：

![修改个人设置]()



<p align="right"><a href="#top">返回顶部</a></p>



------

