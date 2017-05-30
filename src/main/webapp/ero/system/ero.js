/**
 * Created by Errol on 2016/6/10.
 */

;(function ($) {

    $.ero = {
        reqIdForPlugin: undefined,
        navigator: {
            curReqId: undefined,
            data: new Array(),
            getNavigation: function (reqId, pageId) {
                var key = pageId==undefined?'reqId':'pageId';
                var val = pageId==undefined?reqId:pageId;
                for (var i=0; i<this.data.length; i++) {
                    if (this.data[i][key] == val) {
                        return this.data[i];
                    }
                }
            },
            getReqId: function (navigation) {
                return navigation['reqId']
            },
            getPageId: function (navigation) {
                return navigation['pageId']
            },
            getReqObj: function (navigation) {
                if (navigation['reqObj'] == undefined) {
                    navigation['reqObj'] = $("#" + navigation['reqId'])
                }
                return navigation['reqObj']
            },
            getPageObj: function (navigation) {
                if (navigation['pageObj'] == undefined) {
                    navigation['pageObj'] = $("#" + navigation['pageId'])
                }
                return navigation['pageObj']
            },
            reloadData: function(navigation) {
                if (navigation['reloadData'] != undefined) {
                    navigation['reloadData']()
                }
            }
        },
        pluginManager: {
            loadedPages: new Array(),
            pluginConfigs: new Object(),
            setConfig: function(pageId, pluginId, config) {
                if (this.pluginConfigs[pageId] == undefined) {
                    this.pluginConfigs[pageId] = new Object();
                }
                (this.pluginConfigs[pageId])[pluginId] = config;
            },
            getConfig: function (pluginId, pageId) {
                if (pageId == undefined) {
                    pageId = this.loadedPages[this.loadedPages.length-1];
                }
                return (this.pluginConfigs[pageId])[pluginId];
            },
            removeConfig: function (pageId) {
                delete this.pluginConfigs[pageId];
            }
        },
        showPage: function (tarReqId) {
            var ero = this;
            var tarReqNavigation = ero.navigator.getNavigation(tarReqId);
            var tarReqObj = ero.navigator.getReqObj(tarReqNavigation);
            var tarPageId = ero.navigator.getPageId(tarReqNavigation);
            getPage();
            PageTitleObj.text(tarReqObj.text());
            function getPage() {
                var index = ero.pluginManager.loadedPages.lastIndexOf(tarPageId);
                if (index == -1) {
                    loadPage();
                } else {
                    ero.pluginManager.loadedPages.splice(index, 1);
                    ero.pluginManager.loadedPages.push(tarPageId);
                    switchPage();
                    ero.navigator.reloadData(tarReqNavigation);
                }
            }
            function loadPage() {
                $.ajax({
                    url: "navigator/validator",
                    type: "post",
                    data: {
                        type: 'page',
                        reqId: tarReqId
                    },
                    datatype: "json",
                    success: function (res) {
                        if (res.code == 0) {
                            ero.reqIdForPlugin = tarReqId;
                            PageContainerObj.append(res.data);
                            switchPage();
                            if (ero.pluginManager.loadedPages.push(tarPageId) > LoadedPagesMaxLength) {
                                var shiftId = ero.pluginManager.loadedPages.shift();
                                var shiftNavigation = ero.navigator.getNavigation('', shiftId);
                                ero.getPageObj(shiftNavigation).remove();
                                ero.pluginManager.removeConfig(shiftId)
                            }
                        } else {
                            ero.showErrorMessage(res.code, res.data);
                        }
                    },
                    error: function (xhr, msg, obj) {
                        ero.showErrorMessage(-6);
                        ero.getAjaxErrorMessage(xhr, msg, obj);
                    }
                });
            }
            function switchPage() {
                var tarPageObj = ero.navigator.getPageObj(tarReqNavigation);
                if (ero.navigator.curReqId != undefined) {
                    var curReqNavigation = ero.navigator.getNavigation(ero.navigator.curReqId);
                    ero.navigator.getReqObj(curReqNavigation).removeClass("sm_slct");
                    ero.navigator.getPageObj(curReqNavigation).fadeOut(FadeToggleSpeed, function () {
                        tarPageObj.fadeIn(FadeToggleSpeed);
                    });
                }else {
                    tarPageObj.css({"display":"block", "visibility":"hidden"});
                    BodyContentObj.fadeIn(FadeToggleSpeed, function () {
                        tarPageObj.removeAttr("style").fadeIn(FadeToggleSpeed);
                    })
                }
                ero.navigator.curReqId = tarReqId;
                tarReqObj.addClass("sm_slct");
            }

            // setTimeout(function () {
            //     console.log('ero.pluginManager.loadedPages = ')
            //     console.log(ero.pluginManager.loadedPages)
            //     console.log('ero.pluginManager.pluginConfigs = ')
            //     console.log(ero.pluginManager.pluginConfigs)
            //     console.log('$.ero.navigator.data = ')
            //     console.log( $.ero.navigator.data)
            // }, 1000)
        },
        resetContent: function () {
            var ero = this;
            var curReqNavigation = ero.navigator.getNavigation(ero.navigator.curReqId);
            BodyContentObj.fadeOut(FadeToggleSpeed, function () {
                ero.navigator.getPageObj(curReqNavigation).fadeOut();
            });
            ero.navigator.getReqObj(curReqNavigation).removeClass("sm_slct");
            ero.navigator.curReqId = undefined;
        },
        showErrorMessage: function (code, msg) {
            var msgObj = ErrorMessageObj;
            var message = null;
            switch (parseInt(code)) {
                case -1://自定义错误类型
                    message = msg ? msg : "参数错误！";
                    break;
                case -2://登录超时
                    if (msgObj.data("redirect") == undefined){
                        msgObj.data("redirect", "redirect");
                        message = "登录已超时，请重新登录！";
                        alert(message);
                        window.location.href = "./login.html";
                    }
                    break;
                case -3://权限不足
                    message = "权限不足，操作非法！<br>点击反馈错误！";
                    break;
                case -4://编码错误
                    message = "系统错误，请联系技术服务！<br>点击发送错误报告！";
                    break;
                case -5://恶意攻击
                    if (msgObj.data("redirect") == undefined) {
                        msgObj.data("redirect", "redirect");
                        message = "操作非法，请联系技术服务！";
                        alert(message);
                        window.location.href = "admin/login_out";
                    }
                    return false;
                    break;
                case -6://异步加载页面错误
                    message = "重要素材载入失败！<br>请联系技术服务！";
                    break;
                case -7:
                    message = "获取页面初始数据失败！<br>请联系技术服务！";
                    break;
                case -8:
                    message = "页面组件初始化失败！<br>请联系技术服务！";
                    break;
                case -9:
                    message = "获取数据失败！<br>请联系技术服务！";
                    break;
                case -10:
                    message = "提交数据失败！<br>请联系技术服务！";
                    break;
                default ://传参错误
                    message = "参数错误！";
            }
            msgObj.html(message);
            msgObj.stop().fadeIn(FadeToggleSpeed, function () {
                msgObj.setResettableTimeout(10000, function () {
                    msgObj.fadeOut(FadeToggleSpeed, function () {
                        msgObj.html("");
                    });
                });
            });
            // $.reset_content();
        },
        getAjaxErrorMessage: function (xhr, msg, obj) {
            var errmsg = "xhr.readyState:"+xhr.readyState+"/xhr.status:"+xhr.status+"/xhr.statusText:"+xhr.statusText+"/status:"+msg+"/error:"+obj;
            // alert(errmsg);
        },
    };
    $.fn.reloadData = function (fun) {
        var pageId = $(this).attr("id");
        var navigation = $.ero.navigator.getNavigation("", pageId);
        navigation['reloadData'] = fun;
    };

    //show prompt message
    $.fn.show_msg = function (message) {
        var msgObj = $(this);
        if (msgObj.attr("class") == "msg_style") {
            msgObj.html(message);
            msgObj.stop().fadeIn(FadeToggleSpeed, function () {
                msgObj.setResettableTimeout(5000, function () {
                    msgObj.fadeOut(FadeToggleSpeed, function () {
                        msgObj.html("");
                    });
                });
            });
        } else {
            $.ero.showErrorMessage(-4);//编码错误
            return;
        }
    };

    //reset timeout when the fun triggered again during last timeout
    $.fn.setResettableTimeout = function (millisecond, callback) {
        var obj = $(this);
        obj.data("timeout", new Date().getTime() + millisecond);
        setTimeout(function () {
            if (new Date().getTime() >= obj.data("timeout")){callback();}
        }, millisecond);
    };

    //return false until timeout
    window.ArrayofTimeout = new Array();
    $.fn.prevent_until_timeout = function (millisecond) {
        var tarId = $(this).attr("id");
        if (ArrayofTimeout.indexOf(tarId) != -1) {
            return false;
        } else {
            setTimeout(function () {
                ArrayofTimeout.splice(ArrayofTimeout.indexOf(tarId), 1);
            }, parseInt(millisecond));
            ArrayofTimeout.push(tarId);
            return true;
        }
    };

    //date_string formatter for jackson
    $.fn.convert_date_format = function () {
        var date_string = $(this).val();
        var date_string_format = new String();
        var reg_string = /\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}:\d{2}/;
        if (reg_string.test(date_string)) {
            date_string_format = date_string.slice(0, 10) + "T" + date_string.slice(10) + ".000+0800";
            // detail_page_data = detail_page_data.replace(date_string, date_string_format);
            return date_string_format;
        }
    };

    //js array indexOf
    $.indexOf = function (array, element, start, end) {
        var i = start==undefined?0:start;
        var l = end==undefined?array.length:end;
        for (i;i<l; i++){
            if (array[i] == element) return i;
        }
        return -1;
    }

})(jQuery);

jQuery(function ($) {

    $.ajax({
        url: "admin/initialization",
        type: "post",
        data: {},
        datatype: "json",
        success: function (res) {
            if (res.code == 0) {
                $("#hrab_span").text(res.data.adminName);
                var domStr = "";
                var list = res.data.sidebarList;
                var obj;
                for (var i=0; i<list.length; i++) {
                    obj = list[i];
                    if (obj["level"] == 1) {
                        if (domStr != "") {
                            domStr += "</ul></li>";
                        }
                        domStr += "<li id="+obj["id"]+" class='m_m_s sul_even'><img class='sul_img' src="+obj["imgUrl"]+"><span class='sule_span'>"+obj["name"]+"</span></li>";
                        domStr += "<li class='sul_odd'><ul>";
                    }else if (obj["level"] == 2) {
                        $.ero.navigator.data.push({reqId:obj["id"], pageId:obj["pageId"], reqObj:undefined, pageObj:undefined, reloadData:undefined});
                        domStr += "<li id="+obj["id"]+" class='s_m_s sulou_li'>"+obj["name"]+"</li>"
                    }
                }
                domStr += "</ul></li>";
                SidebarBoxObj.append(domStr);
                //sidebar
                SidebarBoxObj.children(":even").click(function () {
                    $(this).toggleClass("mm_slct").next().slideToggle(SlideToggleSpeed);
                });
                SidebarBoxObj.children(":odd").click(function (e) {
                    if ($(this).prevent_until_timeout(700)) {
                        $.ero.showPage(e.target.id)
                    }
                });
                SidebarBoxObj.children(":odd").dblclick(function () {
                    return false;
                });
            }else {
                $.ero.showErrorMessage(res.code, res.data);
            }
        },
        error: function (xhr, msg, obj) {
            $.ero.showErrorMessage(-4);
            $.ero.getAjaxErrorMessage(xhr, msg, obj);
        }
    });

    //header
    $("#hr_admin").mouseenter(function () {
        $("#hr_admin").removeClass("m_b_c").addClass("m_c_c");
        $("#hra_ul").stop(true).slideDown(function(){
            $("#hrab_arrow").attr("src", "com-res/common/img/arrow/a_up.png");
        });
    });
    $("#hr_admin").mouseleave(function () {
        $("#hra_ul").stop(true).slideUp(function(){
            $("#hr_admin").removeClass("m_c_c").addClass("m_b_c");
            $("#hrab_arrow").attr("src", "com-res/common/img/arrow/a_down.png");
        });
    });
    $("#login_out").click(function(){
        window.location.href = "admin/login_out";
    });
});