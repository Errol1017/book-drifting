package project.operation.model;

import common.Util.DateUtil;
import project.navigator.service.CacheManager;
import project.operation.entity.Comment;
import project.operation.model.ClientCache;

/**
 * Created by Errol on 17/6/2.
 */
public class CommentForm {

    private String id;
    private String bookId;
    private String bookName;
    private String clientId;
    private String clientNickname;
    private String content;
    private String createTime;

    public CommentForm(Comment comment, CacheManager cacheManager) {
        this.id = String.valueOf(comment.getId());
        this.bookId = String.valueOf(comment.getBookId());
        this.bookName = cacheManager.getBookCache(comment.getBookId()).getName();
        this.clientId = String.valueOf(comment.getClientId());
        ClientCache cc = cacheManager.getClientCache(comment.getClientId());
        this.clientNickname = cc.getNickName() + "（" + cc.getName() + "）";
        this.content = comment.getContent();
        this.createTime = DateUtil.date2String(comment.getCreateTime(), DateUtil.PATTERN_A);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientNickname() {
        return clientNickname;
    }

    public void setClientNickname(String clientNickname) {
        this.clientNickname = clientNickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
