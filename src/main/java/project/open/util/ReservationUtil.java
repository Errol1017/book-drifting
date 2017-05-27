package project.open.util;

import common.CRUD.service.ComService;
import project.operation.entity.Book;
import project.operation.entity.Client;
import project.operation.entity.Reservation;
import project.operation.model.BookCache;
import project.operation.model.ClientCache;
import project.operation.pojo.BookStatus;
import project.operation.pojo.OwnerType;
import project.operation.pojo.ReservationStatus;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Errol on 17/5/22.
 */
public class ReservationUtil {

    //现在借书建立 reservation
    //默认第一次扫描为借书的用户
    public static void dealWidthBorrow(BookCache bookCache, ClientCache clientCache, ComService comService) {
        long clientId;
        long ownerId;
        OwnerType type;
        if (bookCache.getScannerId()==bookCache.getHolderId()){
            clientId = clientCache.getId();
            ownerId = bookCache.getScannerId();
        }else {
            clientId = bookCache.getScannerId();
            ownerId = clientCache.getId();
        }
        if (bookCache.getStatus().equals(BookStatus.IN_STOCK) && bookCache.getAgencyId()!=-1){ //在架且由机构管理
            type = OwnerType.AGENCY;
        }else {
            type=OwnerType.INDIVIDUAL;
        }
        Reservation reservation = comService.getFirst(Reservation.class, "bookId=" + bookCache.getId() + " and clientId=" + clientId +
                " and status='" + ReservationStatus.RESERVE + "'", "id desc");
        if (reservation==null){
            reservation = new Reservation(bookCache.getId(), clientId);
        }
        reservation.setBorrowedTime(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        reservation.setExpireTime(calendar.getTime());
        reservation.setOwnerType(type);
        reservation.setOwnerId(ownerId);
        reservation.setStatus(ReservationStatus.BORROW);
        comService.saveDetail(reservation);
        Book book = comService.getDetail(Book.class, bookCache.getId());
        book.setReservationId(reservation.getId());
        book.setStatus(BookStatus.BORROWED);
        comService.saveDetail(book);
        bookCache.setStatus(BookStatus.BORROWED);
    }

    //归还图书时处理对应 reservation
    public static void dealWidthRecede(BookCache bookCache, BookStatus status, ComService comService) {
        Book book = comService.getDetail(Book.class, bookCache.getId());
        if (book.getReservationId() != -1) {
            Reservation reservation = comService.getDetail(Reservation.class, book.getReservationId());
            reservation.setRecedeTime(new Date());
            reservation.setStatus(ReservationStatus.RECEDE);
            comService.saveDetail(reservation);
            book.setReservationId(-1);
        }
        book.setStatus(status);
        comService.saveDetail(book);
    }

}
