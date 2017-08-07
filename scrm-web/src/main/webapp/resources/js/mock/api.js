/**
 * Created by Neo on 2017/5/10.
 */
// 模拟延迟
Mock.setup({
    timeout: '1000'
});

Mock.mock("/site/join/act", "post", {
    "code": 200,
    "resultMsg": "ok",
    "data": {
        "prizeType": {
            "code": 0
        },
        "prizeId": 2,
        "prizeName": "超级超级超级便宜的东西",
        "prizeImageUrl": "//yun.duiba.com.cn/tuia/img/mwd6ugaxbo.jpg"
    }
});

Mock.mock(/\/api\/authCode/, "post", {
    "resultCode": 200,
    "resultMsg": "ok",
    "data": null
});

