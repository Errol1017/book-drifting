package project.open.model;

import common.Util.DateUtil;
import project.operation.entity.Book;
import project.operation.entity.Reservation;

/**
 * Created by Errol on 17/5/19.
 */
public class BookListBorrowing extends BookListParent {

    private String rid;
    private String expire;
    private String status;
    private String renew;

    public BookListBorrowing(Book book, Reservation reservation) {
        super(book);
        this.rid = String.valueOf(reservation.getId());
        this.expire = DateUtil.date2String(reservation.getExpireTime(), DateUtil.PATTERN_D);
        if (reservation.getRenew() == 0) {
            this.renew = "have";
        } else {
            this.renew = "able";
        }
        long now = System.currentTimeMillis();
        long expire = reservation.getExpireTime().getTime();
        long per = 86400000;
        if (now >= expire) {
            this.status = "已过期";
            if (this.renew.equals("able")) {
                this.renew = "unable";
            }
        } else {
            int d =(int) Math.ceil((expire - now) * 1.0 / per);
            if (d < 8 || reservation.getRenew() == 0) {
                if (d == 1) {
                    this.status = "明天";
                } else {
                    this.status = "余 " + d + "天";
                }
            } else {
                this.status = "";
            }
        }
    }

    public String getRid() {
        return rid;
    }

    public String getExpire() {
        return expire;
    }

    public String getStatus() {
        return status;
    }

    public String getRenew() {
        return renew;
    }
}
