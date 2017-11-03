/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

/**
 * Created by admin on 2017-07-28.
 */
<!--JSSDK分享接口BEGIN-->
var domain = [[${domain}]],customerId= [[${customerId}]],sourceUserId= [[${sourceUserId}]];
var info = eval([[${info}]]);
//过滤单引号和回车
info.title = info.title.replace('\'','');
info.title = info.title.replace(/\s/g,'');
info.introduce = info.introduce.replace('\'','');
info.introduce = info.introduce.replace(/\s/g,'');
document.write('<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>');
document.write('<script type="text/javascript" src="http://m' + domain + '/Weixin/JsSdk/RegConfig.aspx?customerid=' + customerId+ '&debug=0"></script>');
document.write('<script src="http://m' + domain + '/Weixin/JsSdk/wxShare.js?20150112"></script>');
var share_link = window.location.href;
if(share_link.indexOf("&sourceUserId") > -1){
    share_link = share_link.substr(0,share_link.indexOf("&sourceUserId"));
}
if(sourceUserId!=undefined && sourceUserId != 'null' && sourceUserId!='')
    share_link += ('&sourceUserId=' + sourceUserId);
var scrm_wxShare_template = [
    "wxShare.InitShare({",
    "    title: '" + info.title + "',",
    "    desc: '"+info.introduce + "',",
    "    link: '" + share_link + "',",//分享网址，一般在这里添加自己想要的参数
    "    img_url: '"+info.mallImageUrl + "'",
    "});"
].join("\n");
document.write("<script>" + scrm_wxShare_template + "</script>");



