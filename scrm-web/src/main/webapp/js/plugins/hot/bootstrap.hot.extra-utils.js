/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */
document.write('<script src="http://resali.huobanplus.com/cdn/layer/2.4/layer.js"></script>');

var modal = function () {
    this.layerIndex = null,
        this.layerOption = null,
        this.init = function (title, content, yesCallback, options) {
            var standardOption = {
                content: content,
                title: title,
                offset: 150,
                maxWidth: 720,
                shift: 0,
                //shade: 0,
                btn: ["确定", "关闭"],
                yes: yesCallback,
                cancel: function (index) {
                    return true;
                }
            };
            if (options) {
                standardOption = $.extend({}, standardOption, options);
            }
            this.layerOption = standardOption;
        };
    this.show = function (initCallback) {
        var index = layer.open(this.layerOption);
        if (initCallback != 'undefined' && typeof initCallback == 'function') {
            initCallback();
        }
        this.layerIndex = index;
    };
    this.hide = function (hideCallback) {
        if (this.layerIndex != null) {
            layer.close(this.layerIndex);
        }
        if (hideCallback != 'undefined' && typeof hideCallback == 'function') {
            hideCallback();
        }
    }
};

$.fn.extend({
    /**
     初始化一个modal
     **/
    modal: function (title, yes, options) {
        var $this = $(this);
        var content = $this.html();
        var index = new modal();
        index.init(title, content, yes, options);
        $this.empty();
        return index;
    },
    datePick: function (options) {
        var op = {
            format: "yyyy-mm-dd",
            autoclose: true,
            todayHighlight: true,
            todayBtn: 'linked',
            language: 'zh-CN'
        };
        if (options != 'undefined') {
            op = $.extend({}, op, options);
        }
        $(this).datepicker(op);
    }
});

var hot = $.extend({}, hot, {
    confirm: function (content, callback) {
        layer.confirm(content, {
            offset: 200
        }, function (index) {
            layer.close(index);
            callback(index);
        });
    },
    iframeModal: function (url, width, height, title, option) {
        var op = {
            type: 2,
            fix: true,
            area: [width, height],
            content: url,
            title: title
        };
        if (option) {
            op = $.extend({}, op, option);
        }
        layer.open(op);
    },
    iframeModalWithButton: function (url, width, height, title, yes, option) {
        var op = {
            type: 2,
            title: title,
            fix: true,
            area: [width, height],
            content: url,
            btn: ['确定', '关闭'],
            yes: yes,
            btn2: function (index, layero) {
                layer.close(index);
            }
        };
        if (option) {
            op = $.extend({}, op, option);
        }
        layer.open(op);
    },
    selectCallery: function (customerId, _mainDomain, end) {
        var calleryUrl = "/3rdParty/Widget/Picture/gallery.html?customerId=" + customerId + "&isMult=false&height=-1&domain=" + _mainDomain + "&t=1.2";

        hot.iframeModal(calleryUrl, "920px", "640px;", "图片库", {
            end: end
        });
    },
    newTab: function (url, name) {
        if (name.length > 25) {
            name = name.substring(0, 25) + "...";
        }
        parent.newTab(url, name);
    },
    tip: {
        success: function (content, callback, time) {
            var $msg = $('<div class="hottip-wrap"><div class="layui-layer layui-layer-dialog layui-layer-border layui-layer-msg layui-layer-hui layer-anim hottip-content" style="z-index: 99891018;top: 30%;position: relative;background-color: rgba(28, 175, 154, 0.8);"><div id="" class="layui-layer-content">' + content + '</div></div></div>');
            layer.closeAll("loading");
            $("body").append($msg);

            if (time == 'undefined') {
                time = 1500;
            }

            setTimeout(function () {
                $msg.remove();
                if (typeof callback == 'function')
                    callback();
            }, 1500);
        },
        error: function (content, callback, time) {
            if (time == 'undefined') {
                time = 3000;
            }

            layer.closeAll("loading");
            var $msg = $('<div class="hottip-wrap"><div class="layui-layer layui-layer-dialog layui-layer-border layui-layer-msg layui-layer-hui layer-anim hottip-content" style="z-index: 99891018;top: 30%;position: relative;background-color: rgba(217, 82, 79, 0.8);"><div id="" class="layui-layer-content">' + content + '</div></div></div>');
            $("body").append($msg);
            setTimeout(function () {
                $msg.remove();
                if (typeof callback == 'function')
                    callback();
            }, 3000);
        },
        msg: function (content, callback, time) {
            if (time == 'undefined') {
                time = 3000;
            }

            layer.closeAll("loading");
            var $msg = $('<div class="hottip-wrap"><div class="layui-layer layui-layer-dialog layui-layer-border layui-layer-msg layui-layer-hui layer-anim hottip-content" style="z-index: 99891018;top: 30%;position: relative;"><div id="" class="layui-layer-content">' + content + '</div></div></div>');
            $("body").append($msg);
            setTimeout(function () {
                $msg.remove();
                if (typeof callback == 'function')
                    callback();
            }, 3000);
        }
    },
    loading: {
        show: function () {
            return layer.load()
        },
        close: function () {
            layer.closeAll('loading');
        }
    },
    /**
     * bootstrap分页扩展
     * 将根据所传参数自动渲染输出bootstrap风格的分页按钮
     *
     * @param loadObj 待输出的dom元素
     * @param pageNo 页码,从1开始
     * @param totalPages 总页数
     * @param btnCount 需要显示的按钮数量,不包括上一页下一页首页和末页,奇数
     */
    paging: function (loadObj, pageNo, totalPages, btnCount) {
        this.currentBtnPage = 0;
        this.obj = $(loadObj);
        this.pageNo = pageNo;
        this.totalPages = totalPages;
        this.btnCount = btnCount;

        this.init = function (callback) {
            if (this.obj) {
                this.obj.html('');
                if (this.totalRecords == 0) {
                    this.obj.html('');
                    return;
                }
                if (this.pageNo >= totalPages) {
                    this.pageNo = totalPages;
                }
                if (this.pageNo <= 1) {
                    this.pageNo = 1;
                }

                if (this.pageNo > 1) {
                    //输出首页和上一页按钮
                    this.obj.append('<li ' + (this.pageNo == 1 ? 'class="disabled"' : '') + '><a href="javascript:goTo(1,' + callback + ')" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>');
                    this.obj.append('<li ' + (this.pageNo == 1 ? 'class="disabled"' : '') + '><a href="javascript:goTo(' + (pageNo - 1) + ',' + callback + ')"><i class="fa fa-angle-left"></i></a></li>');
                }

                var btnStart = parseInt(this.btnCount / 2);//从哪个索引开始变换
                this.btnCount = Math.min(totalPages, this.btnCount);

                if (this.pageNo > this.totalPages - btnStart) {
                    this.currentBtnPage = this.totalPages - this.btnCount + 1;
                } else {
                    this.currentBtnPage = Math.max(this.pageNo - btnStart, 1);
                }

                //输出中间八个按钮
                for (var i = this.currentBtnPage; i < this.btnCount + this.currentBtnPage; i++) {
                    this.obj.append('<li ' + (this.pageNo == i ? 'class="active"' : '') + '><a href="'+(this.pageNo==i?'#':'javascript:goTo('+i+','+callback+')')+'">' + i + '</a></li>');
                }

                //输出下一页和末页
                if (this.pageNo != totalPages) {
                    this.obj.append('<li ' + (pageNo == totalPages ? 'class="disabled"' : '') + '><a href="javascript:goTo(' + (pageNo + 1) + ',' + callback + ')"><i class="fa fa-angle-right"></i></a></li>');
                    this.obj.append('<li ' + (pageNo == totalPages ? 'class="disabled"' : '') + '><a href="javascript:goTo(' + totalPages + ',' + callback + ')" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>');
                }
            }
        };
    },
    ajax: function (url, data, success, error, type, loadingDelay, options) {
        if (loadingDelay == "undefined") {
            loadingDelay = 0;
        }
        if (loadingDelay > 0) {
            layer.load();
        }
        setTimeout(function () {
            var op = {
                type: type,
                url: url,
                data: data,
                dataType: 'json',
                success: function (data) {
                    layer.closeAll('loading');
                    if (data == null || data == undefined) {
                        if (typeof error == 'function')
                            error();
                    } else {
                        if (typeof success == 'function')
                            success(data);
                    }
                },
                error: function () {
                    layer.closeAll('loading');
                }
            };
            if (options) {
                op = $.extend({}, op, options);
            }
            $.ajax(op);
        }, loadingDelay);
    },
    utils: {
        formatString: function () { if (arguments.length == 0) return ''; if (arguments.length == 1) return arguments[0]; var args = hot.utils.cloneArray(arguments); args.splice(0, 1); return arguments[0].replace(/{(\d+)?}/g, function ($0, $1) { return args[parseInt($1)]; }); },
        cloneArray: function (arr) { var cloned = []; for (var i = 0, j = arr.length; i < j; i++) { cloned[i] = arr[i]; } return cloned; }
    },
    //图片上传
    uploadImg: function (btnFile, uploadPath, callback, data) {
        var uploadUrl = '/UploadFileEidt.aspx?uploadtype=1&userid=' + uploadPath + '';
        $.ajaxFileUpload({
            url: uploadUrl,
            secureuri: false,//安全协议
            fileElementId: btnFile,//id
            dataType: 'json',
            type: "post",
            data: data,
            error: function (data, status, e) {
            },
            success: function (json) {
                if (json.success) {
                    hot.tip.success("上传成功");
                    callback(json.fileUrl);
                } else {
                    hot.tip.error("上传失败");
                }
            }
        });
    },
    exportHelper : {
    checkPage: function (txtBeginPage, txtEndPage,_maxPage) {
        var numberBug = totalPage;//总页数
        if (txtBeginPage == '' || txtEndPage == '') {
            return {flag: false, msg: '请输入正确的起止页码'};
        }
        var beginPage = parseInt(txtBeginPage);
        var endPage = parseInt(txtEndPage);
        if (endPage < beginPage) {
            return {flag: false, msg: '结束页不能小于起始页'};
        }
        if (endPage > numberBug) {
            return {flag: false, msg: '结束页不能大于总页数'};
        }
        if (endPage - beginPage >= _maxPage) {
            $('#txtEndPage').focus().select();
            return {flag: false, msg: '最多导出' + _maxPage + '页'};
        }
        return {flag: true};
    }
}
});

function goTo(pageNo, callback) {
    callback(pageNo);
}