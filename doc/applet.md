## 小程序相关接口文档

**更新时间** ： `2018年4月22日 00:17:45`

-------

+ <a href="#save">保存小程序用户openid</a>


+ <a href="#setAppid">设置小程序appid和appsecret</a>

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

<img align="right" src=""/>

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

