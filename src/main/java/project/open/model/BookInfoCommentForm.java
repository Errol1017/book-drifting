package project.open.model;

import common.Util.DateUtil;
import project.navigator.service.CacheManager;
import project.operation.entity.Comment;
import project.operation.entity.Reservation;
import project.operation.pojo.ReservationStatus;

/**
 * Created by Errol on 17/5/17.
 */
public class BookInfoCommentForm extends ClientListParent {

    private String content;
    private String time;

    public BookInfoCommentForm(Comment comment, CacheManager cacheManager) {
        super(cacheManager.getClientCache(comment.getClientId()));
        this.content = comment.getContent();
        this.time = DateUtil.date2String(comment.getCreateTime(), DateUtil.PATTERN_K);
    }

    public BookInfoCommentForm(Reservation r, CacheManager cacheManager) {
        super(cacheManager.getClientCache(r.getClientId()));
        this.content = "";
        if (r.getStatus().equals(ReservationStatus.RESERVE)){
//            this.content = "";
            this.time = DateUtil.date2String(r.getCreateTime(), DateUtil.PATTERN_K);
        } else {
            if (r.getStatus().equals(ReservationStatus.BORROW)) {
//                this.content ="借阅中";
                this.time = DateUtil.date2String(r.getBorrowedTime(), DateUtil.PATTERN_K) +" - 至今";
            }else {
//                this.content = "";
                this.time = DateUtil.date2String(r.getBorrowedTime(), DateUtil.PATTERN_K) +" - "+DateUtil.date2String(r.getExpireTime(), DateUtil.PATTERN_K);
            }
        }
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
