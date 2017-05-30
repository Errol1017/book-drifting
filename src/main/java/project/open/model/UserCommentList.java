package project.open.model;

import common.Util.DateUtil;
import project.operation.entity.Book;
import project.operation.entity.Comment;

/**
 * Created by Errol on 17/5/24.
 */
public class UserCommentList extends BookListParent {

    private String content;
    private String time;

    public UserCommentList(Comment comment, Book book) {
        super(book);
        this.content = comment.getContent();
        this.time = DateUtil.date2String(comment.getCreateTime(), DateUtil.PATTERN_D);
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
