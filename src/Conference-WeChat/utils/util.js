function formatTime(date) {
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()

  var hour = date.getHours()
  var minute = date.getMinutes()
  var second = date.getSeconds()


  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

function formatNumber(n) {
  n = n.toString()
  return n[1] ? n : '0' + n
}

function conferences(key,isSearch) {
    var conferences = [{
      cnName:'第11届可证明安全国际会议',
      enName: 'ProvSec 2017',
      tags: '计算机科学理论,计算机应用技术,信息安全',
      icon: 'computer.png',
      location: '中国陕西省西安市',
      sponsor: '陕西师范大学',
      startDate: '2017-10-23',
      endDate: '2017-10-25',
      deadline: '2017-06-03',
      acceptance: '2017-08-03',
      website: 'http://it.snnu.edu.cn/',
      favourites: 233,
      likes: 234
    },{
      cnName:'2017亚洲石墨烯论坛',
      enName: 'Asian Graphene Forum (AGF)',
      tags: '材料科学基础学科,无机非金属材料,复合材料',
      icon: 'architecture.png',
      location: 'Princess Cruise，Singapore',
      sponsor: 'International Association of Advanced Materials (IAAM)',
      startDate: '2017-03-11',
      endDate: '2017-03-16',
      deadline: '2016-08-22',
      acceptance: '2017-08-03',
      website: 'http://www.vbripress.org/asamc/agf/',
      favourites: 233,
      likes: 234
    },{
      cnName:'第十届农业研究国际年会',
      enName: '10th Annual International Symposium on Agricultural Research',
      tags: '人工智能,计算机应用技术,信息安全',
      icon: 'agricultural.png',
      location: 'Athens,Greece',
      sponsor: 'The Agriculture Research Unit of the Athens Institute for Education and Research (ATINER)',
      startDate: '2017-07-10',
      endDate: '2017-07-13',
      deadline: '2017-06-03',
      acceptance: '2017-08-03',
      website: 'https://www.atiner.gr',
      favourites: 233,
      likes: 234
    },{
      cnName:'第11届可证明安全国际会议',
      enName: 'ProvSec 2017 11',
      tags: '计算机科学理论,深度学习,信息安全',
      icon: 'medicine.png',
      location: '中国陕西省西安市',
      sponsor: '陕西师范大学',
      startDate: '2017-10-23',
      endDate: '2017-10-25',
      deadline: '2017-06-03',
      acceptance: '2017-08-03',
      website: 'http://it.snnu.edu.cn/'
    },{
      cnName:'2017亚洲石墨烯论坛',
      enName: 'Asian Graphene Forum (AGF) 11',
      tags: '材料科学基础学科,无机非金属材料,复合材料',
      icon: 'mathematics.png',
      location: 'Princess Cruise，Singapore',
      sponsor: 'International Association of Advanced Materials (IAAM)',
      startDate: '2017-03-11',
      endDate: '2017-03-16',
      deadline: '2016-08-22',
      acceptance: '2017-08-03',
      website: 'http://www.vbripress.org/asamc/agf/'
    },{
      cnName:'第十届农业研究国际年会',
      enName: '10th Annual International Symposium on Agricultural Research 22',
      tags: '中国计算机学会,计算机应用技术,信息安全',
      icon: 'chemistry.png',
      location: 'Athens,Greece',
      sponsor: 'The Agriculture Research Unit of the Athens Institute for Education and Research (ATINER)',
      startDate: '2017-07-10',
      endDate: '2017-07-13',
      deadline: '2017-06-03',
      acceptance: '2017-08-03',
      website: 'https://www.atiner.gr'
    }]
    if (key) {
      var lessConfs = [];
      for(var i=0;i<conferences.length;i++){
        var conf = conferences[i]
        if (!isSearch) {
          if (conf.enName==key) {
            return conf
          }
        } else if (conf.cnName.indexOf(key)>-1 || conf.tags.indexOf(key)>-1 || conf.enName.indexOf(key)>-1) { 
          // is search
          lessConfs[lessConfs.length]=conf;
        }
      }
      if (isSearch) {
        return lessConfs
      }
    } else {
      return conferences
    }
}

function tags() {
  var tags = [{
    text: '人工智能',
    index: 6301
  }, {
    text: '肿瘤医学',
    index: 5112
  }, {
    text: '深度学习',
    index: 2301
  }, {
    text: '生物制药',
    index: 1200
  }, {
    text: '生物识别',
    index: 500
  }, {
    text: '自动驾驶',
    index: 300
  }, {
    text: '农业信息化',
    index: 112
  }, {
    text: '自动化种植',
    index: 50
  }];
  return tags
}

module.exports = {
  formatTime: formatTime,
  conferences : conferences,
  tags: tags
}
