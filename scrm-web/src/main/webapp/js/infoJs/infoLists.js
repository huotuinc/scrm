/**
 * Created by luohaibo on 2017/7/12.
 */


// var ajaxTool = function(hosturl, parame ,callBack){
//
//     $.ajax({
//         type: 'GET',
//         url: url,
//         data: {
//             page:0
//         },
//         dataType: 'json',
//         success: function (data) {
//             var amount = data.data.amount;
//
//
//             $("#infoListAmount").text("共"+amount+"条记录，当前第1/"+Math.ceil(amount/pageSize)+"，每页5条记录");
//             //初始化分页
//             var pageinate = new hot.paging(".pagination", pageIndex, (amount / pageSize), (amount / pageSize));
//
//             pageinate.init(function (p) {
//
//                 $("#infoListAmount").text("共"+amount+"条记录，当前第"+p+"/"+Math.ceil(amount/pageSize)+"，每页5条记录");
//             });
//
//         },
//         error: function (jqXHR, textStatus) {
//
//         }
//     });
//
// }



var infoListHandler = {


    search:function(){
        var condition = $('#infoName').text;

    },


    searchAll:function(){

        alert("1");
    },


}

$('.js-search').click(function () {
    infoListHandler.search();
});


$('.jjs-searchAll').click(function(){
    infoListHandler.searchAll();
});

