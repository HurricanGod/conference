// pages/index/popular.js
var util = require('../../utils/util.js')

var app = getApp();

//请求数据的主域名
var reqUrl = app.globalData.reqUrl;

Page({
  data: {
    tag: {},
    conferences: [],
    conferenceString: ""
  },
  onLoad:function(options){
    var that = this
    var tag  = options.tag;
    // 加载数据
    that.loadConferences(tag)
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

  loadConferences : function(tag) {
    // var that = this;
    // var results = util.conferences(tag,1)
    // that.setData({
    //   tag: {text: tag, index: results.length},
    //   conferences: results
    // })

    var that = this;
    wx.request({
      url: reqUrl + 'conference/push/queryConferenceByTag.do?',
      data: {
        tag: tag,
        startTime: '2017-11-1',
        offsetTime: '10'
      },
      success: function(res) {
        console.log(res.data)
        var resData = res.data;
        // 由于请求回来的数据中含有 "&", "?", "=", 这样navigatorTo的url会解析错误，所以要把 & 换成 and, 把 ? 换成 questionMark，把 = 换成 equalMark
        var resDataStr = JSON.stringify(resData).replace(/\&/g, "andMark").replace(/\?/g, "questionMark").replace(/\=/g, "equalMark");
        that.setData({
          tag: { text: tag, index: resData.length},
          conferences: resData,
          conferenceString: resDataStr
        })
      }
    })
  }
})