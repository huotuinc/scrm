/**
 * Created by Administrator on 2017/7/11.
 */




$(function () {

    'use strict';

    var updateurl = "/mall/updateBusinessCardInfo?customerId=2";
    var uploadImageFileUrl;
    var URL;
    var $simage=$("#simage");

    //var $uploadavator= $("#uploadavator");
    var $avform = $("#avform");
    $avform.on('submit', uploadAvator());
    //$uploadavator.click(uploadAvator);
    //console.log("aaaa");

    URL = window.URL || window.webkitURL;

    // var options = {
    //     aspectRatio: 16 / 9,
    //     preview: '.img-preview',
    //     crop: function (e) {
    //         // $dataX.val(Math.round(e.x));
    //         // $dataY.val(Math.round(e.y));
    //         // $dataHeight.val(Math.round(e.height));
    //         // $dataWidth.val(Math.round(e.width));
    //         // $dataRotate.val(e.rotate);
    //         // $dataScaleX.val(e.scaleX);
    //         // $dataScaleY.val(e.scaleY);
    //         // console.log(Math.round(e.x));
    //         // console.log(Math.round(e.y));
    //         // console.log(Math.round(e.height));
    //         // console.log(Math.round(e.width));
    //         // console.log(e.rotate);
    //         // console.log(e.scaleX);
    //         // console.log(e.scaleY);
    //     }
    // };

    var $uploadimage = $("#btnFile");

    $uploadimage.change(uploadimage);



    //initCropper();


    function initCropper() {

        //alert("init ");
        // Cropper
        $simage.on({
            ready: function (e) {
                console.log(e.type);
            },
            cropstart: function (e) {
                console.log(e.type, e.action);
            },
            cropmove: function (e) {
                console.log(e.type, e.action);
            },
            cropend: function (e) {
                console.log(e.type, e.action);
            },
            crop: function (e) {
                console.log(e.type, e.x, e.y, e.width, e.height, e.rotate, e.scaleX, e.scaleY);
            },
            zoom: function (e) {
                console.log(e.type, e.ratio);
            }
        }).cropper(options);

    }

    function uploadimage() {


        // if (!$simage.data('cropper')) {
        //     console.log("has data");
        //     return;
        // }

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


    var $updatecompanyname = $("#updatecompanyname");
    $updatecompanyname.click(updateBusinessCard);
    var $updateqq=$("#updateqq");
    $updateqq.click(updateBusinessCard);
    var $updatetel = $("#updatetel");
    $updatetel.click(updateBusinessCard);

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

});