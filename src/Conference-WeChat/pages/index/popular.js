// pages/index/popular.js
var util = require('../../utils/util.js')
Page({
  data: {
    tag: {},
    conferences: []
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
    var that = this;
    var results = util.conferences(tag,1)
    that.setData({
      tag: {text: tag, index: results.length},
      conferences: results
    })
  }
})