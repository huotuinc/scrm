/**
 * Created by Administrator on 2017/7/11.
 */

$(function () {
    'use strict';

    var updateurl = "/site/updateBusinessCardInfo";
    //var uploadImageFileUrl;
    //var URL;
    var $customerId = $("#customerId");
    var $userId = $("#userId");

    var $avatarForm = $("#avatarForm");
    //$avatarForm.on('submit', uploadAvator);

    //var $weuishow_avatar = $("#weuishow_avatar");
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

    //URL = window.URL || window.webkitURL;

    var $btnFile = $("#btnInput");
    $btnFile.change(uploadImage);


    // var $uploadimage = $("#btnFile");
    //
    // $uploadimage.change(uploadimage);


    function uploadImage() {

        var files = this.files;
        var file;
        if (files && files.length) {
            file = files[0];
        }else{
            return false;
        }

        console.log("sssbbbbbs");

        //$("#avatarForm").submit();

        uploadAvator();

        //return false;
        //$avatarForm.submit();

        // if (uploadImageFileUrl) {
        //     URL.revokeObjectURL(uploadImageFileUrl);
        // }

        // uploadImageFileUrl = URL.createObjectURL(file);

        //alert(uploadImageFileUrl);

        //var $image = $("#simage");

        //$image.attr("src", uploadImageFileUrl);

        //$simage.cropper('destroy').attr('src', uploadImageFileUrl).cropper(options);

        //$simage.attr("src", uploadImageFileUrl);

        //console.log("set image");

        //$simage.cropper("replace", uploadImageFileUrl);

        //initCropper();

    }

    function uploadAvator() {
        //alert("upload");

        //var data = $simage.cropper("getCroppedCanvas").toDataURL();

        console.log( "1111");
        var $uploadimage = $("#btnInput");
        if( !$uploadimage.val()  ) return false;

        //alert("upload222");
        console.log( $uploadimage.val() );


        //var $form = $("#avform");
        var url = $avatarForm.attr("action");

        console.log("url="+ url);

        var data = new FormData( $avatarForm[0] );

        console.log(data);

        $.ajax(url,{
            method:"post",
            data:data,
            dataType:'json',
            processData: false,
            contentType: false,
            success:function (data) {
                console.log(data);

                var $img_avatar= $("#img_avatar");

                console.log("src=" + data.data.avatar);

                $img_avatar.attr("src" , data.data.avatar);

            },
            error:function (error) {
                console.log(error);
            }
        });

    }


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
                ajaxRequest( datatype , text );
                $ele.text(text);
            },
            onCancel: function () {
                console.log("取消了");
            },
            input: text
        });

    }


    function ajaxRequest(  updatetype , text ) {
        var url = "/site/updateBusinessCardInfo";
        var customerId = $customerId.val();
        var userid = $userId.val();
        var data ={ customerId : customerId , userId:userid , type: updatetype , value:text};

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