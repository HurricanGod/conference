//index.js
var util = require('../../utils/util.js')
var base64 = require("../example/images/base64");
//获取应用实例
var app = getApp()

//请求数据的主域名
var reqUrl = app.globalData.reqUrl

Page({
  data: {
    motto: '学术会议与学术活动',
    userInfo: {},
    windowHeight: 0,
    windowWidth : 0,
    inputShowed: false,
    inputVal: "",
    showMore: false,
    isLower: false,
    isEnd: false,
    conferences: [],
    conferenceString: ""
  },
  onLoad: function (options) {
    //console.log(options.type)
    var that = this
    this.setData({
        icon20: base64.icon20,
        icon60: base64.icon60
    });
    // 调用应用实例的方法获取全局数据
    app.getUserInfo(function(userInfo){
      // 更新数据
      that.setData({
        userInfo:userInfo
      })
      // 设置窗口大小
      wx.getSystemInfo( {
        success: ( res ) => {
          that.setData( {
            windowHeight: res.windowHeight-5,
            windowWidth: res.windowWidth
          })
          //console.dir(that.data.windowHeight)
          // 加载数据
          that.loadConferences()
        }
      })
      
    })
  },
  showInput: function () {
    this.setData({
        inputShowed: true
    });
  },
  hideInput: function () {
    this.setData({
        inputVal: "",
        inputShowed: false
    });
  },
  clearInput: function () {
    this.setData({
        inputVal: "",
        searchResults: []
    });
  },
  inputTyping: function (e) {
    var conferences = util.conferences(e.detail.value,1)
    this.setData({
        inputVal: e.detail.value,
        searchResults: conferences
    });
  },

  onUpper : function() {},
  onLower : function() {
    var that = this
    that.setData({
      isLower: true
    });
    // load more data
    that.loadMoreConferences()
  },
  onScroll : function() {},

  // 首页加载会议
  loadConferences : function() {
    var that = this;
    wx.request({
      url: reqUrl + "conference/conference/latest.do?",
      data: {
        days: '7',
        page: '1'
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        var resData = res.data;
        //console.log(resData);
        // 由于请求回来的数据中含有 "&", "?", "=", 这样navigatorTo的url会解析错误，所以要把 & 换成 and, 把 ? 换成 questionMark，把 = 换成 equalMark
        var resDataStr = JSON.stringify(resData).replace(/\&/g, "andMark").replace(/\?/g, "questionMark").replace(/\=/g, "equalMark");
        // console.log(resDataStr)
        that.setData({
          //conferences: util.conferences()
          conferences: resData,
          conferenceString: resDataStr
        })
      }
    });    
  },

  // 加载更多
  currentPage: 1, // current page
  loadMoreConferences : function() {
    var that = this
    var page = ++that.currentPage
    //var moreConferences = util.conferences('',page)
    wx.request({
      url: reqUrl + "conference/conference/latest.do?",
      data: {
        days: '7',
        page: page
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        var resData = res.data;
        var addNewResData = that.data.conferences.concat(resData);
        // 由于请求回来的数据中含有 "&", "?", "=", 这样navigatorTo的url会解析错误，所以要把 & 换成 and, 把 ? 换成 questionMark，把 = 换成 equalMark
        var addNewResDataStr = JSON.stringify(addNewResData).replace(/\&/g, "andMark").replace(/\?/g, "questionMark").replace(/\=/g, "equalMark");
        //console.log(addNewResDataStr)
        that.setData({
          isLower: false,
          isEnd: res.data.length == 0,
          conferences: addNewResData,
          conferenceString: addNewResDataStr
        }) 
      }      
    });
    // that.setData({
    //   isLower: false,
    //   isEnd: moreConferences.length==0,
    //   conferences: that.data.conferences.concat(moreConferences)
    // })    
    console.dir(page)  //当前页数
  }
})
