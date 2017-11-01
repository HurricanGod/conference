## Web接口API

-----

###  查询近期要召开的会议

+ **days** 参数必选，表示最近的天数，如`days=7`表示查询从今天到下星期内将要举办的会议
+ **page** 参数必选，分页查询参数，表示第几页，每页默认返回8条数据

例：

`http://localhost:8080/conference/conference/latest.do?days=7&page=1`



### 查询会议的所有 Tag

`http://localhost:8080/conference/conference/getTag.do`

**返回**：**json** 对象数组，对象有3个属性**name**、**tag**、**number**

+ **tag**：表示会议的 tag
+ **number**： 表示所有会议中含有 **tag**的会议数量， number越大表示 `Tag` 越热门

![getTag.do图](https://github.com/HurricanGod/conference/blob/master/img/getTag.png)



----

### 根据 Tag 分页查询近期要举办的会议

`http://localhost:8080/conference/conference/getHotConference.do`（**只接收post请求**）

**post请求需要提交的请求参数**：

+ `days`：用于查询未来 days 天内将要举办的会议
+ `page`：分页查询参数，表示第几页，默认每页8条数据
+ `tagString`：**tag** 参数，可以有多个**tag**，每个**tag**用`,`分隔开。

例：

![getHotConference](https://github.com/HurricanGod/conference/blob/master/img/getHotConference.png)

![getHotConference1](https://github.com/HurricanGod/conference/blob/master/img/getHotConference1.png)



------

### 查询给定时间内点赞数前n的会议



**示例请求URL**：

+ `http://localhost:8080/conference/push/getPopularMeeting.do?startTime=2017-10-30&offsetDay=10&top=10`

+ `http://localhost:8080/conference/push/getPopularMeeting.do?offsetDay=10&top=10`

  ​

查询参数：

+ `startTime`：字符串类型(**可选**)，表示会议举办的起始日期，如果没有传递这个参数，会默认使用系统当前日期，格式只能为`yyyy-MM-dd`
+ `offsetDay`：整数（**必选**），表示日期范围，可以正数或负数；
  + 正数表示含义： startTime 到 startTime + offsetDay 范围内的时间
  + 负数表示含义： startTime - offsetDay 到 startTime 范围内的时间
+ `top`：整数（**必选**），表示获得点赞数前 top 的会议



**返回结果**：**json数组**，每个元素为会议信息对象

![getPopularMeeting](https://github.com/HurricanGod/conference/blob/master/img/getPopularMeeting.png)



-----

### 每日推荐热点 Tag



请求URL示例：

+ `http://localhost:8080/conference/push/queryPopTag.do?startTime=2017-11-1&offset=10&top=10`
+ `http://localhost:8080/conference/push/queryPopTag.do?startTime=2017-11-1&offset=60`

**请求参数**：

+ `startTime`：<a name="startTime">字符串类型（**必选**），表示会议举办的日期，用于筛选在`startTime`以后举办的会议，格式只能为`yyyy-MM-dd`</a>
+ `offset` ：<a name="offset">整型（**必选**），相对`startTime`的日期偏移量</a>
+ `top` ：整型（**可选**），如果未传递此参数则默认为5，`top`允许的**最大值为10**



**startTime**、**top** 和 **offset**  的含义解析：如果传递的**startTime**的值为`2017-11-1` ，**offset** 的值为 `10` ，**top=10**；那么将查询举办日期在`2017-11-1` ~ `2017-11-11`范围内被点赞数前10的会议的Tag

![queryPopTag](https://github.com/HurricanGod/conference/blob/master/img/queryPopTag.png)

![queryPopTag.gif](https://github.com/HurricanGod/conference/blob/master/img/queryPopTag1.gif)



----

### 根据热门Tag查询会议

请求URL示例：

+ `http://localhost:8080/conference/push/queryConferenceByTag.do`



**请求参数**：

+ `tag` ：字符串型（**必选**）
+ `startTime` ：字符串型（**必选**），格式只能是**yyyy-MM-dd**格式，<a href="#startTime">含义同上</a>
+ `offsetTime` ：整型（**必选**），<a href="#offset">含义参考offset</a>
+ `page` ：整型（**可选**），未传递此参数默认为0，不传递此参数时可通过`perPageNumber`来控制分页，相当于执行了`limit 0, perPageNumber`这样的分页查询语句
+ `perPageNumber` ：整型（**可选**），不传递此参数默认为8

![queryConferenceByTag](https://github.com/HurricanGod/conference/blob/master/img/queryConferenceByTag.gif)

![queryConferenceByTag](https://github.com/HurricanGod/conference/blob/master/img/queryConferenceByTag1.gif)



###  根据热门Tag查询会议含有Tag的会议数量

请求URL示例：

`http://localhost:8080/conference/push/queryNumOfConferenceByTag.do`



**请求参数**：

- `tag` ：字符串型（**必选**）
- `startTime` ：字符串型（**必选**），格式只能是**yyyy-MM-dd**格式，<a href="#startTime">含义同上</a>
- `offsetTime` ：整型（**必选**），<a href="#offset">含义参考offset</a>

![queryNumOfConferenceByTag](https://github.com/HurricanGod/conference/blob/master/img/queryNumOfConferenceByTag.gif)



-----



