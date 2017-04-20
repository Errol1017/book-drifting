/**
 * Created by Errol on 2016/6/10.
 */

;(function ($) {

    //list_table
    $.fn.list_table = function (param) {
        /**
         * @param param
         * param.reqId              id      required
         * param.tableId            id      required
         * param.tableObj           object      required
         * param.msgObj             object      default: $("#" + param.tableId + "_msg")】
         * param.data               object
         *      data.list
         *      data.totalNum
         *      data.totalPage
         *      data.curPageNum
         *      data.tarPageNum
         *      data.perPageNum
         *      data.totalNumChanged
         *      data.totalPageChanged
         * param.header             object
         *      header.headerObj
         *      header.names            array[string_cn]    required
         *      header.colWith          array[css class]
         *      header.colsNum
         * param.body             object
         *      body.bodyObj
         *      body.data
         *      body.tdConf         array[css class]    required
         * param.footer           object
         *      footer.totalNumObj
         *      footer.totalPageObj
         *      footer.rightObj
         *      footer.firstPageObj
         *      footer.firstButtonObj
         *      footer.secondButtonObj
         *      footer.thirdButtonObj
         *      footer.inputObj
         *      footer.lastPageObj
         * param.paging             object
         *      paging.id               id
         * param.handle             object
         *      handle.handleObj        default $("#" + param.tableId + "_HandleBox")
         *      handle.editObj
         *      handle.deleteObj
         *      handle.data
         *      handle.edit
         *              edit.callback
         *      handle.delete
         *              delete.before
         *              delete.key      default 'id'
         *              delete.success
         *              delete.error
         */
        var pageId = $(this).attr("id");
        init();
        getList();
        console.log(param)
        return {
            getList: function () {
                getList()
            }
        };
        function init() {
            initParam();
            drawHeader();
            drawFooter();
            registerFooterEvent();
            initHandleEvent();
            // $.ero.pluginManager.setConfig(pageId, param.tableId, param);
            function initParam() {
                param.reqId = $.ero.navigator.getReqId($.ero.navigator.getNavigation("", pageId));
                if (param.msgObj == undefined) {
                    param.msgObj = $("#" + param.tableId + "_Msg")
                }
                param.tableObj = $("#" + param.tableId);
                param.data = {
                    tarPageNum: 1,
                    totalNum: 0,
                    totalPage: 0,
                    totalNumChanged: false,
                    totalPageChanged: false
                };
                if (param.data.perPageNum == undefined) {
                    param.data.perPageNum = 10;
                }
                if (param.handle.handleObj == undefined) {
                    param.handle.handleObj = $("#" + param.tableId + "_HandleBox");
                }
                if (param.handle.editObj == undefined) {
                    param.handle.editObj = $("#" + param.tableId + "_Edit");
                }
                if (param.handle.deleteObj == undefined) {
                    param.handle.deleteObj = $("#" + param.tableId + "_Delete");
                }
            }
            function drawHeader() {
                var names = param.header.names;
                var colWidth = param.header.colWith;
                var cols = "";
                var ths = "";
                var colsNum = 0;
                var v1;
                var v2 = colWidth == undefined;
                for (var i = 0; i < names.length; i++) {
                    if (!(/^!_/.test(names[i]))) {
                        v1 = (colsNum++) % 2 == 0;
                        cols += '<col class="' + (v1 ? 'lt_col_even' : 'lt_col_odd') + (v2 ? '' : (' ' + colWidth[i])) + '">';
                        ths += '<th class="lt_th ' + (v1 ? 'lt_th_even' : 'lt_th_odd') + '">' + names[i] + '</th>';
                    }
                }
                param.header.colsNum = colsNum;
                var colgroup = '<colgroup>' + cols + '</colgroup>';
                var thead = '<thead class="lt_thead"><tr class="lt_thead_tr">' + ths + '</tr></thead>';
                var tbody = '<tbody class="lt_tbody"></tbody>';
                var tableObj = param.tableObj;
                tableObj.append(colgroup + thead + tbody);
                param.header.headerObj = tableObj.find("thead").first();
                if (param.body == undefined) {
                    param.body = {
                        bodyObj: tableObj.find("tbody").first()
                    }
                }else {
                    param.body.bodyObj = tableObj.find("tbody").first();
                }
            }
            function drawFooter() {
                var tf = "<tfoot class='list_table_footer'><tr><td class='list_table_footer_td' colspan='" + param.header.colsNum+ "'>匹配数据&nbsp;<span>0</span>&nbsp;条&nbsp;共&nbsp;<span>0</span>&nbsp;页" +
                    "<span class='float_right'><button class='button_style_50_25' value='1'>首页</button><input type='button' class='button_style_x_25' value='1'/>" +
                    "<input type='button' class='button_style_x_25' value='2'/><input type='button' class='button_style_x_25' value='3'/>" +
                    "<input type='text' class='input_text_28 span50' placeholder='页数'/><button class='button_style_50_25' value='0'>尾页</button></span></td></tr></tfoot>";
                param.tableObj.append(tf);
                var s1 = param.tableObj.find('td').last().find("span").first();
                var s2 = s1.next();
                var r = s2.next();
                var b1 = r.find('button').first();
                var i1 = b1.next();
                var i2 = i1.next();
                var i3 = i2.next();
                var i = i3.next();
                var b2 = i.next();
                param.footer = {
                    totalNumObj: s1,
                    totalPageObj: s2,
                    rightObj: r,
                    firstPageObj: b1,
                    firstButtonObj: i1,
                    secondButtonObj: i1,
                    thirdButtonObj: i1,
                    inputObj: i1,
                    lastPageObj: b2
                }
            }
            function registerFooterEvent() {
                var i = 0;
                param.footer.rightObj.children().each(function () {
                    if (i != 4) {
                        $(this).click(function () {
                            param.data.tarPageNum = $(this).val();
                            getList();
                        })
                    } else {
                        $(this).change(function () {
                            var num = parseInt($(this).val());
                            var totalPage = param.data.totalPage;
                            if (isNaN(num)) {
                                $(this).val("");
                                param.msgObj.show_msg("请输入 1 - " + totalPage + "之间的整数");
                                return false;
                            }
                            if (num < 1) {
                                num = 1;
                            }
                            if (num > totalPage) {
                                num = totalPage;
                            }
                            param.data.tarPageNum = num;
                            getList();
                        })
                    }
                })
            }
            function initHandleEvent() {
                param.handle.handleObj.mouseleave(function () {
                    $(this).fadeOut();
                });
                param.handle.editObj.click(function () {
                    param.handle.edit.callback(param.handle.data)
                    param.handle.handleObj.fadeOut();
                });
                param.handle.deleteObj.click(function () {
                    if (param.handle.delete.before != undefined) {
                        if (!param.handle.delete.before()) {
                            return false;
                        }
                    }
                    deleteData(function () {
                        if (param.handle.delete.success != undefined) {
                            param.handle.delete.success(param.handle.data)
                        }
                        getList();
                    })
                });
                function deleteData(success) {
                    $.ajax({
                        url: "navigator/validator",
                        type: "post",
                        data: {
                            type: "delete",
                            reqId: param.reqId,
                            listId: param.tableId,
                            dataId: param.handle.data[param.handle.delete.key==undefined?'id':param.handle.delete.key]
                        },
                        datatype: "json",
                        success: function (res) {
                            if (res.code == 0) {
                                param.msgObj.show_msg("数据删除成功");
                                success();
                            } else if (res.code == -1) {
                                param.msgObj.show_msg(res.data);
                            } else {
                                $.ero.showErrorMessage(res.code, res.data);
                            }
                        },
                        error: function (xhr, msg, obj) {
                            $.ero.showErrorMessage(-10);
                            $.ero.getAjaxErrorMessage(xhr, msg, obj);
                        }
                    });
                }
            }
        }
        function getList() {
            $.ajax({
                url: "navigator/validator",
                type: "post",
                data: {
                    type: "list",
                    reqId: param.reqId,
                    listId: param.tableId,
                    tarPageNum: param.data.tarPageNum,
                    perPageNum: param.data.perPageNum
                },
                datatype: "json",
                success: function (res) {
                    if (res.code == 0) {
                        param.data.list = res.data.list;
                        param.data.curPageNum = param.data.tarPageNum;
                        if (param.data.totalNum != res.data.totalNum) {
                            param.data.totalNumChanged = true;
                            param.data.totalNum = res.data.totalNum;
                            var n = Math.ceil(param.data.totalNum / param.data.perPageNum);
                            if (param.data.totalPage != n) {
                                param.data.totalPage = n;
                                param.data.totalPageChanged = true;
                            }
                        }
                        redraw();
                    } else if (res.code == -1) {
                        param.msgObj.show_msg(res.data);
                    } else {
                        $.ero.showErrorMessage(res.code, res.data);
                    }
                },
                error: function (xhr, msg, obj) {
                    $.ero.showErrorMessage(-9);
                    $.ero.getAjaxErrorMessage(xhr, msg, obj);
                }
            });
            function redraw() {
                redrawBody();
                redrawFooter();
                function redrawBody() {
                    var trs = "";
                    var len = param.data.list.length;
                    if (len == 0) {
                        trs = '<tr class="lt_tbody_tr lt_tr_no_data"><td colspan="' + param.header.colsNum + '">暂无相关数据</td></tr>'
                    } else {
                        var tdConf = param.body.tdConf;
                        var names = param.header.names;
                        var list = param.data.list;
                        var arr = new Array();
                        for (var i = 0; i < len; i++) {
                            var tds = "";
                            var j = 0;
                            arr[i] = new Object();
                            for (var key in list[i]) {
                                if (/^!_/.test(names[j])) {
                                    arr[i][key] = list[i][key];
                                } else {
                                    if (tdConf == undefined || tdConf[j] == '') {
                                        tds += '<td>' + list[i][key] + '</td>';
                                    } else {
                                        tds += '<td class="' + tdConf[j] + '">' + list[i][key] + '</td>';
                                    }
                                }
                                j++;
                            }
                            trs += '<tr class="lt_tbody_tr">' + tds + '</tr>';
                        }
                        param.body.data = arr;
                    }
                    param.body.bodyObj.empty().append(trs);
                    registerBodyEvent();
                }
                function redrawFooter() {
                    var data = param.data;
                    var totalPage = param.data.totalPage;
                    var footer = param.footer;
                    if (data.totalNumChanged) {
                        footer.totalNumObj.text(data.totalNum);
                    }
                    redrawFooterRightBox();
                    redrawFooterSelectedButton();
                    function redrawFooterRightBox() {
                        if (data.totalPageChanged) {
                            footer.totalNumObj.text(data.totalNum);
                            footer.totalPageObj.text(data.totalPage);
                            footer.lastPageObj.val(data.totalPage);
                            if (totalPage <= 1) {
                                footer.rightObj.hide();
                            } else {
                                footer.rightObj.show();
                                if (totalPage == 2) {
                                    footer.firstPageObj.hide();
                                    footer.thirdButtonObj.hide();
                                    footer.inputObj.hide()
                                    footer.lastPageObj.hide()
                                } else if (totalPage == 3) {
                                    footer.firstPageObj.hide();
                                    footer.thirdButtonObj.show();
                                    footer.inputObj.hide();
                                    footer.lastPageObj.hide();
                                } else if (totalPage > 3 && totalPage < 7) {
                                    footer.firstPageObj.show();
                                    footer.thirdButtonObj.show();
                                    footer.inputObj.hide();
                                    footer.lastPageObj.show();
                                } else {
                                    footer.firstPageObj.show();
                                    footer.thirdButtonObj.show();
                                    footer.inputObj.show();
                                    footer.lastPageObj.show();
                                }
                            }
                        }
                    }
                    function redrawFooterSelectedButton() {
                        var curPageNum = data.curPageNum;
                        if (curPageNum == 1) {
                            footer.firstButtonObj.val(1).addClass('lt_page_slct');
                            footer.secondButtonObj.val(2).removeClass('lt_page_slct');
                            footer.thirdButtonObj.val(3).removeClass('lt_page_slct');
                        } else if (curPageNum == totalPage && curPageNum != 2) {
                            footer.firstButtonObj.val(totalPage - 2).removeClass('lt_page_slct');
                            footer.secondButtonObj.val(totalPage - 1).removeClass('lt_page_slct');
                            footer.thirdButtonObj.val(totalPage).addClass('lt_page_slct');
                        } else {
                            footer.firstButtonObj.val(curPageNum - 1).removeClass('lt_page_slct');
                            footer.secondButtonObj.val(curPageNum).addClass('lt_page_slct');
                            footer.thirdButtonObj.val(curPageNum + 1).removeClass('lt_page_slct');
                        }
                    }
                }
                function registerBodyEvent() {
                    param.body.bodyObj.click(function (e) {
                        var trObj = $(e.target).parent();
                        trObj.addClass("lt_tr_slct").siblings().removeClass("lt_tr_slct");
                        param.handle.handleObj.hide().css("top", e.clientY + document.body.scrollTop + 1 + "px").css("left", e.clientX + 1 + "px").slideDown();
                        param.handle.data = param.body.data[trObj.index()];
                    })
                }
            }
        }

    };
    
    //form_detail
    $.fn.form_detail = function (param) {
        /**
         * @param param
         * param.reqId            id
         * param.formId           id
         * param.msgObj           id        default $("#" + param.formId + "_Msg")
         * param.inputIds         array[id]
         * param.keys
         * param.inputObjs
         * param.data
         * param.validator        array[fun]
         * param.bind             object
         *      bind.before
         *      bind.after
         * param.unbind           object
         *      unbind.before
         *      unbind.after
         * param.gather           object
         *      gather.before
         *      gather.after
         * param.submit           object
         *      submit.before
         *      submit.after
         *      submit.success
         *      submit.error
         * param.handle           object
         *      handle.submitObj    default $("#" + param.formId + '_Submit')
         *      handle.unbindObj    default $("#" + param.formId + '_Unbind')
         */
        var pageId = $(this).attr("id");
        init();
        console.log(param)
        return {
            getForm: function (dataId) {
                getForm(dataId)
            },
            unbind: function () {
                unbind()
            }
        };
        function init() {
            initParam();
            initValidateEvent();
            initHandleEvent();
            function initParam() {
                param.reqId = $.ero.navigator.getReqId($.ero.navigator.getNavigation("", pageId));
                if (param.msgObj == undefined) {
                    param.msgObj = $("#" + param.formId + "_Msg")
                }
                param.data = new Object();
            }
            function initValidateEvent() {
                var ids = param.inputIds;
                var keys = new Array();
                var objs = new Array();
                for (var i = 0; i < ids.length; i++) {
                    if (!(/^!_/.test(ids[i]))) {
                        keys[i] = ids[i];
                        ids[i] = param.formId + '_' + ids[i];
                        objs[i] = $("#" + ids[i]);
                        objs[i].change(function () {
                            param.msgObj.set_promptObj();
                            param.validator[$.indexOf(param.inputIds, $(this).attr("id"))]($(this))
                        });
                    } else {
                        keys[i] = ids[i].slice(2);
                        objs[i] = $("#" + param.formId + ids[i].slice(1));
                        param.validator.splice(i, 0, '')
                    }
                }
                param.keys = keys;
                param.inputObjs = objs;
            }
            function initHandleEvent() {
                initEvent();
                function initEvent() {
                    if (param.handle == undefined) {
                        param.handle = {
                            submitObj: $("#" + param.formId + '_Submit'),
                            unbindObj: $("#" + param.formId + '_Unbind')
                        }
                    } else {
                        // handle 中添加新的按钮或事件
                        // if (param.handle.submitObj == undefined) {
                        //     param.handle.submitObj = $("#" + param.formId + '_Submit');
                        // }
                        // if (param.handle.unbindObj == undefined){
                        //     param.handle.unbindObj = $("#" + param.formId + '_Unbind');
                        // }
                    }
                    param.handle.submitObj.click(function () {
                        submit();
                    });
                    param.handle.unbindObj.click(function () {
                        unbind();
                    });
                }
                function submit() {
                    if (validate()) {
                        gather();
                        send();
                    }
                    function validate() {
                        param.msgObj.set_promptObj();
                        for (var i = 0; i < param.validator.length; i++) {
                            if (param.validator[i] != '') {
                                if (!(param.validator[i](param.inputObjs[i]))) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    }
                    function gather() {
                        // if (param.gather != undefined && param.gather.before != undefined) {
                        //     param.gather.before();
                        // }
                        param.data = new Object();
                        for (var i=0;i<param.keys.length;i++){
                            param.data[param.keys[i]] = param.inputObjs[i].val()
                        }
                        if (param.gather != undefined && param.gather.after != undefined) {
                            param.gather.after();
                        }
                    }
                    function send() {
                        // if (param.submit != undefined && param.submit.before != undefined) {
                        //     param.submit.before();
                        // }
                        $.ajax({
                            url: "navigator/validator",
                            type: "post",
                            data: {
                                type: "submit",
                                reqId: param.reqId,
                                formId: param.formId,
                                data: JSON.stringify(param.data)
                            },
                            datatype: "json",
                            success: function (res) {
                                if (res.code == 0) {
                                    param.msgObj.show_msg("数据保存成功");
                                    param.handle.submitObj.text('更 新');
                                    if (param.submit != undefined && param.submit.success != undefined) {
                                        param.submit.success(res.data);
                                    }
                                } else if (res.code == -1) {
                                    param.msgObj.show_msg(res.data);
                                    // if (param.submit != undefined && param.submit.error != undefined) {
                                    //     param.submit.error();
                                    // }
                                } else {
                                    $.ero.showErrorMessage(res.code, res.data);
                                }
                            },
                            error: function (xhr, msg, obj) {
                                $.ero.showErrorMessage(-10);
                                $.ero.getAjaxErrorMessage(xhr, msg, obj);
                            }
                        });
                        // if (param.submit != undefined && param.submit.after != undefined) {
                        //     param.submit.after();
                        // }
                    }
                }
            }
        }
        function getForm(dataId) {
            $.ajax({
                url: "navigator/validator",
                type: "post",
                data: {
                    type: "form",
                    reqId: param.reqId,
                    formId: param.formId,
                    dataId: dataId
                },
                datatype: "json",
                success: function (res) {
                    if (res.code == 0) {
                        param.data = res.data;
                        bind();
                    } else if (res.code == -1) {
                        param.msgObj.show_msg(res.data);
                    } else {
                        $.ero.showErrorMessage(res.code, res.data);
                    }
                },
                error: function (xhr, msg, obj) {
                    $.ero.showErrorMessage(-9);
                    $.ero.getAjaxErrorMessage(xhr, msg, obj);
                }
            });
            function bind() {
                if (param.bind != undefined && param.bind.before != undefined) {
                    param.bind.before();
                }
                for (var i = 0; i < param.keys.length; i++) {
                    param.inputObjs[i].val(param.data[param.keys[i]])
                }
                if (param.bind != undefined && param.bind.after != undefined) {
                    param.bind.after();
                }
                param.handle.submitObj.text('更 新');
            }
        }
        function unbind() {
            if (param.unbind != undefined && param.unbind.before != undefined) {
                param.unbind.before();
            }
            for (var i = 0; i < param.inputObjs.length; i++) {
                param.inputObjs[i].val('');
            }
            if (param.unbind != undefined && param.unbind.after != undefined) {
                param.unbind.after();
            }
            param.handle.submitObj.text('新 增');
        }
    };

    //form validator
    window.promptObj = undefined;
    $.fn.extend({
        set_promptObj: function () {
            promptObj = $(this);
        },
        err_msg: "",
        tab_name: function () {
            return $(this).parent().prev().text() + " ";
        },
        required: function () {
            if ($(this).val() == "") {
                this.err_msg = this.tab_name() + "不能为空 ";
            }
            return this;
        },
        check_length: function (min_length, max_length) {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    var real_length = 0;
                    var char_code = -1;
                    for (var i = 0; i < content.length; i++) {
                        char_code = content.charCodeAt(i);
                        if (char_code >= 0 && char_code <= 128) {
                            real_length += 1;
                        } else {
                            real_length += 3;
                        }
                    }
                    if (real_length < min_length || real_length > max_length) {
                        this.err_msg = this.tab_name() + "长度不合法  请输入" + min_length + "-" + max_length + "个字符（一个中文占三个字符） ";
                    }
                }
            }
            return this;
        },
        check_simple_length: function (min_length, max_length) {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    var real_length = content.length;
                    if (real_length < min_length || real_length > max_length) {
                        this.err_msg = this.tab_name() + "长度不合法  请输入" + min_length + "-" + max_length + "个字符（中英文各占一个）";
                    }
                }
            }
            return this;
        },
        require_number: function (min_num, max_num, reqInt) {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    if (isNaN(content)) {
                        this.err_msg = this.tab_name() + "必须是数字 ";
                    } else {
                        if (typeof min_num == "string") {
                            if (min_num == ">" && content <= max_num) {
                                this.err_msg = this.tab_name() + "数值不合法  请输入 大于 " + max_num + "的数字 ";
                            } else if (min_num == "<" && content >= max_num) {
                                this.err_msg = this.tab_name() + "数值不合法  请输入 小于 " + max_num + "的数字 ";
                            } else if (min_num == ">=" && content < max_num) {
                                this.err_msg = this.tab_name() + "数值不合法  请输入 不小于 " + max_num + "的数字 ";
                            } else if (min_num == "<=" && content > max_num) {
                                this.err_msg = this.tab_name() + "数值不合法  请输入 不大于 " + max_num + "的数字 ";
                            }
                        } else if (typeof min_num == "number" && (content < min_num || content > max_num)) {
                            this.err_msg = this.tab_name() + "数值不合法  请输入" + min_num + "-" + max_num + "之间的数字 ";
                        }
                        if (reqInt || min_num) {
                            var index = content.indexOf(".");
                            if (index != -1 && content.slice(index + 1) != 0) {
                                this.err_msg = this.tab_name() + "必须是整数 ";
                            }
                        }
                    }
                }
            }
            return this;
        },
        require_img: function (default_src) {
            if (this.err_msg == "") {
                var src = $(this).attr("src");
                if (src == default_src || src == "") {
                    this.err_msg = "图片不能为空  请上传图片 ";
                }
            }
            return this;
        },
        require_phone_number: function () {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    var reg = /^1\d{10}$/;
                    if (!reg.test(content)) {
                        this.err_msg = this.tab_name() + "格式不合法  请输入十一位数手机号码 ";
                    }
                }
            }
            return this;
        },
        require_strict_phone_number: function () {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    var reg = /^1[3|4|5|7|8]\d{9}$/;
                    if (!reg.test(content)) {
                        this.err_msg = this.tab_name() + "格式不合法  请输入十一位数手机号码 ";
                    }
                }
            }
            return this;
        },
        require_email: function () {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
                    if (!reg.test(content)) {
                        this.err_msg = this.tab_name() + "格式不合法  请输入正确的邮箱地址 ";
                    }
                }
            }
            return this;
        },
        require_round_ID_number: function () {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    var reg = /(^\d{15}$)|(^\d{17}([0-9]|X|x)$)/;
                    if (!reg.test(content)) {
                        this.err_msg = this.tab_name() + "格式不合法  请输入有效身份证号码 ";
                    }
                }
            }
            return this;
        },
        require_ID_number: function () {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    var reg_18 = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X|x)$/;
                    var reg_15 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
                    if (!reg_18.test(content) && !reg_15.test(content)) {
                        this.err_msg = this.tab_name() + "格式不合法  请输入有效身份证号码 ";
                    }
                }
            }
            return this;
        },
        require_strict_ID_number: function () {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    var regIdCard = /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
                    if (!regIdCard.test(content)) {
                        this.err_msg = this.tab_name() + "格式不合法  请输入有效身份证号码 ";
                    } else {
                        if (content.length == 18) {
                            var idCardWi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);//前17位加权因子
                            var idCardY = new Array(1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2);//加权结果除以11后，可能产生的11位余数，假如余数正好是10，则替换为x
                            var idCardWiSum = 0;
                            for (var i = 0; i < 17; i++) {
                                idCardWiSum += content.substring(i, i + 1) * idCardWi[i];
                            }
                            var idCardMod = idCardWiSum % 11;
                            var idCardLast = content.substring(17);
                            if (idCardMod == 2) {
                                if (idCardLast != "X" && idCardLast != "x") {
                                    this.err_msg = this.tab_name() + "格式不合法  请输入有效身份证号码 ";
                                }
                            } else {
                                if (idCardLast != idCardY[idCardMod]) {
                                    this.err_msg = this.tab_name() + "格式不合法  请输入有效身份证号码 ";
                                }
                            }
                        }
                    }
                }
            }
            return this;
        },
        require_date: function () {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    var reg_string = /\d{4}-\d{2}-\d{2}/;
                    if (!reg_string.test(content)) {
                        this.err_msg = this.tab_name() + "格式不合法  请选择或输入有效日期格式 ";
                    }
                }
            }
            return this;
        },
        require_date_time: function () {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    var reg_string = /\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}:\d{2}/;
                    if (!reg_string.test(content)) {
                        this.err_msg = this.tab_name() + "格式不合法  请选择或输入有效日期和时间格式 ";
                    }
                }
            }
            return this;
        },
        require_date_range: function (num_earliest, num_latest) {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    var date_content = new Date(content.replace("-", "/"));
                    var date_earliest = new Date();
                    var date_latest = new Date();
                    date_earliest.setDate(date_earliest.getDate() + num_earliest);
                    date_latest.setDate(date_latest.getDate() + num_latest);
                    if (date_content < date_earliest || date_content > date_latest) {
                        this.err_msg = this.tab_name() + "超出合法范围  可选时间范围： " + date_earliest.toLocaleDateString() + " - " + date_latest.toLocaleDateString();
                    }
                }
            }
            return this;
        },
        common_account_rule: function (min_length, max_length) {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    var reg = new RegExp("\^[a-zA-Z0-9]{" + (min_length) + "," + (max_length) + "}$");
                    if (!content.match(reg)) {
                        this.err_msg = this.tab_name() + "格式不合法  请输入" + min_length + "-" + max_length + "个字符  可输入大小写字母及数字 ";
                    }
                }
            }
            return this;
        },
        common_password_rule: function (min_length, max_length) {
            if (this.err_msg == "") {
                var content = $(this).val();
                if (content != "") {
                    var reg = new RegExp("\^[a-zA-Z][a-zA-Z0-9_]{" + (min_length - 1) + "," + (max_length - 1) + "}$");
                    if (!content.match(reg)) {
                        this.err_msg = this.tab_name() + "格式不合法  请输入" + min_length + "-" + max_length + "个字符  以字母开头并只接受大小写字母、数字和下划线 ";
                    }
                }
            }
            return this;
        },
        show_check_msg: function (msgObj) {
            if (this.err_msg != "") {
                var msgObj = msgObj==undefined?promptObj:msgObj;
                msgObj.show_msg(this.err_msg);
                if ($(this).focus().attr("type") == "hidden") {
                    $(this).next().focus();
                }
                this.err_msg = "";
                return false;
            }
            return true;
        }
    });

    //cascade checkbox
    $.fn.cascade_checkbox = function (param) {
        /**
         * @param param
         * param.reqId          id
         */
    }

})(jQuery)


;
(function ($) {


    //form_tab_card
    $.fn.extend({
        /**
         * @param obj
         * obj.pageId           id
         * obj.formId           id
         * obj.tabsId           id
         * obj.isWhole          boolean
         * obj.inputIds         array[array[id]]
         * obj.curTab           id
         * obj.msgId            id
         * obj.bind             object
         *      bind.id             id
         *      bind.callback       array[function]
         * obj.unbind           object
         *      unbind.id           id
         *      unbind.callback     array[function]
         * obj.validate         object
         *      validate.ids        array[array[id]]
         *      validate.func       array[function]
         *      validate.callback   array[function]
         * obj.gather           object
         *      gather.id           id
         *      gather.callback     function
         * obj.submit           object
         *      submit.id           id
         *      submit.callback     function
         * obj.list             object
         *      list.tableId        id
         *      list.pageId         id
         */
        form_tab_card_register: function (obj) {
            var pageId = $(this).attr("id");
            obj.pageId = pageId;
            var pluginId = obj.formId;
            var pluginObj = $("#" + pluginId);
            pluginObj.register_form_tabs(obj);
            pluginObj.register_form_tabs_validator(obj);
            pluginObj.register_form_tabs_event_trigger(obj);
            $.register_plugin_config(pageId, pluginId, obj);
        },
        register_form_tabs: function (obj) {
            var tabsObj = $("#" + obj.tabsId);
            if (obj.isWhole) {

            } else {
                var i = 0;
                tabsObj.children().each(function () {
                    $(this).data("cardId", i);
                    if (i == 0) {
                        $(this).addClass("ftc_tab_select");
                    }
                    i++;
                });
                obj.curTab = 0;
                var cardId = obj.formId + "0";
                $("#" + cardId).show();
                tabsObj.children().each(function () {
                    $(this).click(function () {
                        $(this).addClass("ftc_tab_select").siblings().removeClass("ftc_tab_select");
                        $("#" + obj.formId + obj.curTab).hide();
                        obj.curTab = $(this).data("cardId");
                        $("#" + obj.formId + obj.curTab).show();
                    })
                })
            }
        },
        register_form_tabs_validator: function (obj) {
            var formId = obj.formId;
            var validate = obj.validate;
            if (obj.msgId == undefined) {
                obj.msgId = formId + "_msg";
            }
            if (validate.ids != undefined) {
                for (var i = 0; i < validate.ids.length; i++) {
                    for (var j = 0; j < validate.ids[i].length; j++) {
                        validate.ids[i][j] = formId + i + "_" + validate.ids[i][j];
                    }
                }
            }
            if (validate.func != undefined) {
                for (var i = 0; i < validate.ids.length; i++) {
                    validate.func[i](obj.msgId);
                }
                validate.func = "done";
            }
        },
        register_form_tabs_event_trigger: function (obj) {
            var formObj = $(this);
            $("#" + obj.bind.id).click(function () {
                formObj.form_tabs_bind_data(obj);
                $("#" + obj.submit.id).text("更 新");
            });
            $("#" + obj.unbind.id).click(function () {
                formObj.form_tabs_unbind_data(obj);
            });
            $("#" + obj.submit.id).click(function () {
                formObj.form_tabs_validate(obj);
            })
        },
        form_tabs_bind_data: function (obj) {
            var formData = TempMemoryofData;
            var curTab = obj.curTab;
            var inputs = obj.inputIds[curTab];
            var str = "#" + obj.formId + curTab + "_";
            var key = "";
            for (var i = 0; i < inputs.length; i++) {
                key = inputs[i];
                $(str + key).val(formData[key]);
            }
            if (obj.bind.callback !== undefined) {
                obj.bind[curTab].callback(formData);
            }
        },
        form_tabs_unbind_data: function (obj) {
            var curTab = obj.curTab;
            var inputs = obj.inputIds[curTab];
            var str = "#" + obj.formId + curTab + "_";
            var key = "";
            for (var i = 0; i < inputs.length; i++) {
                key = inputs[i];
                $(str + key).val("");
            }
            // if (listId != undefined){
            //     $("#"+listId).deselect_list_table_tr();
            // }
            if (obj.unbind.callback != undefined) {
                obj.unbind.callback[curTab]();
            }
        },
        form_tabs_validate: function (obj) {
            var validate = obj.validate;
            var curTab = obj.curTab;
            var ids = validate.ids[curTab];
            for (var i = 0; i < ids.length; i++) {
                $("#" + ids[i]).trigger("change");
                if (!CheckStatus) {
                    return false;
                }
            }
            if (validate.callback != undefined) {
                if (!validate.callback[curTab](obj.msgId)) {
                    return false;
                }
            }
            $(this).form_tabs_gather_data(obj);
        },
        form_tabs_gather_data: function (obj) {
            var formId = obj.formId;
            var curTab = obj.curTab;
            var inputs = obj.inputIds[curTab];
            var str = "#" + obj.formId + curTab + "_";
            var data = new Object();
            for (var i = 0; i < inputs.length; i++) {
                var key = inputs[i];
                data[key] = $(str + key).val();
            }
            if (obj.gather != undefined && obj.gather.callback != undefined) {
                obj.gather.callback[curTab](data);
            }
            data = JSON.stringify(data);
            $(this).form_tabs_submit_data(obj.pageId, formId, data, obj.list.tableId, obj);
        },
        form_tabs_submit_data: function (data, obj) {
            $.ajax({
                url: "navigator/validator",
                type: "post",
                data: {
                    type: "submit",
                    reqId: obj.pageId,
                    formId: obj.formId,
                    data: data,
                    cardId: obj.curTab
                },
                datatype: "json",
                success: function (res) {
                    if (res.code == 0) {
                        $("#" + obj.msgId).show_msg("数据保存成功");
                        if (obj.submit.callback != undefined) {
                            obj.submit.callback[obj.curTab](res.data);
                        }
                        // $("#" + tableId).refresh_list_table_page();
                    } else if (res.code == -1) {
                        $("#" + obj.msgId).show_msg(res.data);
                    } else {
                        $.ero.showErrorMessage(res.code, res.data);
                    }
                },
                error: function (xhr, msg, obj) {
                    $.ero.showErrorMessage(-10);
                    $.ero.getAjaxErrorMessage(xhr, msg, obj);
                }
            })
        }
    });

    //cascade_checkbox
    $.fn.extend({
        /**
         * @param obj
         * obj.boxId            id
         * obj.valId            id
         * obj.dynamic          boolean
         */
        cascade_checkbox_register: function (obj) {
            var pluginId = obj.boxId;
            $("#" + pluginId).set_cascade_checkbox(obj);
        },
        set_cascade_checkbox: function (obj) {
            var boxObj = $(this);
            boxObj.find(".cc_li_even").each(function () {
                $(this).set_cc_even(obj.boxId);
            });
            boxObj.find(".cc_li_odd").each(function () {
                $(this).set_cc_odd(obj.boxId);
            });
            boxObj.output_cc_value(obj.valId);
            boxObj.set_cc_val(obj.valId);
        },
        set_cc_checked: function () {
            $(this).data("type", "checked");
            $(this).children().first().prop("checked", true);
        },
        set_cc_unchecked: function () {
            $(this).removeData("type");
            $(this).children().first().prop("checked", false);
        },
        set_cc_even: function (boxId) {
            $(this).click(function () {
                var type = $(this).data("type");
                var ccObj = $(this).parent();
                if (type == undefined || type == "checked") {
                    $(this).set_cc_checked();
                    $(this).data("type", "checkedAll");
                    var string = $(this).attr("data-id") + ",";
                    $(this).next().find(".cc_li_odd").each(function () {
                        string += $(this).attr("data-id") + ",";
                        $(this).set_cc_checked();
                    });
                    ccObj.data("ids", string);
                } else {
                    $(this).set_cc_unchecked();
                    $(this).next().find(".cc_li_odd").each(function () {
                        $(this).set_cc_unchecked();
                    });
                    ccObj.removeData("ids");
                }
                $("#" + boxId).data("status", "changed");
            })
        },
        set_cc_odd: function (boxId) {
            $(this).click(function () {
                var type = $(this).data("type");
                var ulObj = $(this).parent();
                var evenObj = ulObj.parent().prev();
                var ccObj = evenObj.parent();
                if (type == undefined) {
                    $(this).set_cc_checked();
                } else {
                    $(this).set_cc_unchecked();
                }
                var isCheckedAll = true;
                var string = "";
                ulObj.children().each(function () {
                    if ($(this).data("type") == "checked") {
                        string += $(this).attr("data-id") + ",";
                    } else {
                        isCheckedAll = false;
                    }
                });
                if (string == "") {
                    ccObj.removeData("ids");
                    evenObj.set_cc_unchecked();
                } else {
                    var evenDataId = evenObj.attr("data-id");
                    evenObj.set_cc_checked()
                    if (isCheckedAll) {
                        evenObj.data("type", "checkedAll");
                    }
                    ccObj.data("ids", evenDataId + "," + string);
                }
                $("#" + boxId).data("status", "changed");
            })
        },
        output_cc_value: function (valId) {
            $(this).mouseleave(function () {
                var status = $(this).data("status");
                if (status == "changed") {
                    $(this).removeData("status");
                    var val = "";
                    $(this).children().each(function () {
                        var ids = $(this).data("ids");
                        if (ids != undefined) {
                            val += ids;
                        }
                    });
                    $("#" + valId).val(val.substr(0, val.length - 1));
                }
            })
        },
        set_cc_data: function (valArr) {
            var ccObj = $(this);
            var string = "";
            var isCheckedAll = true;
            ccObj.find("li").each(function () {
                var dataId = $(this).attr("data-id");
                var isChecked = false;
                if (dataId != undefined) {
                    for (var i = 0; i < valArr.length; i++) {
                        if (dataId == valArr[i]) {
                            $(this).set_cc_checked();
                            string += dataId + ",";
                            isChecked = true;
                            valArr.splice(i, 1);
                            break;
                        }
                    }
                    if (!isChecked) {
                        $(this).set_cc_unchecked();
                        isCheckedAll = false;
                    }
                }
            });
            if (string != "") {
                ccObj.data("ids", string);
                if (isCheckedAll) {
                    ccObj.children().first().data("type", "checkedAll");
                }
            } else {
                ccObj.removeData("ids");
            }
        },
        set_cc_val: function (valId) {
            var boxObj = $(this);
            var valObj = $("#" + valId);
            valObj.change(function () {
                var valArr = valObj.val().split(",");
                boxObj.children().each(function () {
                    $(this).set_cc_data(valArr)
                })
            })
        }
    });

    //dynamic_select
    $.fn.extend({
        /**
         * @param obj
         * obj.pageId           id
         * obj.valId            id
         * obj.textId           id
         * obj.msgId            id
         */
        dynamic_select_register: function (obj) {
            var textObj = $(this);
            textObj.data("obj", obj).dynamic_select_get_data(obj).click(function () {
                $(this).next().dynamic_select_show();
            }).focus(function () {
                $(this).next().dynamic_select_show();
            })
        },
        dynamic_select_show: function () {
            $(this).fadeIn();
            return this;
        },
        dynamic_select_hide: function () {
            $(this).fadeOut().children().first().dynamic_select_pre_deselect();
            return this;
        },
        dynamic_select_get_data: function (obj) {
            var textObj = $(this);
            $.ajax({
                url: "navigator/validator",
                type: "post",
                data: {
                    type: "select",
                    reqId: obj.pageId,
                    key: obj.key
                },
                datatype: "json",
                success: function (res) {
                    if (res.code == 0) {
                        textObj.dynamic_select_draw(obj, res.data);
                    } else if (res.code == -1) {
                        $("#" + obj.msgId).show_msg(res.data);
                    } else {
                        $.ero.showErrorMessage(res.code, res.data);
                    }
                },
                error: function (xhr, msg, obj) {
                    $.ero.showErrorMessage(-10);
                    $.ero.getAjaxErrorMessage(xhr, msg, obj);
                }
            });
            return this;
        },
        dynamic_select_draw: function (obj, data) {
            var textObj = $(this);
            var val = data.val;
            var text = data.text == undefined ? val : data.text;
            var lis = "";
            var liLength = val.length;
            for (var i = 0; i < liLength; i++) {
                lis += "<li data-val='" + val[i] + "' class='dynamic_select_li'>" + text[i] + "</li>";
            }
            textObj.after("<div class='dynamic_plugin_box' style='width:" + textObj.css("width") + "'><ul id='ss' class='dynamic_select_ul'>" + lis + "</ul></div>");
            textObj.next().dynamic_select_event(obj, liLength);
        },
        dynamic_select_event: function (obj, liLength) {
            var divObj = $(this);
            divObj.dynamic_select_li_click(obj).dynamic_select_blur(obj).mouseleave(function () {
                $(this).dynamic_select_hide();
            });
            var ulObj = divObj.children().first().data("len", liLength).dynamic_select_bind(obj).dynamic_select_search(obj).mouseenter(function () {
                $(this).dynamic_select_pre_deselect();
            });
            ulObj.data("padTop", parseInt(ulObj.css("padding-top").slice(0, -2)));
            ulObj.data("liHei", parseInt(ulObj.css("line-height").slice(0, -2)) + 1);
        },
        dynamic_select_li_click: function (obj) {
            var divObj = $(this);
            var ulObj = divObj.children().first();
            ulObj.children().each(function () {
                $(this).click(function () {
                    if (ulObj.data("index") != undefined) {
                        ulObj.dynamic_select_deselect(ulObj.data("index"));
                    }
                    $(this).addClass("dynamic_select_li_select");
                    ulObj.data("index", $(this).index());
                    $("#" + obj.textId).val($(this).text());
                    $("#" + obj.valId).val($(this).attr("data-val"));
                    divObj.dynamic_select_hide();
                })
            });
            return this;
        },
        dynamic_select_blur: function (obj) {
            var divObj = $(this);
            $("#" + obj.textId).blur(function () {
                divObj.dynamic_select_hide();
                if (divObj.children().first().data("index") == undefined) {
                    divObj.prev().val("");
                }
            });
            return this;
        },
        dynamic_select_deselect: function (index) {
            $(this).children().eq(index).removeClass("dynamic_select_li_select");
        },
        dynamic_select_bind: function (obj) {
            var ulObj = $(this);
            var textObj = $("#" + obj.textId);
            $("#" + obj.valId).change(function () {
                if (ulObj.data("index") != undefined) {
                    ulObj.dynamic_select_deselect(ulObj.data("index"));
                }
                var curVal = $(this).val();
                if (curVal != "") {
                    var isExist = false;
                    ulObj.children().each(function () {
                        if ($(this).attr("data-val") == curVal) {
                            $(this).addClass("dynamic_select_li_select");
                            ulObj.data("index", $(this).index());
                            textObj.val($(this).text());
                            isExist = true;
                            return false;
                        }
                    });
                    if (!isExist) {
                        $(this).val("");
                        textObj.val("");
                    }
                } else {
                    textObj.val("");
                }
            });
            return this;
        },
        dynamic_select_pre_select: function (pre, match) {
            $(this).data("pre", pre).children().eq(match == undefined ? pre : match[pre]).addClass("dynamic_select_li_select");
        },
        dynamic_select_pre_deselect: function () {
            var ulObj = $(this);
            var pre = ulObj.data("pre");
            if (pre != undefined) {
                var match = ulObj.data("match");
                ulObj.dynamic_select_deselect(match == undefined ? pre : match[pre]);
                ulObj.removeData("pre");
                var index = ulObj.data("index");
                if (index != undefined) {
                    ulObj.children().eq(index).addClass("dynamic_select_li_select");
                    ulObj.scrollTop(index < 3 ? 0 : ((index - 2) * (ulObj.data("liHei"))) + ulObj.data("padTop"));
                } else {

                }
            }
        },
        dynamic_select_search: function (obj) {
            var ulObj = $(this);
            var valObj = $("#" + obj.valId);
            var getPreIndex = function (pre, match) {
                return match == undefined ? pre : match[pre]
            };
            $("#" + obj.textId).keyup(function (e) {
                var eWhich = e.which;
                if (ulObj.data("len") == 0 || eWhich == 9 || eWhich == 16) {
                    return false;
                }
                if (eWhich == 27) {
                    ulObj.parent().dynamic_select_hide();
                    return false;
                }
                var match = ulObj.data("match");
                var pre = ulObj.data("pre");
                if (eWhich == 38 || eWhich == 40 || eWhich == 13) {
                    if (match != undefined && match.length == 0) {
                        return false;
                    }
                    var maxIndex = match == undefined ? ulObj.data("len") - 1 : match.length - 1;
                    var index = ulObj.data("index");
                    if (index != undefined && pre == undefined) {
                        pre = index;
                    } else {
                        pre = pre == undefined ? -1 : pre;
                    }
                    pre = pre == undefined ? -1 : pre;
                    switch (eWhich) {
                        case 38:
                            if (pre != -1) {
                                ulObj.dynamic_select_deselect(getPreIndex(pre, match));
                            }
                            if (--pre > -1) {
                                ulObj.dynamic_select_pre_select(pre, match);
                            } else {
                                pre = maxIndex;
                                ulObj.dynamic_select_pre_select(pre, match);
                            }
                            if ((pre < maxIndex - 3) && ((pre - 1) * ulObj.data("liHei") < ulObj.scrollTop())) {
                                ulObj.scrollTop(ulObj.scrollTop() - ulObj.data("liHei"));
                            }
                            if (pre >= maxIndex - 3) {
                                ulObj.scrollTop(ulObj.data("liHei") * (maxIndex - 5));
                            }
                            break;
                        case 40:
                            if (pre != -1) {
                                ulObj.dynamic_select_deselect(getPreIndex(pre, match));
                            }
                            if (++pre <= maxIndex) {
                                ulObj.dynamic_select_pre_select(pre, match);
                            } else {
                                pre = 0;
                                ulObj.dynamic_select_pre_select(pre, match);
                            }
                            if ((pre > 3) && ((pre - 5) * ulObj.data("liHei") > ulObj.scrollTop())) {
                                ulObj.scrollTop(ulObj.scrollTop() + ulObj.data("liHei"));
                            }
                            if (pre <= 3) {
                                ulObj.scrollTop(0);
                            }
                            break;
                        case 13:
                            if (pre != -1) {
                                ulObj.children().eq(getPreIndex(pre, match)).trigger("click");
                            }
                            break;
                        default:
                            return false;
                    }
                } else {
                    valObj.val("");
                    ulObj.removeData("index");
                    var curText = $(this).val();
                    if (curText == "" && match != undefined) {
                        ulObj.removeData("match").removeData("pre").children().each(function () {
                            $(this).removeAttr("style").removeClass("dynamic_select_li_select");
                        });
                    }
                    if (curText != "") {
                        curText = curText.toUpperCase();
                        var match = new Array();
                        ulObj.removeData("pre").children().each(function () {
                            if ($(this).text().indexOf(curText) != -1 || $(this).attr("data-val").toUpperCase().indexOf(curText) != -1) {
                                $(this).removeAttr("style").removeClass("dynamic_select_li_select");
                                match.push($(this).index());
                            } else {
                                $(this).css("display", "none").removeClass("dynamic_select_li_select");
                            }
                        });
                        ulObj.data("match", match);
                    }
                }
            });
            return this;
        },
        dynamic_select_update_data: function () {
            var textObj = $(this);
            textObj.next().remove();
            var obj = textObj.data("obj");
            textObj.dynamic_select_register(obj);
            textObj.next().children().first().dynamic_select_bind(obj);
        }
    });

    //file_uploader
    $.fn.extend({
        /**
         * @param obj
         * obj.pageId           id
         * obj.uploaderId       id
         * obj.triggerId        id
         * obj.valId            id
         * obj.msgId            id
         * obj.fileType         string
         * obj.fileMaxSize      number
         * obj.isMultiple       boolean
         */
        file_uploader_register: function (obj) {
            var triggerObj = $(this);
            var frameObj = $("#" + obj.uploaderId);
            obj.isMultiple = obj.isMultiple == undefined ? true : obj.isMultiple;
            var checkTypeAndSize = function () {
                var args = new Object();
                if (obj.fileMaxSize != undefined) {
                    switch (obj.fileMaxSize.slice(-1)) {
                        case "m":
                        case "M":
                            args.maxSize = parseInt(obj.fileMaxSize.slice(0, -1) * 1024 * 1024);
                            break;
                        case "k":
                        case "K":
                            args.maxSize = parseInt(obj.fileMaxSize.slice(0, -1) * 1024);
                            break;
                        default:
                            $.ero.showErrorMessage(-5);
                    }
                } else {
                    args.maxSize = "";
                }
                switch (obj.fileType) {
                    case "img":
                        args.fileType = "jpeg,jpg,png";
                        args.accept = "image/jpeg,image/jpg,image/png";
                        break;
                    case "xls":
                        args.fileType = "xls,xlsx";
                        args.accept = "application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                        break;
                    case undefined:
                        args.fileType = "";
                        args.accept = "";
                        break;
                    default:
                        $.ero.showErrorMessage(-5);
                }
                return args;
            };
            var args = checkTypeAndSize(obj);
            $("#" + obj.valId).file_upload_event_register(obj);
            triggerObj.click(function () {
                frameObj.contents().find("#uploader_reqId").val(obj.pageId).next().val(args.fileType).next().val(args.maxSize).next()
                    .prop("multiple", obj.isMultiple).attr("accept", args.accept).click();
                frameObj.load(function () {
                    $(this).file_upload_check_result(obj);
                });
            });
        },
        file_upload_check_result: function (obj) {
            var frameObj = $(this);
            var fileNamesObj = frameObj.contents().find("#upload_fileNames");
            var fileNames = fileNamesObj.text();
            var codeObj = fileNamesObj.next();
            var code = codeObj.text();
            var messageObj = codeObj.next();
            var message = messageObj.text();
            if (fileNames == "null" && code == "null") {
                $("#" + obj.msgId).show_msg("上传文件 格式错误 或 大小超限")
            } else {
                var valObj = $("#" + obj.valId);
                if (code != "null") {
                    $("#" + obj.msgId).show_msg(message);
                } else {
                    if (valObj.val() == "" || !obj.isMultiple) {
                        valObj.val(fileNames).trigger("change");
                    } else {
                        valObj.val(valObj.val() + "," + fileNames).data("names", fileNames).trigger("change");
                    }
                }
            }
            $(this).unbind("load");
        },
        file_upload_event_register: function (obj) {
            var valObj = $(this);
            if (obj.fileType == "img") {
                if (obj.isMultiple) {
                    valObj.file_upload_images_show();
                } else {
                    // valObj.file_upload_img_show();
                }
            }
        },
        file_upload_img_show: function () {

        },
        file_upload_img_event: function () {

        },
        file_upload_images_show: function () {
            var valObj = $(this);
            valObj.change(function () {
                var fileNames;
                if (valObj.data("names") != undefined) {
                    fileNames = valObj.data("names");
                    valObj.removeData("names");
                } else {
                    valObj.prevAll().remove();
                    fileNames = $(this).val();
                }
                if (fileNames != "") {
                    var fileNamesArr = fileNames.split(",");
                    valObj.before("<span><img class='fu_img' src='uploads/" + fileNamesArr[0] + "'></span>");
                    valObj.file_upload_images_event();
                    var len = fileNamesArr.length;
                    if (len > 1) {
                        var i = 1;
                        var t = setInterval(function () {
                            valObj.before("<span><img class='fu_img' src='uploads/" + fileNamesArr[i] + "'></span>");
                            valObj.file_upload_images_event();
                            if (++i >= len) {
                                clearInterval(t)
                            }
                        }, 100)
                    }
                }
            })
        },
        file_upload_images_event: function () {
            var valObj = $(this);
            var spanObj = valObj.prev();
            var imgObj = spanObj.children().first();
            spanObj.mouseenter(function () {
                var p = imgObj.position();
                imgObj.after("<span class='fu_img_box' style='width: " + imgObj.css("width") + ";top: " + p.top + "px;left: " + p.left + "px;'>删 除</span>");
                var boxObj = imgObj.next();
                boxObj.click(function () {
                    var fileNamesArr = valObj.val().split(",");
                    var index = spanObj.index();
                    fileNamesArr.splice(index, 1);
                    valObj.val(fileNamesArr.join(","));
                    $(this).parent().animate({opacity: '0', width: '0px'}, "800", function () {
                        $(this).remove()
                    });
                })
            });
            spanObj.mouseleave(function () {
                imgObj.next().stop().animate({opacity: '0', height: '0px'}, "800", function () {
                    $(this).remove()
                });
            })
        }
    });

    //date picker
    $.fn.extend({
        /**
         * @param obj
         * obj.valId            id
         * obj.shortDate        boolean
         */
        date_picker_register: function (obj) {
            var valObj = $(this);
            valObj.date_picker_parse_date_picked().trigger("change");
            valObj.date_picker_prepare_table(obj);
        },
        date_picker_parse_date_picked: function () {
            var valObj = $(this);
            valObj.click(function () {
                var valDate = $(this).val();
                var dateData = new Object();
                if (valDate == "" || new Date(valDate) == "Invalid Date") {
                    dateData.datePicked = new Date();
                    dateData.dateTarget = new Date();
                } else {
                    dateData.datePicked = new Date(valDate);
                    dateData.dateTarget = new Date(valDate);
                }
                valObj.data("dateData", dateData);
            }).keyup(function () {
                $(this).next().fadeOut();
            });
            return this;
        },
        date_picker_prepare_table: function (obj) {
            var valObj = $(this);
            var weekStr = "<tr class='date_picker_thead'><th>Sun.</th><th>Mon.</th><th>Tue.</th><th>Wed.</th><th>Thu.</th><th>Fri.</th><th>Sat.</th></tr>";
            var colGroupStr = "<colgroup><col class='date_picker_col'><col class='date_picker_col'><col class='date_picker_col'><col class='date_picker_col'>" +
                "<col class='date_picker_col'><col class='date_picker_col'><col class='date_picker_col'></colgroup>";
            var headStr = "<table class='date_picker_table' style='border-bottom: none'><thead class='date_picker_thead'><tr></tr></thead></table>";
            var bodyStr = "<table class='date_picker_table' style='border-top: none'>" + colGroupStr + "<thead class='date_picker_thead'>" + weekStr + "</thead><tbody class='date_picker_tbody'></tbody></table>";
            valObj.after("<div class='dynamic_plugin_box'>" + headStr + bodyStr + "</div>");
            var boxObj = valObj.next();
            var headTrObj = boxObj.find("tr").first();
            var tbodyObj = boxObj.find("tbody").first();
            valObj.date_picker_event_thead(obj, headTrObj, tbodyObj);
            valObj.date_picker_event_tbody(obj, headTrObj, tbodyObj);
            valObj.click(function () {
                // valObj.data("type", "date");
                valObj.date_picker_draw_table(obj, headTrObj, tbodyObj, "date");
                boxObj.fadeIn();
            }).blur(function () {
                if (!boxObj.data("prevent")) {
                    boxObj.fadeOut();
                }
            });
            boxObj.mouseenter(function () {
                $(this).data("prevent", true);
            }).mouseleave(function () {
                boxObj.fadeOut().data("prevent", false);
            })
        },
        date_picker_draw_table: function (obj, headTrObj, tbodyObj, type, dateData) {
            var valObj = $(this);
            dateData = dateData == undefined ? valObj.data("dateData") : dateData;
            headTrObj.date_picker_draw_thead(obj, dateData, type);
            valObj.data("type", type);
            if (type == "date") {
                valObj.date_picker_build_date_array(dateData);
                tbodyObj.date_picker_draw_tbody_date(dateData);
            } else if (type == "month") {
                tbodyObj.date_picker_draw_tbody_month(dateData);
            } else if (type == "year") {
                tbodyObj.date_picker_draw_tbody_year(dateData);
            } else if (type == "time") {
                tbodyObj.date_picker_draw_tbody_time(dateData);
            }
        },
        date_picker_build_date_array: function (dateData) {
            var valObj = $(this);
            var baseDateArray = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28];
            var dateArray = new Array();
            var dateTarget = dateData.dateTarget;
            var dateTargetDay = dateTarget.getDay();
            var date_0_0;
            var date_5_6;
            var monthPickedFirstDay = -(dateTarget.getDate() % 7) + dateTargetDay + 1;
            if (monthPickedFirstDay < 0) {
                monthPickedFirstDay += 7;
            }
            if (monthPickedFirstDay == 0) {
                date_0_0 = -6;
            } else {
                date_0_0 = 1 - monthPickedFirstDay;
            }
            date_5_6 = date_0_0 + 41;
            var firstDate = new Date(dateTarget.getFullYear(), dateTarget.getMonth(), dateTarget.getDate());
            firstDate.setDate(date_0_0);
            var lastDate = new Date(dateTarget.getFullYear(), dateTarget.getMonth(), dateTarget.getDate());
            lastDate.setDate(date_5_6);
            for (var i = 0; i <= (0 - date_0_0); i++) {
                dateArray.push(firstDate.getDate() + i);
            }
            dateArray = dateArray.concat(baseDateArray);
            for (var i = 1; i <= ((date_5_6 - lastDate.getDate()) - 28); i++) {
                dateArray.push(28 + i);
            }
            for (var i = 1; i <= lastDate.getDate(); i++) {
                dateArray.push(i);
            }
            dateData.dateArray = dateArray;
            valObj.data("dateData", dateData);
            return this;
        },
        date_picker_draw_thead: function (obj, dateData, type) {
            var headTrObj = $(this);
            var dateTarget = dateData.dateTarget;
            if (type == "date" || type == "time") {
                var tt = dateTarget.getFullYear() + " - " + (dateTarget.getMonth() + 1);
            } else if (type == "month") {
                var tt = dateTarget.getFullYear();
            } else if (type == "year") {
                var y1 = parseInt(dateTarget.getFullYear() / 10) * 10;
                var tt = (y1) + "-" + (y1 + 11);
            }
            if (obj.shortDate) {
                var cc = new Date().getHours() + "：" + new Date().getMinutes();
                var aa = "";
            } else {
                var cc = dateTarget.getHours() + "：" + dateTarget.getMinutes() + "：" + dateTarget.getSeconds();
                var aa = " class='date_picker_title'";
            }
            headTrObj.empty().append("<th class='date_picker_title' style='width: 14%'><</th><th colspan='2' class='date_picker_title' style='width: 28.4%'>" + tt
                + "</th><th class='date_picker_title' style='width: 14.1%'>></th><th" + aa + " colspan='3' style='width: 43%'>" + cc + "</th>");
            return this;
        },
        date_picker_draw_tbody_date: function (dateData) {
            var tbodyObj = $(this);
            tbodyObj.prev().show();
            var index = 0;
            var dateTarget = dateData.dateTarget;
            var datePicked = dateData.datePicked;
            var indexDate = new Date(dateTarget.toDateString());
            indexDate.setMonth(indexDate.getMonth() - 1);
            var isTargetMonth = false;
            var arr = dateData.dateArray;
            var dateToday = new Date();
            var domStr = "";
            var tdClass;
            for (var i = 0; i < 6; i++) {
                domStr += "<tr>";
                for (var j = 0; j < 7; j++) {
                    tdClass = "";
                    if (!isTargetMonth) {
                        tdClass += "date_picker_beyond_target_month ";
                    }
                    if (arr[index] == datePicked.getDate() && datePicked.getMonth() == indexDate.getMonth() && datePicked.getFullYear() == indexDate.getFullYear()) {
                        tdClass += "date_picker_date_picked ";
                    }
                    if (arr[index] == dateToday.getDate() && dateToday.getMonth() == indexDate.getMonth() && dateToday.getFullYear() == indexDate.getFullYear()) {
                        tdClass += "date_picker_date_today ";
                    }
                    domStr += "<td class='" + tdClass + "'>" + arr[index] + "</td>";
                    if (arr[index] > arr[index + 1]) {
                        isTargetMonth = !isTargetMonth;
                        indexDate.setMonth(indexDate.getMonth() + 1)
                    }
                    index++;
                }
                domStr += "</tr>";
            }
            tbodyObj.addClass("date_picker_tbody").empty().append(domStr);
            return this;
        },
        date_picker_draw_tbody_month: function (dateData) {
            var tbodyObj = $(this);
            tbodyObj.prev().hide();
            var arr = ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"];
            var datePicked = dateData.datePicked;
            var sameYear = dateData.dateTarget.getFullYear() == datePicked.getFullYear();
            var domStr = "";
            var index = 0;
            var tdClass;
            for (var i = 1; i < 4; i++) {
                domStr += "<tr class='date_picker_date_month'>";
                for (var j = 1; j < 5; j++) {
                    tdClass = "";
                    if (sameYear && index == datePicked.getMonth()) {
                        tdClass += "class='date_picker_date_picked'";
                    }
                    domStr += "<td data-v='" + index + "' " + tdClass + ">" + arr[index] + "</td>";
                    index++;
                }
                domStr += "</tr>";
            }
            tbodyObj.empty().append(domStr);
            return this;
        },
        date_picker_draw_tbody_year: function (dateData) {
            var tbodyObj = $(this);
            var targetYear = dateData.datePicked.getFullYear();
            var y0 = parseInt(dateData.dateTarget.getFullYear() / 10) * 10;
            var arr = new Array();
            for (var i = 0; i < 12; i++) {
                arr[i] = y0 + i;
            }
            var domStr = "";
            var index = 0;
            var tdClass;
            for (var i = 1; i < 4; i++) {
                domStr += "<tr class='date_picker_date_month'>";
                for (var j = 1; j < 5; j++) {
                    tdClass = "";
                    if (index == 0 || index == 11) {
                        tdClass += "date_picker_beyond_target_month "
                    }
                    if (targetYear == arr[index]) {
                        tdClass += "date_picker_date_picked";
                    }
                    domStr += "<td data-v='" + index + "' class='" + tdClass + "'>" + arr[index] + "</td>";
                    index++;
                }
                domStr += "</tr>";
            }
            tbodyObj.empty().append(domStr);
            return this;
        },
        date_picker_draw_tbody_time: function (dateData) {
            var tbodyObj = $(this);
            tbodyObj.prev().hide();
            var dateTarget = dateData.dateTarget;
            var domStr = "";
            var arr = ["<img class='date_picker_time date_picker_time_img' src='com-res/common/image/arrow/arrow_up.png'>",
                "<input type='text' class='date_picker_time' style='font-size: 18px;color: #333;text-indent: 0.7em'>", "<img class='date_picker_time' src='com-res/common/image/arrow/arrow_down.png'>"];
            var arr2 = ["", "：", ""];
            for (var i = 0; i < 3; i++) {
                domStr += "<tr class='date_picker_date_month' style='font-size: 20px'><td style='width: 50px'></td>";
                for (var j = 0; j < 2; j++) {
                    domStr += "<td>" + arr[i] + "</td><td style='width: 20px;'>" + arr2[i] + "</td>";
                }
                domStr += "<td>" + arr[i] + "</td><td style='width: 50px'></td></tr>";
            }
            tbodyObj.removeClass("date_picker_tbody").empty().append(domStr);
            var tt = [dateTarget.getHours(), dateTarget.getMinutes(), dateTarget.getSeconds()];
            var i = 0;
            tbodyObj.find("input").each(function () {
                $(this).val(tt[i++]);
            });
            return this;
        },
        date_picker_event_thead: function (obj, headTrObj, tbodyObj) {
            var valObj = $(this);
            headTrObj.click(function (e) {
                var index = $(e.target).index();
                var dateData = valObj.data("dateData");
                var type = valObj.data("type");
                switch (index) {
                    case 0:
                        if (type == "date") {
                            dateData.dateTarget.setMonth(dateData.dateTarget.getMonth() - 1);
                        } else if (type == "month") {
                            dateData.dateTarget.setFullYear(dateData.dateTarget.getFullYear() - 1);
                        } else if (type == "year") {
                            dateData.dateTarget.setFullYear(dateData.dateTarget.getFullYear() - 10);
                        } else if (type == "time") {
                            return false;
                        }
                        break;
                    case 2:
                        if (type == "date") {
                            dateData.dateTarget.setMonth(dateData.dateTarget.getMonth() + 1);
                        } else if (type == "month") {
                            dateData.dateTarget.setFullYear(dateData.dateTarget.getFullYear() + 1);
                        } else if (type == "year") {
                            dateData.dateTarget.setFullYear(dateData.dateTarget.getFullYear() + 10);
                        } else if (type == "time") {
                            return false;
                        }
                        break;
                    case 1:
                        if (type == "date") {
                            type = "month";
                        } else if (type == "month") {
                            type = "year";
                        } else if (type == "time") {
                            type = "date";
                        }
                        break;
                    case 3:
                        if (obj.shortDate) {
                            return false;
                        }
                        if (type == "time") {
                            type = "date";
                        } else {
                            type = "time";
                        }
                }
                valObj.date_picker_draw_table(obj, headTrObj, tbodyObj, type, dateData);
            })
        },
        date_picker_event_tbody: function (obj, headTrObj, tbodyObj) {
            var valObj = $(this);
            tbodyObj.click(function (e) {
                var type = valObj.data("type");
                var dateData = valObj.data("dateData");
                var dateTarget = dateData.dateTarget;
                if (type == "date") {
                    var date = $(e.target).text();
                    if ($(e.target).hasClass("date_picker_beyond_target_month")) {
                        dateTarget.setMonth(date > 15 ? dateTarget.getMonth() - 1 : dateTarget.getMonth() + 1);
                    }
                    dateTarget.setDate(date);
                    if (obj.shortDate) {
                        valObj.val(dateTarget.getFullYear() + "-" + (dateTarget.getMonth() + 1) + "-" + dateTarget.getDate()).next().fadeOut().data("prevent", false);
                    } else {
                        valObj.val(dateTarget.getFullYear() + "-" + (dateTarget.getMonth() + 1) + "-" + dateTarget.getDate() + " " + dateTarget.getHours() + ":" +
                            dateTarget.getMinutes() + ":" + dateTarget.getSeconds()).next().fadeOut().data("prevent", false);
                    }
                } else if (type == "month") {
                    dateTarget.setMonth($(e.target).attr("data-v"));
                    valObj.date_picker_draw_table(obj, headTrObj, tbodyObj, "date", dateData);
                } else if (type == "year") {
                    dateTarget.setFullYear($(e.target).text());
                    valObj.date_picker_draw_table(obj, headTrObj, tbodyObj, "month", dateData);
                } else if (type == "time") {
                    if ($(e.target).text() == "：") {
                        return false;
                    }
                    var o = $(e.target).parent();
                    var i = o.parent().index();
                    var j = o.index();
                    var reg1 = new RegExp("\^(((0|1)?[0-9])|20|21|22|23)$");
                    var reg2 = new RegExp("\^(0|1|2|3|4|5)?[0-9]$");
                    var ips = tbodyObj.find("input");
                    switch (j) {
                        case 1:
                            switch (i) {
                                case 1:
                                    $(e.target).blur(function () {
                                        $(this).val($(this).val().match(reg1) ? parseInt($(this).val()) : 0);
                                        dateTarget.setHours($(this).val());
                                        console.log(dateTarget.getHours())
                                    });
                                    break;
                                case 0:
                                case 2:
                                    var h = dateTarget.getHours() + (i == 0 ? 1 : -1);
                                    h = h == 24 ? 0 : h == -1 ? 23 : h;
                                    ips.first().val(h);
                                    dateTarget.setHours(h);
                                    console.log(dateTarget.getHours())
                            }
                            break;
                        case 3:
                            switch (i) {
                                case 1:
                                    $(e.target).blur(function () {
                                        $(this).val($(this).val().match(reg2) ? parseInt($(this).val()) : 0);
                                        dateTarget.setMinutes($(this).val());
                                        console.log(dateTarget.getMinutes())
                                    });
                                    break;
                                case 0:
                                case 2:
                                    var m = dateTarget.getMinutes() + (i == 0 ? 1 : -1);
                                    m = m == 60 ? 0 : m == -1 ? 59 : m;
                                    ips.eq(1).val(m);
                                    dateTarget.setMinutes(m);
                                    console.log(dateTarget.getMinutes())
                            }
                            break;
                        case 5:
                            switch (i) {
                                case 1:
                                    $(e.target).blur(function () {
                                        $(this).val($(this).val().match(reg2) ? parseInt($(this).val()) : 0);
                                        dateTarget.setSeconds($(this).val());
                                        console.log(dateTarget.getSeconds())
                                    });
                                    break;
                                case 0:
                                case 2:
                                    var m = dateTarget.getSeconds() + (i == 0 ? 1 : -1);
                                    m = m == 60 ? 0 : m == -1 ? 59 : m;
                                    ips.last().val(m);
                                    dateTarget.setSeconds(m);
                                    console.log(dateTarget.getSeconds())
                            }
                            break;
                        default:
                            return false;
                    }
                }
            })
        }
    });

    //input formatter
    $.fn.extend({
        //input number
        /**
         * @param carry             3|4
         * @param decimalLength
         */
        input_number_formatter: function (carry, decimalLength) {
            var valObj = $(this);
            var formatter = function (number) {
                var arr = new Array(), c = "";
                for (var i = 0; i < number.length; i++) {
                    c = number.charAt(i);
                    if (c != "," && c != "." && c != " ") {
                        arr.unshift(c);
                    }
                }
                var res = "";
                var z = 1;
                var decLen = decimalLength == undefined ? 0 : decimalLength;
                for (var i = 0; i < arr.length; i++) {
                    if (i == decLen && i > 0) {
                        res = ". " + res;
                    } else if (i == decLen + carry * z) {
                        res = ", " + res;
                        z++;
                    }
                    res = arr[i] + res;
                }
                return res;
            };
            valObj.keyup(function () {
                var val = valObj.val();
                if (val != "") {
                    valObj.val(formatter(val));
                }
            }).focus(function () {
                var val = valObj.val();
                if (val != "") {
                    valObj.val(formatter(val));
                }
            }).blur(function () {
                var val = valObj.val();
                if (val != "") {
                    var res = "", c = "";
                    for (var i = 0; i < val.length; i++) {
                        c = val.charAt(i);
                        if (c != "," && c != "." && c != " ") {
                            res += c;
                        }
                    }
                    res = res.replace(/^0+/, "");
                    valObj.val(res);
                }
            })
        },
        //input decimal
        input_decimal_formatter: function () {
            var valObj = $(this);
            valObj.focus(function () {
                var val = valObj.val();
                if (val == "") {
                    valObj.val("0.");
                }
            }).blur(function () {
                var val = valObj.val();
                if (val == "0.") {
                    valObj.val("");
                }
            })
        }
    });

})(jQuery);
