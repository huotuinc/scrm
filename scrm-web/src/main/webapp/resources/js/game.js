/**
 * Created by Neo on 2017/6/1.
 */
$(function () {
    FastClick.attach(document.body);

    var game = {
        thanksDeg: null,
        speed: 1300,
        timeout: false,
        running: false,
        hasNoTimes: false,
        gameTimes: parseInt($('#J_RemainTimes').val()),
        circleEle: $("#J_circle"),
        startBtn: $('#J_start'),
        prizeModal: $('#J_prizeModal'),
        errorModal: $('#J_errorModal'),
        loadModal: $('#J_loadingModal'),
        init: function () {
            this.reset();
            this.preEvents();
            this.renderElement();
            this.initModals();
            this.switchInfo();
        },
        preEvents: function () {
            game.startBtn.click(function () {
                if (game.running) return '';
                if (game.gameTimes) {
                    game.start();
                    $('#J_guide').hide();
                } else {
                    $('#J_recommend').show();
                    game.toggleFilter();
                }
            });
        },
        getOrder: function () {

            var actId =  $("body").attr("activeId");
            $.ajax({
                type: 'POST',
                url: '/site/join/act',
                data: {actId:actId},
                dataType: 'json',
                success: function (res) {
                    if (res.resultCode !== 2000) {
                        game.errorModals(res.resultMsg);
                        return;
                    }
                    if (res.data.joinid <= 0) {
                        game.errorModals(res.data.desc);
                        return;
                    }
                    game.gameTimes--;
                    game.rotateFn(res.data);
                },
                error: function (xhr, type) {
                    game.errorModals('发生错误，请稍后重试');

                }
            });
        },
        reset: function () { // 2017.08.02
            game.toggleLight();
            var i = $(".circle-box").find("[data-type='0']").data("index");
            game.thanksDeg = 60 * (5 - i) + 30;
            game.circleEle.rotate(game.thanksDeg);
            game.circleEle.rotate({
                angle: game.thanksDeg,
                animateTo: 3600,
                duration: 3e6,
                easing: function (t, e, i, r, o) {
                    return -r * ((e = e / o - 1) * e * e * e - 1) + i
                }
            })
        },
        rotateStart: function () {
            game.timeout = false;
            game.running = true;
            game.startBtn.addClass("disabled");
            game.circleEle.stopRotate();
            game.circleEle.rotate({
                angle: game.getRotateAngle(),
                animateTo: 7200,
                duration: 20 * game.speed,
                easing: function (t, e, i, r, o) {
                    return e
                },
                callback: function () {
                    game.timeout = true;
                }
            })
        },
        rotateFn: function (data) { // 2017.08.02
            var r;
            $("#J_circle").find('.prize').each(function () {
                //匹配awardId
                if ($(this).data("id") === +data.awardId) {
                    return void (r = 60 * (5 - $(this).data("index")) + 30)
                }
            });
            setTimeout(function () {
                game.circleEle.rotate({
                    angle: game.getRotateAngle(),
                    animateTo: r + 360,
                    duration: game.speed,
                    easing: function (t, e, i, r, o) {
                        return -r * ((e = e / o - 1) * e * e * e - 1) + i
                    },
                    callback: function () {
                        game.showModal(data);
                        game.renderElement();
                        game.running = false;
                    }
                });
            }, 500)
        },
        start: function () {
            if (game.running) return '';
            this.doStart();
        },
        doStart: function () {
            this.rotateStart();
            this.getOrder();
        },
        toggleLight: function () {
            window.lightTimer = setTimeout(function () {
                $('.radius-group').toggleClass('toggle');
                game.toggleLight();
            }, 500)
        },
        showModal: function (data) {
            var ele = game.createGoodsModal(data);
            $('body').append(ele);
            $('.J_modalShowPrize').show();
            game.toggleFilter();
        },
        hideModal: function () {
            $(document).on('click', '.coupon-modal-close', function () {
                $('.J_modalShowPrize').remove();
                game.reInit();
                game.toggleFilter();
            });
        },
        reInit: function () {
            this.running = false;
            clearTimeout(window.lightTimer);
            game.circleEle.rotate(game.thanksDeg);
            game.startBtn.removeClass("disabled");
            this.reset();
        },
        getRotateAngle: function () {
            return game.circleEle.getRotateAngle() % 60
        },
        renderElement: function () {
            if (game.gameTimes > 0) {
                $(".game-time span").html(game.gameTimes);
            } else {
                $(".game-time p").html('抽奖机会已用完');
            }
        },
        createGoodsModal: function (data) {
            return '<div class="J_modalShowPrize coupon-modal-showPrize">' +
                '<span class="close coupon-modal-close"></span>' +
                '<div class="coupon-modal-showPrize-dialog">' +
                '<div class="modal-header"></div>' +
                '<div class="modal-body withoutCode">' +
                '<p class="modal-title">' + data.orderTitle + '</p>' +
                '<div class="coupon-imageBg">' +
                '<div class="coupon-image">' +
                '<a href="#"><img src="' + data.orderImg + '"></a>' +
                '</div>' +
                '</div>' +
                '<a href="#" class="coupon-use">' + data.orderTitle + '</a>' +
                '</div>' +
                '<i class="ribbon"></i>' +
                '</div>' +
                '</div>';
        },
        activityClose: function () {
            $(document).on('click', '#J_recommend .close', function () {
                $('#J_recommend').hide();
                game.toggleFilter();
            });
        },
        showPrizeModal: function () {
            $('.prize-packet').click(function () {
                var level = $(this).data('level');
                $('.prize-detail-content.on').removeClass('on');
                $('.prize-detail-content[data-level="'+level+'"]').addClass('on');
                game.prizeModal.addClass('show');
                game.toggleFilter();
            });
        },
        closePrizeModal: function () {
            $('#J_slideUp').click(function () {
                game.prizeModal.removeClass('show');
                game.toggleFilter();
            });
        },
        showRuleModal: function () {
            $('#J_ruleBtn').click(function () {
                $('#J_ruleModal').show();
                game.toggleFilter();
            });
        },
        closeRuleModal: function () {
            $('#J_ruleModal').find('.close').click(function () {
                $('#J_ruleModal').hide();
                game.toggleFilter();
            });
        },
        toggleFilter: function () {
            $('#app').toggleClass('filter');
        },
        switchInfo: function () {
            $('#J_ruleModal').find('.nav').click(function () {
                $(this).parent().toggleClass('active');
            });
        },
        errorModals: function (text) {
            $('.error-title').html(text);
            game.errorModal.show();
            game.toggleFilter();
        },
        closeErrorModal: function () {
            $('.js-errorClose').click(function () {
                game.errorModal.hide();
                game.toggleFilter();
                game.reInit();
            });
        },
        loadingModal: function () {
            game.loadModal.show();
        },
        closeLoadModal: function () {
            game.loadModal.hide();
        },
        initModals: function () {
            this.hideModal();
            this.activityClose();
            this.showPrizeModal();
            this.closePrizeModal();
            this.showRuleModal();
            this.closeRuleModal();
            this.closeErrorModal();
        }
    };
    game.init();
    //等待图片加载完毕
    imagesLoaded(document.querySelector('body'), function () {
        $('.pre-loader').hide();
    });
});