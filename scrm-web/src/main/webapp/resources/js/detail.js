/**
 * Created by Neo on 2017/6/5.
 */
$(function () {
    function endTime(min, max) {
        var date = Math.floor(new Date / 1e3);
        return (date + Math.floor(3600 * Math.random() * (max - min) + 3600 * min)) * 1000;
    }

    function formatMillisec() {
        return parseInt(10 + (99 - 10) * (Math.random()));
    }

    $('#J_countDown').countdown({
        date: endTime(1, 4),
        refresh: 100,
        render: function (date) {
            $(this.el).html('距结束 <span>' + this.leadingZeros(date.hours) + '</span>:<span>' + this.leadingZeros(date.min) + '</span>:<span>' + this.leadingZeros(date.sec) + '</span>:<span>' + formatMillisec() + '</span>');
        }
    });

    function randomWidth() {
        return parseInt(60 + (92 - 60) * (Math.random()));
    }

    var rW = randomWidth();
    $('#J_spanRob').html('已抢' + rW + '%');
    $(".progress-bg").css("width", rW + "%");
});