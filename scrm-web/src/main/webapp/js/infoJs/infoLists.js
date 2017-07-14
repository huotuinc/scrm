/**
 * Created by luohaibo on 2017/7/12.
 */



var infoListHandler = {
    search:function() {
        var search = $("input:text[name='infoName']").val();
        $('#searchForm').submit();
    },
    searchAll:function(){
        window.location.href = "/mall/infoLists";
    }
}

$('.js-search').click(function () {
    infoListHandler.search();
});


$('.js-searchAll').click(function(){
    infoListHandler.searchAll();
});

