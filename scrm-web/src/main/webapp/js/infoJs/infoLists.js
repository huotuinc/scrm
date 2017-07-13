/**
 * Created by luohaibo on 2017/7/12.
 */

$(function(){

    var url = $("body").attr("data-url-infoLists");
    $.ajax({
        type: 'GET',
        url: url,
        data: {
            page:0
        },
        dataType: 'json',
        success: function (data) {

            console.log(data);

        },
        error: function (jqXHR, textStatus) {

        }
    });

});