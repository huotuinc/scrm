/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2017. All rights reserved.
 */

/**
 * Created by montage on 2017/7/17.
 */

$(function () {
    hot.ajax(ajaxUrl,null,function (res) {
        if (res.code == 2000){
        }
    },function (){},"post");

    //参与记录
    $('i[class~=fa-paste]').click(function () {
        var activity =  $(this).closest('div[class*=panel-body]');
        var customerId = activity.find("td[id=act]");
       $.ajax({
           type: 'POST',
           url: '/act/win/list',
           data: { customerId: customerId},
           dataType: 'json',
           success:function (res) {
           }
       });
    });

    //奖品设置
   $('i[class~=fa-cog]').click(function () {
       $.ajax({
           type: 'POST',
           url: '/prize/list',
           data: null,
           dataType: 'json',
           success:function (res) {
           }
       });
    });

   //编辑
    $('i[class~=fa-edit]').click(function () {
        var activity =  $(this).closest('div[class*=panel-body]');
        var actId = activity.find("td[class~=js-actId]");
        $.ajax({
            type: 'POST',
            url: '/act/list/save',
            data: {actId:actId},
            dataType: 'json',
            success:function (res) {
                return;
            }
        });
    });

    //删除
    $('i[class~=fa-remove]').click(function () {
        var activity =  $(this).closest('div[class*=panel-body]');
        var actId = activity.find("td[class~=js-actId]");
        $.ajax({
            type: 'POST',
            url: '/act/list/delete',
            data: {actId:actId},
            dataType: 'json',
            success:function (res) {
                if (res.code == 2000){
                    window.location.reload();
                    return;
                }
            }
        });
    });

});