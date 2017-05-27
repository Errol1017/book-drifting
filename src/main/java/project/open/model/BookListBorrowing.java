package project.open.model;

import common.CRUD.service.ComService;
import common.Util.DateUtil;
import project.operation.entity.Book;
import project.operation.entity.Reservation;

/**
 * Created by Errol on 17/5/19.
 */
public class BookListBorrowing extends BookListParent {

    private String expire;

    public BookListBorrowing(Book book, ComService comService, Reservation reservation) {
        super(book, comService);
        this.expire = DateUtil.date2String(reservation.getExpireTime(), DateUtil.PATTERN_D);
    }

    public String getExpire() {
        return expire;
    }
}
