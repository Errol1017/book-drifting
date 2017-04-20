package project.navigator.pojo;

/**
 * Created by Errol on 2016/10/2.
 */
public enum Types {

    page,//获取页面，参数reqId

    data,//获取页面初始化数据，参数reqId

    list,//获取列表数据，参数reqId，listId，pageNum，perPageNum

    form,//获取一条数据，参数reqId，formId，dataId

    submit,//提交一条数据，参数reqI，formId，data

    delete,//删除一条数据，参数reqId，listId，dataId

    select,//加载动态选择框数据，参数reqId，key

    upload,//上传文件，参数reqId，fileType

    ;

    Types(){

    }

}
