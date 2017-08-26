/**
 * Created by Jinxiangdong on 2017/7/11.
 */
$(function () {
    //'use strict';

    var $customerId = $("#customerId");
    var $userId = $("#userId");
    var $avatarForm = $("#avatarForm");
    var $weuishow_companyname=$("#weuishow_companyname");
    var $weuishow_qq=$("#weuishow_qq");
    var $weuishow_tel=$("#weuishow_tel");
    var $weuishow_email=$("#weuishow_email");
    var $weuishow_companyaddress=$("#weuishow_companyaddress");
    var $weuishow_job=$("#weuishow_job");
    var $js_avatar_div=$(".js-avatar-div");

    $weuishow_companyaddress.click(showTip);
    $weuishow_companyname.click(showTip);
    $weuishow_job.click(showTip);
    $weuishow_email.click(showTip);
    $weuishow_qq.click(showTip);
    $weuishow_tel.click(showTip);
    $js_avatar_div.click(callInputFile);

    var $btnFile = $("#btnInput");
    $btnFile.change(uploadImage);
    var layerIndex = 0;

    function uploadImage() {
        var files = this.files;
        //var file;
        if (files && files.length) {
            //file = files[0];
        }else{
            return false;
        }

        uploadAvatar();
    }

    /**
     * 点击头像触发选择图片事件
     */
    function callInputFile() {
        $btnFile.click();
    }

    function supportFormData() {
        return !! window.FormData;
    }

    function uploadAvatar() {

        var suppFormData = supportFormData();
        console.log("support FormData=" + suppFormData);

        //var $uploadimage = $("#btnInput");
        if( !$btnFile.val()  ) return false;

        //alert("upload222");
        console.log( $btnFile.val() );

        //var $form = $("#avform");
        var url = $avatarForm.attr("action");

        //test--------------
        //url = "/common/upload";

        console.log("url="+ url);

        var data = new FormData( $avatarForm[0] );

        console.log(data);
        console.log( JSON.stringify( data));

        $.ajax(url,{
            type:'post',
            data:data,
            dataType:'json',
            processData: false,
            contentType: false,
            beforeSend:function () {
                layerIndex = layer.load();
            },
            complete:function () {
                layer.close(layerIndex);
            },
            success:function (data) {
                console.log("data="+data);
                if( data.code == 200 ) {
                    var $img_avatar = $("#img_avatar");
                    var imgUrl = data.data.avatar + "?r=" + Math.random() * 10;
                    console.log("src=" + imgUrl);
                    $img_avatar.attr("src", imgUrl);
                }else{
                    layer.msg( data.msg );
                }
            },
            error:function (error) {
                console.log("error="+ JSON.stringify(error) );
                layer.msg(error.responseText);
            }
        });

    }

    function showTip() {
        var dataType= $(this).attr("js-data-type");
        var title = $(this).attr("js-data-hint");
        var $ele = $(this).find(".ui-data-ele");
        var maxlength = $(this).attr("js-data-maxlength");
        var text = $ele.text();
        //console.log(text);
        //console.log( maxlength);

        $.prompt({
            text: "",
            title: title+",长度不要超过"+maxlength,
            onOK: function (text) {
                if(text.length > maxlength){
                    layer.msg("您输入的内容长度太多了，请重新设置吧！");
                    return;
                }
                ajaxRequest( $ele , dataType , text );
            },
            onCancel: function () {
                console.log("取消了");
            },
            input: text
        });
    }

    function ajaxRequest( ele , updateType , text ) {
        var url = $('body').attr("js-data-update-url");
        var customerId = $customerId.val();
        //var userId = $userId.val();
        var data = {customerId: customerId, type: updateType, typeValue: text};

        console.log("url=" + url);
        console.log("data=" + JSON.stringify(data));

        $.ajax({
            url: url,
            type: 'POST',
            dataType: 'json',
            data: data,
            beforeSend: function () {
                layerIndex = layer.load();
            },
            complete: function () {
                layer.close(layerIndex);
            },
            success: function (data) {
                if (data.code == 200) {
                    ele.text(text);
                } else {
                    layer.msg(data.msg);
                }
            },
            error: function (error) {
                console.log(error);
                layer.msg(error.responseText);
            },
        });
    }

});