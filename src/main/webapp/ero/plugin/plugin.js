/**
 * Created by Errol on 2016/6/10.
 */

;(function ($) {

    //list table
    $.fn.list_table = function (param) {
        /**
         * @param param
         * param.reqId              id
         * param.tableId            id
         * param.tableObj           object      required
         * param.msgObj             object      default: $("#" + param.tableId + "_msg")】
         * param.perPageNum
         * param.data               object
         *      data.list
         *      data.totalNum
         *      data.totalPage
         *      data.curPageNum
         *      data.tarPageNum
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
         *      handle.reloadObj
         *      handle.data
         *      handle.edit
         *              edit.callback
         *      handle.delete
         *              delete.before
         *              delete.key      default 'id'
         *              delete.success
         *              delete.error
         * param.queryData          jsonStr
         */
        param.tableObj = $(this);
        init();
        getList();
        return {
            getList: function (p) {
                getList(p)
            },
            deselect: function () {
                deselect();
            }
        };
        function init() {
            initParam();
            drawHeader();
            drawFooter();
            registerFooterEvent();
            initHandle();
            initHandleEvent();
            registerBodyEvent();
            function initParam() {
                param.reqId = $.ero.reqIdForPlugin;
                param.tableId = param.tableObj.attr("id");
                if (param.msgObj == undefined) {
                    param.msgObj = $("#" + param.tableId + "_Msg")
                }
                param.data = {
                    tarPageNum: 1,
                    totalNum: 0,
                    totalPage: 0,
                    totalNumChanged: false,
                    totalPageChanged: false
                };
                if (param.perPageNum == undefined) {
                    param.perPageNum = 10;
                }
                param.queryData = '';
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
                } else {
                    param.body.bodyObj = tableObj.find("tbody").first();
                }
            }
            function drawFooter() {
                var tf = "<tfoot class='list_table_footer'><tr><td class='list_table_footer_td' colspan='" + param.header.colsNum + "'>匹配数据&nbsp;<span>0</span>&nbsp;条&nbsp;共&nbsp;<span>0</span>&nbsp;页" +
                    "<span class='list_table_footer_right'><button class='button_style_50_25' value='1'>首页</button><input type='button' class='button_style_x_25' value='1'/>" +
                    "<input type='button' class='button_style_x_25' value='2'/><input type='button' class='button_style_x_25' value='3'/>" +
                    "<input type='text' class='input_text_25 span50' placeholder='页数'/><button class='button_style_50_25' value='0'>尾页</button></span></td></tr></tfoot>";
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
                    secondButtonObj: i2,
                    thirdButtonObj: i3,
                    inputObj: i,
                    lastPageObj: b2
                }
            }
            function registerFooterEvent() {
                var i = 0;
                param.footer.rightObj.children().each(function () {
                    if (i++ != 4) {
                        $(this).click(function () {
                            param.data.tarPageNum = parseInt($(this).val());
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
                            param.footer.inputObj.val(num);
                            getList();
                        })
                    }
                })
            }
            function initHandle() {
                if (param.handle == undefined) {
                    param.handle = new Object()
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
                if (param.handle.reloadObj == undefined) {
                    param.handle.reloadObj = $("#" + param.tableId + "_Reload");
                }
            }
            function initHandleEvent() {
                if (param.handle.handleObj.length > 0) {
                    param.handle.handleObj.mouseleave(function () {
                        $(this).fadeOut();
                    });
                }
                if (param.handle.editObj.length > 0) {
                    param.handle.editObj.click(function () {
                        param.handle.edit.callback(param.handle.data);
                        param.handle.handleObj.fadeOut();
                    });
                }
                if (param.handle.deleteObj.length > 0) {
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
                }
                if (param.handle.reloadObj.length > 0) {
                    param.handle.reloadObj.click(function () {
                        getList(1);
                    });
                }
                function deleteData(success) {
                    $.ajax({
                        url: "navigator/validator",
                        type: "post",
                        data: {
                            type: "delete",
                            reqId: param.reqId,
                            listId: param.tableId,
                            dataId: param.handle.data[param.handle.delete.key == undefined ? 'id' : param.handle.delete.key]
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
            function registerBodyEvent() {
                if (param.handle.handleObj.length > 0) {
                    param.body.bodyObj.click(function (e) {
                        var trObj = $(e.target).parent();
                        trObj.addClass("lt_tr_slct").siblings().removeClass("lt_tr_slct");
                        param.handle.handleObj.hide().css("top", e.clientY + document.body.scrollTop + 1 + "px").css("left", e.clientX + 1 + "px").slideDown();
                        param.handle.data = param.body.data[trObj.index()];
                    })
                }
            }
        }
        function getList(p) {
            if (p != undefined) {
                if (typeof (p) == 'number') {
                    param.data.tarPageNum = p;
                } else if (typeof (p) == 'string') {
                    param.queryData = p;
                    param.data.tarPageNum = 1;
                } else {
                    param.data.tarPageNum = p.page;
                    param.queryData = p.query;
                }
            }
            $.ajax({
                url: "navigator/validator",
                type: "post",
                data: {
                    type: "list",
                    reqId: param.reqId,
                    listId: param.tableId,
                    tarPageNum: param.data.tarPageNum,
                    perPageNum: param.perPageNum,
                    query: param.queryData
                },
                datatype: "json",
                success: function (res) {
                    if (res.code == 0) {
                        param.data.list = res.data.list;
                        param.data.curPageNum = param.data.tarPageNum;
                        if (param.data.totalNum != res.data.total) {
                            param.data.totalNumChanged = true;
                            param.data.totalNum = res.data.total;
                            var n = Math.ceil(param.data.totalNum / param.perPageNum);
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
                param.handle.handleObj.fadeOut();
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
                                    footer.inputObj.hide();
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
            }
        }
        function deselect() {
            param.body.bodyObj.find("tr").removeClass("lt_tr_slct");
        }
    };

    //form detail
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
         // *      submit.before
         *      submit.after
         *      submit.success
         *      submit.error
         * param.handle           object
         *      handle.submitObj    default $("#" + param.formId + '_Submit')
         *      handle.unbindObj    default $("#" + param.formId + '_Unbind')
         */
        param.formId = $(this).attr("id");
        init();
        return {
            getForm: function (dataId) {
                getForm(dataId)
            },
            unbind: function (id) {
                unbind(id)
            }
        };
        function init() {
            initParam();
            initValidateEvent();
            initHandleEvent();
            function initParam() {
                param.reqId = $.ero.reqIdForPlugin;
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
                        if (param.gather != undefined && param.gather.before != undefined) {
                            param.gather.before();
                        }
                        param.data = new Object();
                        for (var i=0;i<param.keys.length;i++){
                            param.data[param.keys[i]] = param.inputObjs[i].val()
                        }
                        if (param.gather != undefined && param.gather.after != undefined) {
                            param.gather.after();
                        }
                    }
                    function send() {
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
        function unbind(id) {
            if (id != undefined && param.inputObjs[0].val() != id) {
                return false
            }
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

    //dynamic select
    $.fn.dynamic_select = function (param) {
        /**
         * @param param
         * param.reqId
         * param.valObj         required
         * param.subObj
         * param.compId         default param.valObj.attr("id")
         * param.boxObj         default $("#" + param.compId + "_box")
         * param.ulObj
         * param.liObjs
         * param.dynamic        boolean default true
         * param.writable       boolean default true
         * param.lines          number  default 7
         * param.data           array
         * param.index
         * param.match
         * param.matchIndex
         * param.css
         *      css.lineHeight
         *      css.paddingTop
         * param.callback       default valObj.trigger('change')
         * param.out
         */
        if (param == undefined) {
            param = {}
        }
        param.subObj = $(this);
        init();
        return {
            reset: function () {
                reset();
            }
        };
        function init() {
            initValObj();
            initData();
            function initValObj() {
                var id = param.subObj.attr("id");
                param.subObj.attr("id", id + '_Sub');
                param.subObj.before('<input type="hidden" id="' + id + '"/>');
                param.valObj = param.subObj.prev();
                if (param.compId == undefined) {
                    param.compId = id;
                }
            }
            function initData() {
                if (param.dynamic == undefined || param.dynamic) {
                    param.reqId = $.ero.reqIdForPlugin;
                    getData();
                } else {
                    if (param.boxObj == undefined) {
                        param.boxObj = param.subObj.next();
                    }
                    param.liObjs = param.boxObj.find("li");
                    setData();
                    getCss();
                    initEvent();
                }
                if (param.writable == undefined) {
                    param.writable = true;
                }
                if (param.callback == undefined) {
                    param.callback = function () {
                        param.valObj.trigger("change");
                    }
                }
                param.out = true;
            }
            function getData() {
                $.ajax({
                    url: "navigator/validator",
                    type: "post",
                    data: {
                        type: 'data',
                        reqId: param.reqId,
                        compId: param.compId
                    },
                    success: function (res) {
                        if (res.code == 0) {
                            param.data = res.data;
                            draw();
                        } else {
                            $.ero.showErrorMessage(res.code, res.data);
                        }
                    },
                    error: function (xhr, msg, obj) {
                        $.ero.showErrorMessage(-9);
                        $.ero.getAjaxErrorMessage(xhr, msg, obj);
                    }
                });
                function draw() {
                    var liStr = "";
                    for (var i = 0; i < param.data.length; i++) {
                        liStr += "<li class='dynamic_select_li'>" + param.data[i]['text'] + "</li>";
                    }
                    param.subObj.after("<div class='dynamic_plugin_box' style='width:" + param.subObj.css("width") + "'><ul class='dynamic_select_ul'>" + liStr + "</ul></div>");
                    param.boxObj = param.subObj.next();
                    param.liObjs = param.boxObj.find("li");
                    getCss();
                    initEvent();
                }
            }
            function setData() {
                param.data = new Array();
                param.liObjs.each(function () {
                    param.data.push({
                        val: $(this).attr("data-val"),
                        text: $(this).text()
                    })
                })
            }
            function getCss() {
                param.ulObj = param.boxObj.children().first();
                param.lineHeight = parseInt(param.ulObj.css("line-height").slice(0, -2)) + 1;
                param.paddingTop = parseInt(param.ulObj.css("padding-top").slice(0, -2));
            }
            function initEvent() {
                initHandleEvent();
                initBoxObjEvent();
                function initHandleEvent() {
                    param.subObj.click(function () {
                        boxIn();
                    });
                    if (param.writable) {
                        param.subObj.focus(function () {
                            boxIn();
                        }).keyup(function (e) {
                            param.boxObj.fadeIn();
                            if (param.out) {
                                param.out = false;
                            }
                            search(e);
                        }).blur(function () {
                            boxOut()
                        })
                    } else {
                        param.subObj.focus(function () {
                            boxIn();
                            param.subObj.blur();
                        });
                    }
                    param.boxObj.mouseleave(function () {
                        boxOut();
                    })
                }
                function initBoxObjEvent() {
                    param.liObjs.each(function () {
                        $(this).click(function () {
                            clearIndex();
                            param.index = $(this).index();
                            $(this).addClass("dynamic_select_li_select");
                            setVal();
                            setSub();
                            boxOut();
                        })
                    });
                }
                function boxIn() {
                    if (param.out) {
                        param.out = false;
                        var s = param.valObj.val();
                        if (s != '') {
                            select(param.index);
                        }
                        param.boxObj.fadeIn();
                        scroll(param.index);
                    }
                }
                function boxOut() {
                    if (!param.out) {
                        param.out = true;
                        if (param.valObj.val() == '') {
                            param.subObj.val('')
                        }
                        if (param.matchIndex != undefined) {
                            deselect(getIndex());
                            param.matchIndex = undefined;
                        }
                        param.boxObj.fadeOut(function () {
                            clearMatch();
                        });
                    }
                }
                function search(e) {
                    var eWhich = e.which;
                    if (eWhich == 9 || eWhich == 16 || param.data.length == 0) {
                        return false;
                    }
                    if (eWhich == 27) {
                        boxOut();
                        return false;
                    }
                    if (eWhich == 38 || eWhich == 40 || eWhich == 13) {
                        if (param.match != undefined && param.match.length == 0) {
                            return false;
                        }
                        var maxIndex = param.match == undefined ? param.data.length - 1 : param.match.length - 1;
                        if (param.matchIndex == undefined && param.index != undefined) {
                            param.matchIndex = param.index;
                        }
                        switch (eWhich) {
                            case 38:
                                if (param.matchIndex == undefined || param.matchIndex == 0) {
                                    param.matchIndex = maxIndex;
                                } else {
                                    deselect(getIndex());
                                    param.matchIndex--;
                                }
                                scroll(param.matchIndex);
                                select(getIndex());
                                break;
                            case 40:
                                if (param.matchIndex == undefined || param.matchIndex == maxIndex) {
                                    param.matchIndex = 0;
                                } else {
                                    deselect(getIndex());
                                    param.matchIndex++;
                                }
                                scroll(param.matchIndex);
                                select(getIndex());
                                break;
                            case 13:
                                if (param.matchIndex == undefined) {
                                    if (param.subObj.val() == '') {
                                        param.callback();
                                    }
                                    param.valObj.val('');
                                    param.index = undefined;
                                } else {
                                    param.index = getIndex();
                                    param.matchIndex = undefined;
                                    setVal();
                                    setSub();
                                }
                                param.subObj.blur();
                                break;
                            default:
                                return false;
                        }
                    } else {
                        param.valObj.val("");
                        clearIndex();
                        deselect(getIndex());
                        var curText = param.subObj.val();
                        if (curText == "") {
                            clearMatch();
                        } else {
                            curText = curText;
                            var i = 0;
                            param.match = new Array();
                            param.liObjs.each(function () {
                                if (param.data[i]['val'].toUpperCase().indexOf(curText.toUpperCase()) != -1 || param.data[i]['text'].indexOf(curText) != -1) {
                                    $(this).show();
                                    param.match.push($(this).index());
                                } else {
                                    $(this).hide();
                                }
                                i++;
                            });
                        }
                    }
                }
                function scroll(index) {
                    if (index != undefined) {
                        param.ulObj.scrollTop(index < 3 ? 0 : ((index - 2) * (param.lineHeight)) + param.paddingTop);
                    } else {
                        param.ulObj.scrollTop(0);
                    }
                }
                function clearMatch() {
                    if (param.match != undefined) {
                        param.match = undefined;
                        param.liObjs.each(function () {
                            $(this).show();
                        });
                    }
                }
                function getIndex() {
                    return param.match == undefined ? param.matchIndex : param.match[param.matchIndex];
                }
            }
        }
        function setVal() {
            param.valObj.val(param.data[param.index]['val']);
            param.callback();
        }
        function setSub() {
            param.subObj.val(param.data[param.index]['text']);
        }
        function select(index) {
            param.liObjs.eq(index).addClass("dynamic_select_li_select");
        }
        function deselect(index) {
            param.liObjs.eq(index).removeClass("dynamic_select_li_select");
        }
        function setIndex(valStr) {
            for (var i = 0; i < param.data.length; i++) {
                if (param.data[i]['val'] == valStr) {
                    param.index = i;
                    break;
                }
            }
        }
        function clearIndex() {
            if (param.index != undefined) {
                deselect(param.index);
                param.index = undefined;
            }
        }
        function reset() {
            clearIndex();
            var valStr = param.valObj.val();
            if (valStr == '') {
                param.subObj.val('');
            } else {
                setIndex(valStr);
                if (param.index == undefined) {
                    param.valObj.val('');
                    param.subObj.val('');
                } else {
                    setSub();
                }
            }
        }
    };

    //cascade checkbox
    $.fn.cascade_checkbox = function (param) {
        /**
         * @param param
         * param.reqId          id
         * param.valObj         required
         * param.compId         default param.valObj.attr("id")
         * param.boxObj         default $("#" + param.compId + "_box")
         * param.dynamic        boolean
         * param.data           array
         * param.callback       default valObj.trigger('change')
         */
        if (param == undefined) {
            param = {}
        }
        param.valObj = $(this);
        init();
        return {
            reset: function () {
                reset();
            }
        };
        function init() {
            param.reqId = $.ero.reqIdForPlugin;
            initData();
            function initData() {
                if (param.compId == undefined) {
                    param.compId = param.valObj.attr("id");
                }
                if (param.dynamic == undefined || param.dynamic) {
                    getData();
                } else {
                    if (param.boxObj == undefined) {
                        param.boxObj = $("#" + param.compId + "_box");
                    }
                    initEvent();
                }
                if (param.callback == undefined) {
                    param.callback = function () {
                        param.valObj.trigger("change");
                    }
                }
            }
            function getData() {
                $.ajax({
                    url: "navigator/validator",
                    type: "post",
                    data: {
                        type: 'data',
                        reqId: param.reqId,
                        compId: param.compId
                    },
                    success: function (res) {
                        if (res.code == 0) {
                            param.data = res.data;
                            draw();
                        } else {
                            $.ero.showErrorMessage(res.code, res.data);
                        }
                    },
                    error: function (xhr, msg, obj) {
                        $.ero.showErrorMessage(-9);
                        $.ero.getAjaxErrorMessage(xhr, msg, obj);
                    }
                });
                function draw() {
                    var domStr = '';
                    var obj;
                    for (var i = 0; i < param.data.length; i++) {
                        obj = param.data[i];
                        if (obj["level"] == 1) {
                            if (domStr != "") {
                                domStr += '</ul></li></ul>';
                            }
                            domStr += '<ul class="cascade_checkbox span140"><li class="cc_li_even" data-val="' + obj["val"] + '"><input type="checkbox" class="input_checkbox">' + obj["text"] + '</li><li><ul>';
                        } else if (obj["level"] == 2) {
                            domStr += '<li class="cc_li_odd" data-val="' + obj["val"] + '"><input type="checkbox" class="input_checkbox">' + obj["text"] + '</li>';
                        }
                    }
                    param.valObj.after('<div class="cascade_checkbox_box">' + domStr + '</div>');
                    param.boxObj = param.valObj.next();
                    initEvent();
                }
            }
            function initEvent() {
                param.boxObj.find(".cc_li_even").each(function () {
                    $(this).click(function () {
                        var type = $(this).data("type");
                        var ccObj = $(this).parent();
                        if (type == undefined || type == "checked") {
                            check($(this));
                            $(this).data("type", "checkedAll");
                            var string = $(this).attr("data-val") + ",";
                            $(this).next().find("li").each(function () {
                                string += $(this).attr("data-val") + ",";
                                check($(this));
                            });
                            ccObj.data("values", string);
                        } else {
                            uncheck($(this));
                            $(this).next().find("li").each(function () {
                                uncheck($(this));
                            });
                            ccObj.removeData("values");
                        }
                        param.boxObj.data("status", "changed");
                    })
                });
                param.boxObj.find(".cc_li_odd").each(function () {
                    $(this).click(function () {
                        var type = $(this).data("type");
                        var ulObj = $(this).parent();
                        var evenObj = ulObj.parent().prev();
                        var ccObj = evenObj.parent();
                        if (type == undefined) {
                            check($(this));
                        } else {
                            uncheck($(this));
                        }
                        var isCheckedAll = true;
                        var string = "";
                        ulObj.children().each(function () {
                            if ($(this).data("type") == "checked") {
                                string += $(this).attr("data-val") + ",";
                            } else {
                                isCheckedAll = false;
                            }
                        });
                        if (string == "") {
                            uncheck(evenObj);
                            ccObj.removeData("values");
                        } else {
                            check(evenObj);
                            if (isCheckedAll) {
                                evenObj.data("type", "checkedAll");
                            }
                            ccObj.data("values", evenObj.attr("data-val") + "," + string);
                        }
                        param.boxObj.data("status", "changed");
                    })
                });
                param.boxObj.mouseleave(function () {
                    if ($(this).data("status") == "changed") {
                        $(this).removeData("status");
                        var val = "";
                        $(this).children().each(function () {
                            var s = $(this).data("values");
                            if (s != undefined) {
                                val += s;
                            }
                        });
                        param.valObj.val(val.substr(0, val.length - 1));
                        param.callback();
                    }
                });
            }
        }
        function check(liObj) {
            liObj.data("type", "checked").children().first().prop("checked", true);
        }
        function uncheck(liObj) {
            liObj.removeData("type").children().first().prop("checked", false);
        }
        function reset() {
            var valArr = param.valObj.val().split(",");
            var i = 0;
            param.boxObj.children().each(function () {
                resetData($(this))
            });
            function resetData(ccObj) {
                var string = "";
                var isCheckedAll = true;
                ccObj.find("li").each(function () {
                    var val = $(this).attr("data-val");
                    if (val != undefined) {
                        if (val == valArr[i]) {
                            check($(this));
                            string += val + ",";
                            i++;
                        } else {
                            uncheck($(this));
                            isCheckedAll = false;
                        }
                    }
                });
                if (string != "") {
                    ccObj.data("values", string);
                    if (isCheckedAll) {
                        ccObj.children().first().data("type", "checkedAll");
                    }
                } else {
                    ccObj.removeData("values");
                }
            }
        }
    };

    //date picker
    $.fn.date_picker = function (param) {
        /**
         * @param param
         * param.subObj
         * param.valObj
         * param.shortDate      boolean default true
         * param.chinese        boolean default true
         * param.writable       boolean default true
         * param.arr
         * param.curDate        当前选定date，默认当天
         * param.tarDate        目标date，默认和curDate相同
         * param.boxObj
         * param.headObj        the control row
         * param.bodyObj
         * param.dateType       date/time/month/year
         * param.dateChanged
         * param.timeChanged
         * param.callback       default param.valObj.trigger("change")
         */
        if (param == undefined) {
            param = {}
        }
        param.subObj = $(this);
        init();
        return {
            reset: function () {
                reset();
            }
        };
        function init() {
            initValObj();
            initData();
            initBox();
            initHandleEvent();
            initHeadEvent();
            initBodyEvent();
            function initValObj() {
                var id = param.subObj.attr("id");
                param.subObj.attr("id", id + '_Sub');
                param.subObj.before('<input type="hidden" id="' + id + '"/>');
                param.valObj = param.subObj.prev();
            }
            function initData() {
                param.dateType = "date";
                if (param.shortDate == undefined) {
                    param.shortDate = true;
                }
                if (param.chinese == undefined) {
                    param.chinese = true;
                }
                if (param.chinese) {
                    param.arr = ['年', '月', '日', ' ', '时', '分', '秒']
                } else {
                    param.arr = ['-', '-', '', ' ', ':', ':', '']
                }
                if (param.writable == undefined) {
                    param.writable = true;
                }
                if (param.callback == undefined) {
                    param.callback = function () {
                        param.valObj.trigger("change");
                    }
                }
            }
            function initBox() {
                if (param.chinese) {
                    var weekStr = "<tr class='date_picker_thead'><th>日</th><th>一</th><th>二</th><th>三</th><th>四</th><th>五</th><th>六</th></tr>";
                } else {
                    var weekStr = "<tr class='date_picker_thead'><th>Sun.</th><th>Mon.</th><th>Tue.</th><th>Wed.</th><th>Thu.</th><th>Fri.</th><th>Sat.</th></tr>";
                }
                var colGroupStr = "<colgroup><col class='date_picker_col'><col class='date_picker_col'><col class='date_picker_col'><col class='date_picker_col'>" +
                    "<col class='date_picker_col'><col class='date_picker_col'><col class='date_picker_col'></colgroup>";
                var headStr = "<table class='date_picker_table' style='border-bottom: none'><thead class='date_picker_thead'><tr></tr></thead></table>";
                var bodyStr = "<table class='date_picker_table' style='border-top: none'>" + colGroupStr + "<thead class='date_picker_thead'>" + weekStr + "</thead><tbody class='date_picker_tbody'></tbody></table>";
                param.subObj.after("<div class='dynamic_plugin_box'>" + headStr + bodyStr + "</div>");
                param.boxObj = param.subObj.next();
                param.headObj = param.boxObj.find("tr").first();
                param.bodyObj = param.boxObj.find("tbody").first();
            }
            function initHandleEvent() {
                initSubObjEvent();
                initBoxObjEvent();
                function initSubObjEvent() {
                    param.subObj.click(function () {
                        boxIn();
                    });
                    if (param.writable) {
                        param.subObj.change(function () {
                            var s = $(this).val();
                            if (s != '') {
                                if (param.chinese) {
                                    s = s.replace(param.arr[0], '-');
                                    s = s.replace(param.arr[1], '-');
                                    s = s.replace(param.arr[2], '');
                                    s = s.replace(param.arr[4], ':');
                                    s = s.replace(param.arr[5], ':');
                                    s = s.replace(param.arr[6], ':');
                                }
                                if (new Date(s) == "Invalid Date") {
                                    s = '';
                                    param.valObj.val(s);
                                    param.subObj.val(s);
                                } else {
                                    param.valObj.val(new Date(s).getTime())
                                }
                            } else {
                                param.valObj.val('');
                            }
                        }).keyup(function (e) {
                            param.boxObj.fadeOut();
                            if (e.which == 13) {
                                param.subObj.trigger("change").blur();
                                param.callback();
                            }
                        }).blur(function () {
                            if (param.dateChanged == undefined || param.dateChanged) {
                                param.boxObj.fadeOut();
                            }
                        });
                    } else {
                        param.subObj.focus(function () {
                            boxIn();
                            param.subObj.blur();
                        })
                    }
                    function boxIn() {
                        setDate();
                        drawTable();
                        param.boxObj.fadeIn();
                        function setDate() {
                            var dateStr = param.valObj.val();
                            if (dateStr == "" || new Date(parseInt(dateStr)) == "Invalid Date") {
                                param.curDate = new Date();
                                param.tarDate = new Date();
                                param.subObj.val('');
                                param.valObj.val('');
                            } else {
                                param.curDate = new Date(parseInt(dateStr));
                                param.tarDate = new Date(parseInt(dateStr));
                            }
                            if (param.shortDate) {
                                param.curDate.setHours(0);
                                param.curDate.setMinutes(0);
                                param.curDate.setSeconds(0);
                                param.tarDate.setHours(0);
                                param.tarDate.setMinutes(0);
                                param.tarDate.setSeconds(0);
                            }
                            param.dateType = "date";
                        }
                    }
                }
                function initBoxObjEvent() {
                    param.boxObj.mouseenter(function () {
                        param.dateChanged = false;
                    }).mouseleave(function () {
                        if (param.dateType == "time" && param.timeChanged) {
                            setVal();
                        }
                        if (!param.shortDate && (param.dateChanged || param.timeChanged)) {
                            param.callback();
                        }
                        param.dateChanged = undefined;
                        param.timeChanged = undefined;
                        param.boxObj.fadeOut();
                    });
                }
            }
            function initHeadEvent() {
                param.headObj.click(function (e) {
                    var index = $(e.target).index();
                    switch (index) {
                        case 0:
                            if (param.dateType == "date") {
                                param.tarDate.setMonth(param.tarDate.getMonth() - 1);
                            } else if (param.dateType == "month") {
                                param.tarDate.setFullYear(param.tarDate.getFullYear() - 1);
                            } else if (param.dateType == "year") {
                                param.tarDate.setFullYear(param.tarDate.getFullYear() - 10);
                            } else if (param.dateType == "time") {
                                return false;
                            }
                            break;
                        case 2:
                            if (param.dateType == "date") {
                                param.tarDate.setMonth(param.tarDate.getMonth() + 1);
                            } else if (param.dateType == "month") {
                                param.tarDate.setFullYear(param.tarDate.getFullYear() + 1);
                            } else if (param.dateType == "year") {
                                param.tarDate.setFullYear(param.tarDate.getFullYear() + 10);
                            } else if (param.dateType == "time") {
                                return false;
                            }
                            break;
                        case 1:
                            if (param.dateType == "date") {
                                param.dateType = "month";
                            } else if (param.dateType == "month") {
                                param.dateType = "year";
                            } else if (param.dateType == "time") {
                                param.dateType = "date";
                            }
                            break;
                        case 3:
                            if (param.shortDate) {
                                return false;
                            }
                            if (param.dateType == "time") {
                                param.dateType = "date";
                                if (param.timeChanged) {
                                    setVal();
                                }
                            } else {
                                param.dateType = "time";
                            }
                    }
                    drawTable();
                })
            }
            function initBodyEvent() {
                param.bodyObj.click(function (e) {
                    var tarObj = $(e.target);
                    if (param.dateType == "date") {
                        var date = parseInt(tarObj.attr("data-v"));
                        if (tarObj.hasClass("date_picker_beyond_target_month")) {
                            param.tarDate.setMonth(parseInt(date) > 15 ? param.tarDate.getMonth() - 1 : param.tarDate.getMonth() + 1);
                        }
                        param.tarDate.setDate(date);
                        setVal();
                        if (param.shortDate) {
                            param.boxObj.fadeOut();
                            param.callback();
                        }
                    } else if (param.dateType == "month") {
                        param.tarDate.setMonth(parseInt(tarObj.attr("data-v")));
                        param.dateType = "date";
                        drawTable();
                    } else if (param.dateType == "year") {
                        param.tarDate.setFullYear(parseInt(tarObj.attr("data-v")));
                        param.dateType = "month";
                        drawTable();
                    }
                })
            }
            function setVal() {
                param.valObj.val(param.tarDate.getTime());
                setSub();
                param.dateChanged = true;
            }
            function drawTable() {
                drawHead();
                if (param.dateType == "date") {
                    drawDate();
                } else if (param.dateType == "month") {
                    drawMonth();
                } else if (param.dateType == "year") {
                    drawYear();
                } else if (param.dateType == "time") {
                    drawTime();
                }
                function drawHead() {
                    if (param.dateType == "date" || param.dateType == "time") {
                        var tt = param.tarDate.getFullYear() + " - " + (param.tarDate.getMonth() + 1);
                    } else if (param.dateType == "month") {
                        var tt = param.tarDate.getFullYear();
                    } else if (param.dateType == "year") {
                        var y1 = parseInt(param.tarDate.getFullYear() / 10) * 10;
                        var tt = (y1) + "-" + (y1 + 11);
                    }
                    if (param.shortDate) {
                        var cc = new Date().getHours() + "：" + new Date().getMinutes();
                        var aa = "";
                    } else {
                        var cc = param.tarDate.getHours() + "：" + param.tarDate.getMinutes() + "：" + param.tarDate.getSeconds();
                        var aa = " class='date_picker_title'";
                    }
                    param.headObj.empty().append("<th class='date_picker_title' style='width: 14%'><</th><th colspan='2' class='date_picker_title' style='width: 28.4%'>" + tt
                        + "</th><th class='date_picker_title' style='width: 14.1%'>></th><th" + aa + " colspan='3' style='width: 43%'>" + cc + "</th>");
                }
                function drawDate() {
                    setDateArray();
                    function setDateArray() {
                        var baseDateArray = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28];
                        var dateArray = new Array();
                        var date_0_0;
                        var date_5_6;
                        var FirstDateOfPickedMonth = -(param.tarDate.getDate() % 7) + param.tarDate.getDay() + 1;
                        if (FirstDateOfPickedMonth < 0) {
                            FirstDateOfPickedMonth += 7;
                        }
                        if (FirstDateOfPickedMonth == 0) {
                            date_0_0 = -6;
                        } else {
                            date_0_0 = 1 - FirstDateOfPickedMonth;
                        }
                        date_5_6 = date_0_0 + 41;
                        var firstDate = new Date(param.tarDate.getFullYear(), param.tarDate.getMonth(), param.tarDate.getDate());
                        firstDate.setDate(date_0_0);
                        var lastDate = new Date(param.tarDate.getFullYear(), param.tarDate.getMonth(), param.tarDate.getDate());
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
                        drawBody(dateArray);
                    }
                    function drawBody(dateArray) {
                        param.bodyObj.prev().show();
                        var indexDate = new Date(param.tarDate.toString());
                        indexDate.setMonth(indexDate.getMonth() - 1);
                        var index = 0;
                        var isTargetMonth = false;
                        var domStr = "";
                        var tdClass;
                        for (var i = 0; i < 6; i++) {
                            domStr += "<tr>";
                            for (var j = 0; j < 7; j++) {
                                tdClass = "";
                                if (!isTargetMonth) {
                                    tdClass += "date_picker_beyond_target_month ";
                                }
                                if (dateArray[index] == param.curDate.getDate() && param.curDate.getMonth() == indexDate.getMonth() && param.curDate.getFullYear() == indexDate.getFullYear()) {
                                    tdClass += "date_picker_date_cur";
                                }
                                domStr += "<td data-v='" + dateArray[index] + "' class='" + tdClass + "'>" + dateArray[index] + "</td>";
                                if (dateArray[index] > dateArray[index + 1]) {
                                    isTargetMonth = !isTargetMonth;
                                    indexDate.setMonth(indexDate.getMonth() + 1)
                                }
                                index++;
                            }
                            domStr += "</tr>";
                        }
                        param.bodyObj.addClass("date_picker_tbody").empty().append(domStr);
                    }
                }
                function drawMonth() {
                    param.bodyObj.prev().hide();
                    var arr = ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"];
                    var domStr = "";
                    var index = 0;
                    var tdClass;
                    for (var i = 1; i < 4; i++) {
                        domStr += "<tr class='date_picker_date_month'>";
                        for (var j = 1; j < 5; j++) {
                            tdClass = "";
                            if (index == param.curDate.getMonth() && param.tarDate.getFullYear() == param.curDate.getFullYear()) {
                                tdClass += "date_picker_date_cur";
                            }
                            domStr += "<td data-v='" + index + "' class='" + tdClass + "'>" + arr[index] + "</td>";
                            index++;
                        }
                        domStr += "</tr>";
                    }
                    param.bodyObj.empty().append(domStr);
                }
                function drawYear() {
                    var y0 = parseInt(param.tarDate.getFullYear() / 10) * 10;
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
                            if (arr[index] == param.curDate.getFullYear()) {
                                tdClass += "date_picker_date_cur";
                            }
                            domStr += "<td data-v='" + arr[index] + "' class='" + tdClass + "'>" + arr[index] + "</td>";
                            index++;
                        }
                        domStr += "</tr>";
                    }
                    param.bodyObj.empty().append(domStr);
                }
                function drawTime() {
                    param.bodyObj.prev().hide();
                    var domStr = "";
                    var arr = ["<img class='date_picker_time_img' src='./com-res/common/img/arrow/arrow_up.png'>",
                        "<input type='text' class='date_picker_time_input'>",
                        "<img class='date_picker_time_img' src='./com-res/common/img/arrow/arrow_down.png'>"];
                    var arr2 = ["", "：", ""];
                    for (var i = 0; i < 3; i++) {
                        domStr += "<tr><td style='width: 50px'>&nbsp;</td>";
                        for (var j = 0; j < 2; j++) {
                            domStr += "<td>" + arr[i] + "</td><td style='width: 20px;'>" + arr2[i] + "</td>";
                        }
                        domStr += "<td>" + arr[i] + "</td><td style='width: 50px'></td></tr>";
                    }
                    param.bodyObj.removeClass("date_picker_tbody").empty().append(domStr);
                    var tt = [param.tarDate.getHours(), param.tarDate.getMinutes(), param.tarDate.getSeconds()];
                    var i = 0;
                    param.bodyObj.find("input").each(function () {
                        $(this).val(tt[i++]);
                    });
                    registerTimeBoxEvent();
                    function registerTimeBoxEvent() {
                        var reg1 = new RegExp("\^(((0|1)?[0-9])|20|21|22|23)$");
                        var reg2 = new RegExp("\^(0|1|2|3|4|5)?[0-9]$");
                        var imgs = param.bodyObj.find("img");
                        var inputs = param.bodyObj.find("input");
                        imgs.first().click(function () {
                            var h = param.tarDate.getHours() + 1;
                            param.tarDate.setHours(h);
                            h = h == 24 ? 0 : h == -1 ? 23 : h;
                            inputs.first().val(h);
                            param.timeChanged = true;
                        });
                        imgs.eq(1).click(function () {
                            var m = param.tarDate.getMinutes() + 1;
                            param.tarDate.setMinutes(m);
                            m = m == 60 ? 0 : m == -1 ? 59 : m;
                            inputs.eq(1).val(m);
                            inputs.first().val(param.tarDate.getHours());
                            param.timeChanged = true;
                        });
                        imgs.eq(2).click(function () {
                            var m = param.tarDate.getSeconds() + 1;
                            param.tarDate.setSeconds(m);
                            m = m == 60 ? 0 : m == -1 ? 59 : m;
                            inputs.last().val(m);
                            inputs.eq(1).val(param.tarDate.getMinutes());
                            param.timeChanged = true;
                        });
                        imgs.eq(3).click(function () {
                            var h = param.tarDate.getHours() - 1;
                            param.tarDate.setHours(h);
                            h = h == 24 ? 0 : h == -1 ? 23 : h;
                            inputs.first().val(h);
                            param.timeChanged = true;
                        });
                        imgs.eq(4).click(function () {
                            var m = param.tarDate.getMinutes() - 1;
                            param.tarDate.setMinutes(m);
                            m = m == 60 ? 0 : m == -1 ? 59 : m;
                            inputs.eq(1).val(m);
                            inputs.first().val(param.tarDate.getHours());
                            param.timeChanged = true;
                        });
                        imgs.last().click(function () {
                            var m = param.tarDate.getSeconds() - 1;
                            param.tarDate.setSeconds(m);
                            m = m == 60 ? 0 : m == -1 ? 59 : m;
                            inputs.last().val(m);
                            inputs.eq(1).val(param.tarDate.getMinutes());
                            param.timeChanged = true;
                        });
                        inputs.first().change(function () {
                            console.log("blur")
                            $(this).val($(this).val().match(reg1) ? parseInt($(this).val()) : 0).blur();
                            param.tarDate.setHours($(this).val());
                            param.timeChanged = true;
                        });
                        inputs.eq(1).change(function () {
                            $(this).val($(this).val().match(reg2) ? parseInt($(this).val()) : 0).blur();
                            param.tarDate.setMinutes($(this).val());
                            param.timeChanged = true;
                        });
                        inputs.last().change(function () {
                            $(this).val($(this).val().match(reg2) ? parseInt($(this).val()) : 0).blur();
                            param.tarDate.setSeconds($(this).val());
                            param.timeChanged = true;
                        });
                    }
                }
            }
        }
        function setSub() {
            if (param.shortDate) {
                param.subObj.val(param.tarDate.getFullYear() + param.arr[0] + (param.tarDate.getMonth() + 1) + param.arr[1] + param.tarDate.getDate() + param.arr[2]);
            } else {
                param.subObj.val(param.tarDate.getFullYear() + param.arr[0] + (param.tarDate.getMonth() + 1) + param.arr[1] + param.tarDate.getDate() + param.arr[2] + param.arr[3]
                    + param.tarDate.getHours() + param.arr[4] + param.tarDate.getMinutes() + param.arr[5] + param.tarDate.getSeconds() + param.arr[6]);
            }
        }
        function reset() {
            var s = param.valObj.val();
            if (s == '') {
                param.subObj.val('');
            } else {
                if (new Date(parseInt(s)) == "Invalid Date") {
                    param.valObj.val('');
                    param.subObj.val('');
                } else {
                    param.tarDate = new Date(parseInt(s));
                    setSub();
                }
            }
        }
    };

    //query manager
    $.fn.query_manager = function (param) {
        /**
         * @param param
         * param.prefix
         * param.manually       default true
         * param.keys
         * param.ids
         * param.keyObjs
         * param.queryData
         * param.callback
         * param.handle
         *      handle.submitObj
         *      handle.submitCall
         *      handle.resetObj
         *      handle.resetCall
         */
        param.prefix = $(this).attr("id") + '_';
        init();
        return {
        };
        function init() {
            initData();
            initQueryObj();
            initEvent();
            initHandle();
            function initData() {
                param.queryData = new Object();
                param.ids = new Array();
                param.keyObjs = new Array();
                if (param.manually == undefined) {
                    param.manually = true;
                }
                if (param.manually) {
                    param.cc = function () {}
                } else {
                    param.cc = param.callback;
                }
                if (param.handle == undefined) {
                    param.handle = new Object();
                }
            }
            function initQueryObj() {
                for (var i = 0; i < param.keys.length; i++) {
                    var k = param.prefix + param.keys[i];
                    param.keyObjs[i] = $("#" + k);
                    param.queryData[param.keys[i]] = param.keyObjs[i].val();
                    param.ids[i] = k;
                }
            }
            function initEvent() {
                for (var i = 0; i < param.keyObjs.length; i++) {
                    param.keyObjs[i].change(function () {
                        param.queryData[param.keys[$.indexOf(param.ids, $(this).attr("id"))]] = $(this).val();
                        param.cc(JSON.stringify(param.queryData));
                    })
                }
            }
            function initHandle() {
                initSubmitEvent();
                initResetEvent();
                function initSubmitEvent() {
                    if (param.manually) {
                        if (param.handle.submitObj == undefined) {
                            param.handle.submitObj = $("#" + param.prefix + "Submit");
                        }
                        param.handle.submitObj.click(function () {
                            param.callback(JSON.stringify(param.queryData));
                            if (param.handle.submitCall != undefined) {
                                param.handle.submitCall();
                            }
                        })
                    }
                }
                function initResetEvent() {
                    if (param.handle.resetObj == undefined) {
                        param.handle.resetObj = $("#" + param.prefix + "Reset");
                    }
                    param.handle.resetObj.click(function () {
                        for (var i = 0; i < param.keyObjs.length; i++) {
                            param.queryData[param.keys[i]] = "";
                            param.keyObjs[i].val('');
                        }
                        if (param.handle.resetCall != undefined) {
                            param.handle.resetCall();
                        }
                    })
                }
            }
        }
    };

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
