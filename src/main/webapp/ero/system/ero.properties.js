/**
 * Created by Errol on 2016/9/28.
 */

jQuery(function () {

    /**
     * dashboard        插件级别
     * content          系统级别
     * page             页面级别
     *
     * page必须是名称为对应导航栏id的html页面
     * page中最外层div的id必须是对应导航栏id+"Page"的格式
     */

    /**
     * 系统运行所必须的元素id，不允许修改
     * s_ul             导航栏父元素
     * c_title          内容页标题
     * c_container      异步加载页面的父元素
     * body_content     页面content部分
     */
    window.SidebarBoxObj = $("#s_ul");
    window.PageTitleObj = $("#c_title");
    window.PageContainerObj = $("#c_container");
    window.BodyContentObj = $("#body_content");

    /**
     * 页面上显示错误信息和提示信息的元素id
     * 错误信息id默认为 sidebar中的"s_msg"
     * 提示信息id默认为 content中的"c_msg"
     */
    window.ErrorMessageObj = $("#s_msg");
    window.PromptMessageId = "s_msg";
    /**
     * 异步加载页面数量的最大值
     */
    window.LoadedPagesMaxLength = 10;
    /**
     * jQuery动画的速度，可选值为
     * '','slow','fast',或毫秒数
     */
    window.SlideToggleSpeed = "";
    window.FadeToggleSpeed = "";

});