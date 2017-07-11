/**
 * Created by Neo on 2017/5/10.
 */
// 模拟延迟
Mock.setup({
    timeout: '1000'
});

Mock.mock(/\/activity\/dojoin/, "post", {
    "resultCode": 2000,
    "resultMsg": "ok",
    "data": {
        "awardtype": 0,
        "orderId": "6546789400763",
        "orderTitle": "超级超级超级便宜的东西",
        "orderImg": "//yun.duiba.com.cn/tuia/img/mwd6ugaxbo.jpg",
        "prizeId": "300",
        "prizeType": "lucky"
    }
});

