## 小程序Web接口API

-----

###  查询近期要召开的会议

**请求URL** `http://localhost:8080/conference/conference/latest.do`



**请求参数**

+ **days(必选)**，表示最近的天数，如`days=7`表示查询从今天到下星期内将要举办的会议
+ **page(必选)**，分页查询参数，表示第几页，每页默认返回8条数据



**返回**：

`http`响应类型为**application/json;charset=UTF-8**，返回对象集合，集合中的每个对象的属性如下所示：

<a name = "ConferenceMsg">`ConferenceMsg`对象</a>

```java
public class ConferenceMsg {

    private Long id;  // 会议id
    private String cnName; // 会议中文名（可能为null）
    private String enName; // 会议英文名（可能为null）
    private String tag; // 会议归类 Tag
    private String location; // 会议举办地点
    private String sponsor; // 会议举办方
    private String startdate; // 会议召开的时间
    private String enddate; // 会议结束时间
    private String deadline; // 论文提交截止时间
    private String acceptance; //
    private String website; // 会议官网
}
```



**网络请求示例** ：

`http://localhost:8080/conference/conference/latest.do?days=7&page=1`

![latest.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/latest.do-request.png)

![latest.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/latest.do-response.png)

-----

### 查询会议的所有 Tag

**请求URL**  `http://localhost:8080/conference/conference/getTag.do`



**请求参数** ： 无



**返回**：

**json** 对象数组，对象有3个属性**name**、**tag**、**number**

+ **tag**：表示会议的 tag
+ **number**： 表示所有会议中含有 **tag**的会议数量， number越大表示 `Tag` 越热门



**网络请求示例** ：

![getTag.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/getTag.do-request.png)

![getTag.do图](https://github.com/HurricanGod/conference/blob/master/img/getTag.png)



----

### 根据 Tag 分页查询近期要举办的会议



**请求URL**  `http://localhost:8080/conference/conference/getHotConference.do`

**备注** ：`只接收post请求`



**请求参数**：

+ `days`：用于查询未来 days 天内将要举办的会议
+ `page`：分页查询参数，表示第几页，默认每页8条数据
+ `tagString`：**tag** 参数，可以有多个**tag**，每个**tag**用`,`分隔开。



**返回** ：

`http`响应类型为**application/json;charset=UTF-8**，返回对象集合，集合中的每个对象参见<a href="#ConferenceMsg">`ConferenceMsg`</a>



**网络请求示例** ：

![getHotConference](https://github.com/HurricanGod/conference/blob/master/img/getHotConference.png)

![getHotConference.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/getHotConference.do-response.png)

![getHotConference1](https://github.com/HurricanGod/conference/blob/master/img/getHotConference1.png)



------

### 查询给定时间内点赞数前n的会议(作为精选会议的一部分)



**请求URL**  `http://localhost:8080/conference/push/getPopularMeeting.do`



**参数** ：

+ `startTime`：字符串类型(**可选**)，表示会议举办的起始日期，如果没有传递这个参数，会默认使用系统当前日期，格式只能为`yyyy-MM-dd`，**请在前端检验格式**
+ `offsetDay`：整数（**必选**），表示日期范围，可以正数或负数；
  + 正数表示含义： startTime 到 startTime + offsetDay 范围内的时间
  + 负数表示含义： startTime - offsetDay 到 startTime 范围内的时间
+ `top`：整数（**必选**），表示获得点赞数前 top 的会议




**例**：

- `http://localhost:8080/conference/push/getPopularMeeting.do?startTime=2017-10-30&offsetDay=10&top=10`

- `http://localhost:8080/conference/push/getPopularMeeting.do?offsetDay=10&top=10`

  ​



**返回**：

**json对象集合**，每个元素为会议信息对象，参见<a href="#ConferenceMsg">`ConferenceMsg`</a>



**网络请求示例** ：

![getPopularMeeting-request1](https://github.com/HurricanGod/conference/blob/master/doc/img/getPopularMeeting-request1.png)

![getPopularMeeting-request2](https://github.com/HurricanGod/conference/blob/master/doc/img/getPopularMeeting-request2.png)



![getPopularMeeting](https://github.com/HurricanGod/conference/blob/master/img/getPopularMeeting.png)



-----

### 每日推荐热点 Tag



**请求URL**  `http://localhost:8080/conference/push/queryPopTag.do`



**请求参数**：

+ `startTime`：<a name="startTime">字符串类型（**必选**），表示会议举办的日期，用于筛选在`startTime`以后举办的会议，格式只能为`yyyy-MM-dd`</a>，请在**客户端进行格式检验**
+ `offset` ：<a name="offset">整型（**必选**），相对`startTime`的日期偏移量</a>
+ `top` ：整型（**可选**），如果未传递此参数则默认为5，`top`允许的**最大值为10**




**startTime**、**top** 和 **offset**  的含义解析：如果传递的**startTime**的值为`2017-11-1` ，**offset** 的值为 `10` ，**top=10**；那么将查询举办日期在`2017-11-1` ~ `2017-11-11`范围内被点赞数前10的会议的Tag



例：

- `http://localhost:8080/conference/push/queryPopTag.do?startTime=2017-11-1&offset=10&top=10`
- `http://localhost:8080/conference/push/queryPopTag.do?startTime=2017-11-1&offset=60`



**返回** ：

字符串数组



**网络请求示例** ：

![queryPopTag](https://github.com/HurricanGod/conference/blob/master/img/queryPopTag.png)

![queryPopTag.gif](https://github.com/HurricanGod/conference/blob/master/img/queryPopTag1.gif)



----

### 根据热门Tag查询会议



**请求URL**  `http://localhost:8080/conference/push/queryConferenceByTag.do`



**请求参数**：

+ `tag` ：字符串型（**必选**）
+ `startTime` ：字符串型（**必选**），格式只能是**yyyy-MM-dd**格式，<a href="#startTime">含义同上</a>，请在**客户端进行格式检验**
+ `offsetTime` ：整型（**必选**），<a href="#offset">含义参考offset</a>
+ `page` ：整型（**可选**），未传递此参数默认为0，不传递此参数时可通过`perPageNumber`来控制分页，相当于执行了`limit 0, perPageNumber`这样的分页查询语句
+ `perPageNumber` ：整型（**可选**），不传递此参数默认为8



**返回** ：

**json对象集合**，每个元素为会议信息对象，参见<a href="#ConferenceMsg">`ConferenceMsg`</a>



**网络请求示例** ：

![queryConferenceByTag](https://github.com/HurricanGod/conference/blob/master/img/queryConferenceByTag.gif)

![queryConferenceByTag](https://github.com/HurricanGod/conference/blob/master/img/queryConferenceByTag1.gif)

-----

###  根据热门Tag查询会议含有Tag的会议数量



**请求URL** ：`http://localhost:8080/conference/push/queryNumOfConferenceByTag.do`



**请求参数**：

- `tag` ：字符串型（**必选**）
- `startTime` ：字符串型（**必选**），格式只能是**yyyy-MM-dd**格式，<a href="#startTime">含义同上</a>请在**客户端进行格式检验**
- `offsetTime` ：整型（**必选**），<a href="#offset">含义参考offset</a>



**返回** ：

**json** 对象，该对象只有1个`count`字段，表示查询出来的会议数量



**网络请求示例** ：

![queryNumOfConferenceByTag](https://github.com/HurricanGod/conference/blob/master/img/queryNumOfConferenceByTag.gif)



-----

### 查询一段时间内(以 系统当前时间作为参考)将要举行会议的总数



**请求URL** ：`http://localhost:8080/conference/conference/queryLatestCount.do`



**请求参数**：

+ `offset` ：整型（**必选**），相对系统当前时间的日期偏移量<a href="#offset">含义参考offset</a>



**返回** ：

**json** 对象，该对象只有1个字段`number`，表示会议的数量



**网络请求示例** ：

![queryLatestCount.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/queryLatestCount.do-request.png)

![queryLatestCount.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/queryLatestCount.do-response.png)



--------

### 收藏会议



**请求URL** ：`http://localhost:8080/conference/collect/collectConference.do`



**请求参数**：

+ <a name="cid">`cid`</a> ：整型(**必选**)，被收藏会议的id
+ <a name="uid">`uid` </a>：整型(**必选**)，用户id



**返回** ：

**json对象** ，对象只有1个属性`isSuccess`，类型为**boolean** ，表示是否收藏成功



**网络请求示例** ：

![collectConference.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/collectConference.do-request.png)

![collectConference.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/collectConference.do-response.png)



------

### 根据会议id查询会议被收藏的次数



**请求URL** ：`http://localhost:8080/conference/collect/queryCollectedNumber.do`



**请求参数**：

+ `cid` ：整型(**必选**)，会议id



**返回** ：

**json对象** ，对象只有1个属性`number`，类型为**整型** ，表示该条会议被收藏的次数



**网络请求示例** ：

![queryCollectedNumber.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/queryCollectedNumber.do-request.png)

![queryCollectedNumber.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/queryCollectedNumber.do-response.png)



-----

### 查询用户是否收藏了某个会议



**请求URL** ：`http://localhost:8080/conference/collect/checkIsCollected.do`



**请求参数**：

- <a href="#cid">`cid`</a> ：整型(**必选**)，待收藏会议的id
- <a href="#uid">`uid` </a>：整型(**必选**)，用户id



**返回** ：

**json对象** ，对象只有1个属性`isCollected`，类型为**boolean** ，表示会议是否被用户收藏



**网络请求示例** ：

![checkIsCollected.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/checkIsCollected.do-request.png)

![checkIsCollected.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/checkIsCollected.do-response.png)



------

### 取消收藏



**请求URL** ：`http://localhost:8080/conference/collect/cancelCollectConference.do`



**请求参数**：

- <a href="#cid">`cid`</a> ：整型(**必选**)，会议的id
- <a href="#uid">`uid` </a>：整型(**必选**)，用户id



**返回** ：

**json对象** ，对象只有1个属性`isSuccess`，类型为**boolean** ，表示会议是否被用户取消收藏



**网络请求示例** ：

![cancelCollectConference.do-request]()

![cancelCollectConference.do-response]()



-------

### 根据用户id查询收藏会议的总数



**请求URL** `http://localhost:8080/conference/collect/queryMyCollectionCount.do`



**请求参数**：

+ <a href="#uid">`uid` </a>：整型(**必选**)，用户id



**返回** ：

**json对象** ，对象只有1个属性`number`，类型为**整型** ，表示用户收藏会议的总数



**网络请求示例** ：

![queryMyCollectionCount.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/queryMyCollectionCount.do-request.png)

![queryMyCollectionCount.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/queryMyCollectionCount.do-response.png)



-----

### 分页查询用户收藏的会议



**请求URL** `http://localhost:8080/conference/collect/queryMyCollections.do`



**请求参数**：

- <a href="#uid">`uid` </a>：整型(**必选**)，用户id
- `startTime` (**可选**)：参见<a href="#startTime">上文**startTime**</a>
- `offset` (**可选**)：参考<a href="#offset">`offset`</a>
- `page` (**可选**)：整型，分页查询中的页数，如未传递默认为 1
- `perPageNumber` (**可选**)：分页查询每页数据条数，如未传递默认为 8



**返回** ：

**json对象集合**，每个元素为会议信息对象，参见<a href="#ConferenceMsg">`ConferenceMsg`</a>



**网络请求示例** ：

![queryMyCollections.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/queryMyCollections.do-request.png)

![queryMyCollections.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/queryMyCollections.do-response.png)



----

### 点赞会议

**注意** ：客户端点赞前请先发送一次请求到服务端判断用户有没有对该条会议点赞过



**请求URL** `http://localhost:8080/conference/perference/admire.do`



**请求参数**：

+ `conferenceid` (**必选**) ：整型，会议id
+ `uid` (**必选**) ：整型，用户id



**返回** ：

**json对象** ，对象只有1个属性`isSuccess`，类型为**boolean** ，表示是否点赞成功



**网络请求示例** ：

![admire-request](https://github.com/HurricanGod/conference/blob/master/doc/img/pngadmire-request.png)

![admire-response](https://github.com/HurricanGod/conference/blob/master/doc/img/admire-response.png)



-----

### 根据用户id与会议id查询用户是否对会议点过赞



**请求URL** `http://localhost:8080/conference/perference/checkIsAdmired.do`



**请求参数**：

- `conferenceid` (**必选**) ：整型，会议id
- `uid` (**必选**) ：整型，用户id



**返回** ：

**json对象** ，对象只有1个属性 `isSuccess`，**boolean**类型，点过赞则返回`true`，否则返回`false`



**网络请求示例** ：

![checkIsAdmired-request](https://github.com/HurricanGod/conference/blob/master/doc/img/checkIsAdmired-request.png)

![checkIsAdmired-response](https://github.com/HurricanGod/conference/blob/master/doc/img/checkIsAdmired-response.png)

-----

### 根据会议id查询会议被点赞的次数



**请求URL**  `http://localhost:8080/conference/perference/queryCount.do`



**请求参数**：

- `conferenceid` (**必选**) ：整型，会议id



**返回** ：

**json对象** ，对象只有1个属性`number`，类型为**整型** ，表示会议被点赞的总次数



**网络请求示例** ：

![queryCountPraise.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/queryCountPraise.do-request.png)

![queryCountPraise.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/queryCountPraise.do-response.png)



------

### 取消点赞



**请求URL**  `http://localhost:8080/conference/perference/unPraise.do`



**请求参数**：

- `conferenceid` (**必选**) ：整型，会议id
- `uid` (**必选**) ：整型，用户id



**返回** ：

**json对象** ，对象只有1个属性 `isSuccess`，**boolean**类型，取消点赞则返回`true`，否则返回`false`



**网络请求示例** ：

![unPraise.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/unPraise.do-request.png)

![unPraise.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/unPraise.do-response.png)



-----

### 根据用户id查询用户点赞过的会议总数



**请求URL**  `http://localhost:8080/conference/perference/queryUserPraisedCount.do`



**请求参数**：

+ `uid`(**必选**)： 用户id，整型
+ `startTime`(**可选**)：约束参见<a href="#startTime">**startTime**</a>
+ `offset` (**可选**)：约束参考<a href="#offset">`offset`</a>，未传递此参数后台默认生成并赋值为7



**建议** ：

前端把上面3个参数封装成一个对象



**返回** ：

**json对象** ，对象只有1个属性`number`，整型，表示用户点赞过的会议总数



**网络请求示例** ：

![queryUserPraisedCount.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/queryUserPraisedCount.do-request.png)

![queryUserPraisedCount.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/queryUserPraisedCount.do-response.png)



-----

### 根据用户 id 分页查询用户点赞过的会议



**请求URL**  `http://localhost:8080/conference/perference/queryPraisedConference.do`



**请求参数**：

- `uid`(**必选**)： 用户id，整型
- `startTime`(**可选**)：约束参见<a href="#startTime">**startTime**</a>
- `offset` (**可选**)：约束参考<a href="#offset">`offset`</a>，未传递此参数后台默认生成并赋值为7


- `page` (**可选**)：整型，分页查询中的页数，如未传递默认为 1
- `perPageNumber` (**可选**)：分页查询每页数据条数，如未传递默认为 8



**建议** ：

前端把上面5个参数封装成一个对象



**返回** ：

**json对象数组**，数组中每个元素为<a href="#ConferenceMsg">`ConferenceMsg`</a>对象



**网络请求示例** ：

![queryPraisedConference.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/queryPraisedConference.do-request.png)

![queryPraisedConference.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/queryPraisedConference.do-response.png)



-----

### 发布会议



**请求URL**  `http://localhost:8080/conference/publish/savePublishConference.do`



**请求参数**：

+ `uri` (**必选**)：字符串型，要发布会议的网址
+ `email`  (**必选**)：字符串型，发布者联系邮箱
+ `organization`(**可选**)：字符串型，发布机构
+ `description`(**可选**)：字符串型，其它描述信息



**建议** ：

建议使用`post`请求



**返回** ：

**json**对象，对象只有一个属性`isSuccess`，**true**表示发布成功，**false**表示发布失败，可能的原因为已经有其他人发布了这个会议



**网络请求示例** ：

![savePublishConference.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/savePublishConference.do-request.png)

![savePublishConference.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/savePublishConference.do-response.png)



-----

### 会议信息纠错



**请求URL**  `http://localhost:8080/conference/feedback/submitError.do`



**请求参数**：

+ `conferenceid`(**必选**) ： 整型，出错会议id
+ `errordetail`(**必选**) ：字符串，错误描述



**建议** ：把上面两个参数封装成对象，使用`JSON.stringify()`方法发送**ajax**请求



**返回** ：

**json**对象，对象只有一个属性`isSuccess`，类型为boolean，**true**表示成功提交纠错内容



**网络请求示例** ：

![submitError.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/submitError.do-request.png)

![submitError.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/submitError.do-response.png)



----

### 用户反馈



**请求URL**  `http://localhost:8080/conference/feedback/submitSuggestion.do`



**请求参数**：

+ `description`(**必选**)：字符串类型，表示用户反馈信息
+ `email`(**可选**)：字符串类型，用户邮箱



**返回** ：

**json**对象，对象只有一个属性`isSuccess`，类型为boolean，**true**表示成功提交反馈



**网络请求示例** ：

![submitSuggestion.do-request](https://github.com/HurricanGod/conference/blob/master/doc/img/submitError.do-request.png)

![submitSuggestion.do-response](https://github.com/HurricanGod/conference/blob/master/doc/img/submitSuggestion.do-response.png)



