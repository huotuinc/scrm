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
            var actId = $("body").attr("activeId");
            var customerId = $("body").attr("customerId");
            game.loadingModal();
            $.ajax({
                type: 'POST',
                url: '/site/join/act',
                data: {
                    actId: actId,
                    customerId: customerId
                },
                dataType: 'json',
                success: function (res) {
                    game.closeLoadModal();
                    if (res.code !== 200) {
                        game.errorModals(res.msg);
                        return;
                    }
                    if (res.data.code <= 0) {
                        game.errorModals(res.data.value);
                        return;
                    }
                    game.gameTimes--;
                    game.rotateFn(res.data);
                    game.closeLoadModal();
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
        rotateFn: function (data) {

            console.log(data.prizeDetailId);
            // $('#J_userInfo').find('input:hidden[name="prizeDetail"]').val(data.prizeDetailId);
            // $("input:hidden[name='prizeDetail']").val(data.prizeDetailId);
            $("input[name='ActWinDetailId']").val(data.prizeDetailId);
            // var search = $("input:text[name='prizeDetail']").val(data.prizeDetailId);

            var r;
            $("#J_circle").find('.prize').each(function () {
                //匹配awardId
                if ($(this).data("id") === +data.prizeId) {
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
            // 动态渲染
            $(document).on('click', '.coupon-use, .coupon-modal-close', function () {
                $('.J_modalShowPrize').remove();
                game.reInit();
                game.toggleFilter();
                if ($(this).attr("data-type") != 0) {
                    game.showUserInfo();
                }

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
                $(".game-time p").html("游戏次数： " + game.gameTimes + "次");
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
                '<p class="modal-title">' + data.prizeName + '</p>' +
                '<div class="coupon-imageBg">' +
                '<div class="coupon-image">' +
                '<a href="#"><img src="' + data.prizeImageUrl + '"></a>' +
                '</div>' +
                '</div>' +
                '<a href="javascript:;" class="coupon-use" data-type="' + data.prizeType.code + '">' + "确定" + '</a>' +
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
                $('.prize-detail-content[data-level="' + level + '"]').addClass('on');
                game.prizeModal.addClass('show');
                game.toggleFilter();
            });
        },
        closePrizeModal: function () {
            $(document).on('click', '#J_slideUp', function () {
                game.prizeModal.removeClass('show');
                game.toggleFilter();
            })
        },
        showRuleModal: function () {
            $('#J_ruleBtn').click(function () {
                $('#J_ruleModal').show();
                game.toggleFilter();
            });
        },
        showUserInfo: function () {
            $('#J_addTel').show();
            game.toggleFilter();
        },
        closeNameAndTel: function () {
            $('#J_addTel').find('.close').click(function () {
                $('#J_addTel').hide();
                game.toggleFilter();
            })
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
            this.closeNameAndTel();
        }
    };
    game.init();
    //等待图片加载完毕
    imagesLoaded(document.querySelector('body'), function () {
        $('.pre-loader').hide();
    });

    $('#J_userFormBtn').click(function (e) {
        e.preventDefault();
        if(verifyForm()) {




        }
    });

    //短信接口，下面为mock，请用实际
    var sendAuthCodeUrl = '/sendAuthCode';

    $('#J_sendAuth').click(function () {
        var self = $(this);
        var mobile = $('#J_userInfo').find('input[name="mobile"]').val();
        var customerId = $("body").attr("customerId");
        if (!/^1([34578])\d{9}$/.test(mobile)) {
            showMsg('请输入正确的手机号');
            return;
        }
        sendSMS(self);
        $.ajax({
            url:sendAuthCodeUrl,
            type: 'POST',
            data: {
                loginName: mobile,
                customerId:customerId,
            },
            dataType: 'json',
            success: function (data) {
                if (data.resultCode === 400) {
                    showMsg(data.resultMsg);
                    return false;
                }
                if (data.resultCode !== 200) {
                    showMsg("发送失败，请重试");
                    return false;
                }
            },
            error: function () {
                showMsg("系统错误");
            }
        })
    });

    function verifyForm() {
        var form = $('#J_userInfo');
        var name = form.find('input[name="name"]').val();
        var mobile = form.find('input[name="mobile"]').val();
        var authCode = form.find('input[name="authCode"]').val();
        var deId  =  form.find('input[name="ActWinDetailId"]').val();
        if(!name) {
            showMsg('姓名不能为空');
            return false;
        }
        if(!(/^1([34578])\d{9}$/.test(mobile))) {
            showMsg('请填写正确的手机号');
            return false;
        }
        if(!authCode) {
            showMsg('验证码不能为空');
            return false;
        }

        $.ajax({
            url:"/site/update/winRecord",
            type: 'POST',
            data: {
                mobile: mobile,
                name: name,
                authCode:authCode,
                actWinDetailId:deId
            },
            dataType: 'json',
            success: function (data) {
                if (data.code != 200) {
                    showMsg(data.data);
                    return false;
                }
                showMsg(data.msg);
                setTimeout(function () {
                    $('#J_addTel').hide();
                    game.toggleFilter();
                },3000);
            },
            error: function () {
                showMsg("系统错误");
                return false;
            }
        })

    }
    var time;

    function showMsg(t) {
        clearTimeout(time);
        var modal = $('#J_msgModal');
        var p = modal.find('p');
        p.text(t);
        modal.addClass('show');
        time = setTimeout(function () {
            modal.removeClass('show');
        }, 2000);
    }

    function sendSMS(ele) {
        ele.prop('disabled', true)
            .addClass('disabled');
        var s = 60;
        var t = setInterval(function () {
            ele.text('重新发送'+ s-- + '秒');
            if (s === -1) {
                clearInterval(t);
                ele.text('获取验证码')
                    .prop('disabled', false)
                    .removeClass('disabled');
            }
        }, 1000);
    }

});