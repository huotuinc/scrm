/**
 * Created by Neo on 2017/6/2.
 */
var Bubble = function () {
    this.nameArr = ["李", "王", "张", "刘", "陈", "杨", "赵", "黄", "周", "吴", "徐", "孙", "胡", "朱", "高", "林", "何", "郭", "马", "罗", "梁", "宋", "郑", "谢", "韩", "唐", "冯", "于", "董", "萧", "程", "曹", "袁", "邓", "许", "傅", "沈", "曾", "彭", "吕", "苏", "卢", "蒋", "蔡", "贾", "丁", "魏", "薛", "叶", "阎", "余", "潘", "杜", "戴", "夏", "钟", "汪", "田", "任", "姜", "范", "方", "石", "姚", "谭", "廖", "邹", "熊", "金", "陆", "郝", "孔", "白", "崔", "康", "毛", "邱", "秦", "江", "史", "顾", "侯", "邵", "孟", "龙", "万", "段", "漕", "钱", "汤", "尹", "黎", "易", "常", "武", "乔", "贺", "赖", "龚", "文"];
    this.telHeadArr = ["134", "135", "136", "137", "138", "139", "130", "131", "132", "133", "150", "153", "156", "180", "181", "187", "188", "189"];
};
Bubble.prototype.getOrder = function () {
    return "" + this.randName() + this.randTel() + this.randTime() + "订购了该商品";

};

Bubble.prototype.randName = function () {
    return this.nameArr[Math.floor(Math.random() * this.nameArr.length)] + "**"
};

Bubble.prototype.randTel = function () {
    for (var e = this.telHeadArr[Math.floor(Math.random() * this.telHeadArr.length)], t = "", n = 0; n < 4; n++)
        t += Math.floor(10 * Math.random());
    return "（" + e + "****" + t + "）"
};

Bubble.prototype.randTime = function () {
    var e = Math.floor(29 * Math.random()) + 1;
    return "在" + e + "秒之前"
};

$.fn.bubble = function (options) {
    var self = this;
    var op = {
        maxBubble: 2
    };

    var setting = $.extend({}, op, options);

    var Bubble = function (obj) {
        this.maxBubble = obj.maxBubble || 2;
        this.bufferQueen = [];
        this.bubbleQueen = [];
    };


    function itemSet() {
        self.find(".bubble-item:first-child").addClass("leave");
        self.find(".bubble-item:nth-child(2)").addClass("hide")
    }

    function setHeight() {
        self.css({
            height: self.find(".bubble-list").height()
        })
    }

    Bubble.prototype.init = function () {
        self.find(".bubble-list").append('<li class="bubble-item show hide leave"><span></span></li>');
        self.find(".bubble-list").append('<li class="bubble-item show hide"><span></span></li>');
        this.start()
    };

    Bubble.prototype.addBuffer = function (e) {
        this.bufferQueen.push(e);
        1 === this.bufferQueen.length && 0 === this.bubbleQueen.length && this.start()
    };
    Bubble.prototype.start = function () {
        var that = this;
        if (this.bufferQueen.length > 0) {
            setTimeout(function () {
                var t = that.bufferQueen[0];
                that.bubbleQueen.push(t);
                self.find(".bubble-list").removeClass("scroll");
                if (that.bubbleQueen.length > that.maxBubble) {
                    self.find(".bubble-item:first-child").remove();
                }

                setHeight();
                that.moveRight();
                that.bufferQueen.shift();

                if (that.bubbleQueen.length > that.maxBubble) {
                    self.find(".bubble-list").addClass("scroll");
                    itemSet();
                    that.bubbleQueen.shift();
                }
                that.start();
            }, 1500)
        } else {
            if (this.bubbleQueen.length > 0) {

                setTimeout(function () {
                    self.find(".bubble-list").removeClass("scroll");
                    self.find(".bubble-item:first-child").remove();
                    itemSet();
                    that.bubbleQueen.shift();
                    that.start();
                }, 1500)
            }
        }
    };

    Bubble.prototype.moveRight = function () {
        var div = '<li class="bubble-item"><span>' + this.bufferQueen[0] + '</span></li>';
        self.find(".bubble-list").append(div);
        setTimeout(function () {
            self.find(".bubble-item:last-child").addClass("show")
        }, 100)
    };

    var bubble = new Bubble(setting);
    bubble.init();
    window.BUBBLE = bubble;
    return this;
};

$('#J_Bubble').bubble();

var b = new Bubble();
function showBubble() {
    BUBBLE.addBuffer(b.getOrder());
    var t = Math.floor(4500 * Math.random());
    setTimeout(showBubble, t)
}

showBubble();