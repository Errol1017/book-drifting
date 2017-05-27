package project.open.model;

import common.CRUD.service.ComService;
import common.Util.DateUtil;
import project.operation.entity.Book;
import project.operation.entity.Reservation;

/**
 * Created by Errol on 17/5/19.
 */
public class BookListRecede extends BookListParent {

    private String period;

    public BookListRecede(Book book, ComService comService, Reservation reservation) {
        super(book, comService);
        this.period = DateUtil.date2String(reservation.getBorrowedTime(), DateUtil.PATTERN_K)+" - "+DateUtil.date2String(reservation.getExpireTime(), DateUtil.PATTERN_K);
    }

    public String getPeriod() {
        return period;
    }
}
