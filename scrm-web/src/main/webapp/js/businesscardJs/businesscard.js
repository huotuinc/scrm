/**
 * Created by Administrator on 2017/7/11.
 */

$(function () {
    'use strict';

    var updateurl = "/mall/updateBusinessCardInfo?customerId=2";
    var uploadImageFileUrl;
    var URL;
    var $simage=$("#simage");

    //var $avform = $("#avform");
    //$avform.on('submit', uploadAvator());

    var $weuishow_avatar = $("#weuishow_avatar");
    var $weuishow_companyname=$("#weuishow_companyname");
    var $weuishow_qq=$("#weuishow_qq");
    var $weuishow_tel=$("#weuishow_tel");
    var $weuishow_email=$("#weuishow_email");
    var $weuishow_companyaddress=$("#weuishow_companyaddress");
    var $weuishow_job=$("#weuishow_job");

    //$weuishow_avatar.click( showTip );
    $weuishow_companyaddress.click(showTip);
    $weuishow_companyname.click(showTip);
    $weuishow_job.click(showTip);
    $weuishow_email.click(showTip);
    $weuishow_qq.click(showTip);
    $weuishow_tel.click(showTip);

    URL = window.URL || window.webkitURL;


    // var $uploadimage = $("#btnFile");
    //
    // $uploadimage.change(uploadimage);


    function uploadimage() {


        var files = this.files;
        var file;
        if (files && files.length) {
            file = files[0];
        }

        if (uploadImageFileUrl) {
            URL.revokeObjectURL(uploadImageFileUrl);
        }

        uploadImageFileUrl = URL.createObjectURL(file);

        //alert(uploadImageFileUrl);

        //var $image = $("#simage");

        //$image.attr("src", uploadImageFileUrl);

        //$simage.cropper('destroy').attr('src', uploadImageFileUrl).cropper(options);

        $simage.attr("src", uploadImageFileUrl);

        console.log("set image");

        //$simage.cropper("replace", uploadImageFileUrl);

        //initCropper();


    }

    function uploadAvator() {
        //alert("upload");

        //var data = $simage.cropper("getCroppedCanvas").toDataURL();

        //console.log( data);
        var $uploadimage = $("#btnFile");
        if( !$uploadimage.val()  ) return false;

        alert("upload222");


        var $form = $("#avform");
        var url = $form.attr("action");

        var data = new FormData( $form[0] );
        console.log(data);

        $.ajax(url,{
            type:"post",
            data:data,
            dataType:'json',
            success:function (data) {
                console.log(data);
            },
            error:function (error) {
                console.log(error);
            }
        });

    }


    // var $updatecompanyname = $("#updatecompanyname");
    // $updatecompanyname.click(updateBusinessCard);
    // var $updateqq=$("#updateqq");
    // $updateqq.click(updateBusinessCard);
    // var $updatetel = $("#updatetel");
    // $updatetel.click(updateBusinessCard);

    function updateBusinessCard() {
        var type= $(this).attr("js-datatype");
        var value = $("#companyname").val();

        alert(type);
        var data = {type:type,value:value};
        $.ajax(updateurl,{
         type:'Post',
         dataType:"json",
         data: data,
         success:function (data) {
             console.log(data);
         },
         error:function (error) {
            console.log(error);
         }
        })

    }

    function showTip() {
        var userid = 99999999;
        var datatype= $(this).attr("js-data-type");
        var title = $(this).attr("js-data-hint");
        var $ele = $(this).find(".ui-data-ele");
        var text = $ele.text();
        console.log(text);

        $.prompt({
            text: "",
            title: title,
            onOK: function (text) {
                //$.alert("姓名:" + text, "填写成功");
                $ele.val(text);
                ajaxRequest(userid , datatype , text );
                $ele.text(text);
            },
            onCancel: function () {
                console.log("取消了");
            },
            input: text
        });

    }


    function ajaxRequest( userId , updatetype , text ) {
        var url = "/mall/updateBusinessCardInfo";
        var data ={userId:userId , type: updatetype , value:text};

        console.log("ssss");

        $.ajax({
           url: url,
            type:"post",
           dataType:"json",
            data: data,
            beforeSend:function () {

            },
            success:function (data) {

            },
            error:function (error) {
               console.log(error);
            },


        });
    }

});