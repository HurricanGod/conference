/*
全部接口请看：https://github.com/AllenMinD/conference/blob/master/doc/web-api.md

发现页这里上面 “精选” 部分需要用到的接口

1. 每日推荐热点 Tag：
http://localhost:8080/conference/push/queryPopTag.do?startTime=2017-11-1&offset=10&top=10

2. 根据热门Tag查询会议：
http://localhost:8080/conference/push/queryConferenceByTag.do?tag=人工智能&startTime=2017-11-1&offsetTime=10

3. 根据热门Tag查询会议含有Tag的会议数量：
http://localhost:8080/conference/push/queryNumOfConferenceByTag.do?tag=人工智能&startTime=2017-11-1&offsetTime=10

具体实现：
1. 当进入“发现页”时，就开始发起请求，请求【每日推荐热点 Tag】和 【根据热门Tag查询会议含有Tag的会议数量】这2个接口
2. 把【会议数量】渲染到 “发现页” 和 “tag.wxml” 中
3. “发现页”只显示前3个【热点Tag】，其他的Tag放在【其他热门关键词】里面（即tag.wxml）
4. 点进去具体的某个热门Tag时，会发起请求，请求【根据热门Tag查询会议】这个接口
5. 把 4 请求回来的 【会议】显示在相应的 popular.wxml 中
*/

// pages/explore/explore.js
var base64 = require("../example/images/base64");

//获取应用实例
var app = getApp()

//请求数据的主域名
var reqUrl = app.globalData.reqUrl

// 临时存储Tag的会议数量
var tagCount = 0;

Page({
  data:{
    popTag: [],
    popTagString: ''
  },
  onLoad:function(options){
    var that = this;
    that.loadPopTag();
    this.setData({
        icon: base64.icon20
    });
  },
  onReady:function(){
    // 页面渲染完成
  },
  onShow:function(){
    // 页面显示
  },
  onHide:function(){
    // 页面隐藏
  },
  onUnload:function(){
    // 页面关闭
  },

  // 请求【每日推荐热点 Tag】
  loadPopTag: function() {
    var that = this;
    wx.request({
      url: reqUrl + 'conference/push/queryPopTag.do?',
      data: {
        startTime: '2017-11-1',
        offset: '10',
        top: '10'
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        var resData = res.data; // 存了popTag数组
        // console.log(res.data);
        for (var i=0;i<resData.length;i++) {
          that.loadNumOfConferenceByTag(resData[i]);  // 获取各个标签及其所对应的会议数量
        }
      }
    });
  },

  // 请求【根据热门Tag查询会议含有Tag的会议数量】
  loadNumOfConferenceByTag: function (tagName) {
    var that = this;
    wx.request({
      url: reqUrl + 'conference/push/queryNumOfConferenceByTag.do?',
      data: {
        tag: tagName,
        startTime: '2017-11-1',
        offsetTime: '10'
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        var tempPopTag = {
          tag: '',
          count: 0
        };
        tempPopTag.tag = tagName;
        tempPopTag.count = res.data.count;
        var tempPopTagArray = that.data.popTag;
        tempPopTagArray.push(tempPopTag);
        that.tagToString();  // 把popTag数组变成字符串
        that.setData({
          popTag: tempPopTagArray
        });
      }
    }); 
  },

  // 把popTag数组变成字符串，用来页面间传值
  tagToString: function() {
    var that = this;
    that.setData({
      popTagString: JSON.stringify(that.data.popTag)
    });
    //console.log(that.data.popTagString);
  }

})