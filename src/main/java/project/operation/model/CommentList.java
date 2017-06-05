package project.operation.model;

import common.Util.DateUtil;
import project.navigator.service.CacheManager;
import project.operation.entity.Comment;

/**
 * Created by Errol on 17/6/2.
 */
public class CommentList {

    private String id;
    private String bookId;
    private String bookName;
    private String clientId;
    private String clientNickname;
    private String content;
    private String createTime;

    public CommentList(Comment comment, CacheManager cacheManager) {
        this.id = String.valueOf(comment.getId());
        this.bookId = String.valueOf(comment.getBookId());
        this.bookName = cacheManager.getBookCache(comment.getBookId()).getName();
        this.clientId = String.valueOf(comment.getClientId());
        this.clientNickname = cacheManager.getClientCache(comment.getClientId()).getNickName();
        this.content = comment.getContent().length() > 50 ? (comment.getContent().substring(0, 50) + "…") : comment.getContent();
        this.createTime = DateUtil.date2String(comment.getCreateTime(), DateUtil.PATTERN_C);
    }

    public String getId() {
        return id;
    }

    public String getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientNickname() {
        return clientNickname;
    }

    public String getContent() {
        return content;
    }

    public String getCreateTime() {
        return createTime;
    }
}
