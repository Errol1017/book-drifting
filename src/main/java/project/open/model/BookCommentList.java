package project.open.model;

import common.Util.DateUtil;
import project.navigator.service.CacheManager;
import project.operation.entity.Comment;

/**
 * Created by Errol on 17/5/23.
 */
public class BookCommentList extends ClientListParent {

//    private String id;
    private String createTime;
    private String content;

    public BookCommentList(Comment comment, CacheManager cacheManager) {
        super(cacheManager.getClientCache(comment.getClientId()));
//        this.id = String.valueOf(comment.getId());
        this.createTime = DateUtil.date2String(comment.getCreateTime(), DateUtil.PATTERN_D);
        this.content = comment.getContent();
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
