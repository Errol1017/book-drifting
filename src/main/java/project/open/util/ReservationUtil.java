package project.open.util;

import common.CRUD.service.ComService;
import common.ServerAdvice.util.LogUtil;
import project.navigator.service.CacheManager;
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
    public static void dealWidthBorrow(BookCache bookCache, ClientCache clientCache, ComService comService, CacheManager cacheManager) {
        long clientId;
        long ownerId;
        OwnerType holderType;
        if (bookCache.getScannerId() == bookCache.getHolderId()) {
            clientId = clientCache.getId();
            ownerId = bookCache.getScannerId();
        } else {
            clientId = bookCache.getScannerId();
            ownerId = clientCache.getId();
        }
        if (bookCache.getStatus().equals(BookStatus.IN_STOCK) && bookCache.getAgencyId() != -1) { //在架且由机构管理
            holderType = OwnerType.AGENCY;
        } else {
            holderType = OwnerType.INDIVIDUAL;
        }
        Reservation reservation = comService.getFirst(Reservation.class, "bookId=" + bookCache.getId() + " and clientId=" + clientId +
                " and status='" + ReservationStatus.RESERVE + "'", "id desc");
        if (reservation == null) {
            reservation = new Reservation(bookCache.getId(), clientId);
        }
        reservation.setBorrowedTime(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        reservation.setExpireTime(calendar.getTime());
        reservation.setOwnerType(holderType);
        reservation.setOwnerId(ownerId);
        reservation.setStatus(ReservationStatus.BORROW);
        comService.saveDetail(reservation);

        if (holderType.equals(OwnerType.INDIVIDUAL) && ownerId != bookCache.getOwnerId()) {
            Reservation end = comService.getFirst(Reservation.class, "bookId=" + bookCache.getId() + " and clientId=" + ownerId +
                    " and status='" + ReservationStatus.BORROW + "'", "id desc");
            if (end != null) {
                end.setRecedeTime(new Date());
                end.setStatus(ReservationStatus.RECEDE);
                comService.saveDetail(end);
            }
        }

        Book book = comService.getDetail(Book.class, bookCache.getId());
        book.setReservationId(reservation.getId());
        book.setStatus(BookStatus.BORROWED);
        comService.saveDetail(book);

        ClientCache borrower = cacheManager.getClientCache(clientId);
        int i = borrower.getBorrowingSum() + 1;
        if (i > 2) {
            LogUtil.debug("***********************************  debug  *******************************888");
            LogUtil.debug("某用户已借阅两本或以上图书，还是借阅成功了！");
            LogUtil.debug("该用户id为：" + clientId);
            LogUtil.debug("本次借阅记录id为：" + reservation.getId());
        }
        borrower.setBorrowingSum(i);
        comService.updateDetail(Client.class, clientId, "borrowingSum=" + i);

        if (holderType.equals(OwnerType.INDIVIDUAL) && bookCache.getStatus().equals(BookStatus.BORROWED)) {
            ClientCache holder = cacheManager.getClientCache(ownerId);
            int j = holder.getBorrowingSum() - 1;
            if (j < 0) {
                LogUtil.debug("***********************************  debug  *******************************888");
                LogUtil.debug("某用户已借阅图书数量少于0了！");
                LogUtil.debug("该用户id为：" + ownerId);
                LogUtil.debug("本次借阅记录id为：" + reservation.getId());
            }
            holder.setBorrowingSum(j);
            comService.updateDetail(Client.class, ownerId, "borrowingSum=" + j);
        }

        bookCache.setStatus(BookStatus.BORROWED);
        bookCache.setHolderId(clientId);
    }

    //归还图书时处理对应 reservation
    public static void dealWidthRecede(BookCache bookCache, BookStatus status, ComService comService, CacheManager cacheManager) {
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

        bookCache.setStatus(status);
        if (bookCache.getHolderId() != -1) {
            ClientCache holder = cacheManager.getClientCache(bookCache.getHolderId());
            int i = holder.getBorrowingSum() - 1;
            if (i < 0) {
                LogUtil.debug("***********************************  debug  *******************************888");
                LogUtil.debug("某用户还书后图书数量少于0了！");
                LogUtil.debug("该用户id为：" + holder);
            }
            holder.setBorrowingSum(i);
            comService.updateDetail(Client.class, holder.getId(), "borrowingSum=" + i);
            bookCache.setHolderId(-1);
        }

    }

}
