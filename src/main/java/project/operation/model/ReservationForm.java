package project.operation.model;

import common.Util.DateUtil;
import project.navigator.service.CacheManager;
import project.operation.entity.Reservation;
import project.operation.pojo.OwnerType;
import project.operation.pojo.ReservationStatus;

/**
 * Created by Errol on 17/6/2.
 */
public class ReservationForm {

    private String id;
    private String bookId;
    private String clientId;
    private String bookName;
    private String holder;
    private String client;
    private String status;
    private String createTime;
    private String borrowedTime;
    private String recedeTime;
    private String expireTime;

    public ReservationForm(Reservation reservation, CacheManager cacheManager) {
        this.id = String.valueOf(reservation.getId());
        this.bookId = String.valueOf(reservation.getBookId());
        this.clientId = String.valueOf(reservation.getClientId());
        this.bookName = cacheManager.getBookCache(reservation.getBookId()).getName();
        if (reservation.getStatus().equals(ReservationStatus.RESERVE)) {
            this.holder = "";
            this.borrowedTime = "";
            this.expireTime = "";
        } else {
            if (reservation.getOwnerType().equals(OwnerType.AGENCY)) {
                this.holder = cacheManager.getAgencyCache((int) reservation.getOwnerId()).getName();
            } else {
                ClientCache hh = cacheManager.getClientCache(reservation.getOwnerId());
                this.holder = hh.getNickName() + "（" + hh.getName() + "）";
            }
            this.borrowedTime = DateUtil.date2String(reservation.getBorrowedTime(), DateUtil.PATTERN_A);
            this.expireTime = DateUtil.date2String(reservation.getExpireTime(), DateUtil.PATTERN_A);
        }
        ClientCache cc = cacheManager.getClientCache(reservation.getClientId());
        this.client = cc.getNickName() + "（" + cc.getName() +"）";
        this.status = reservation.getStatus().getName();
        this.createTime = DateUtil.date2String(reservation.getCreateTime(), DateUtil.PATTERN_A);
        if (reservation.getStatus().equals(ReservationStatus.RECEDE)) {
            this.recedeTime = DateUtil.date2String(reservation.getRecedeTime(), DateUtil.PATTERN_A);
        } else {
            this.recedeTime = "";
        }
    }

    public String getId() {
        return id;
    }

    public String getBookId() {
        return bookId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getHolder() {
        return holder;
    }

    public String getClient() {
        return client;
    }

    public String getStatus() {
        return status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getBorrowedTime() {
        return borrowedTime;
    }

    public String getRecedeTime() {
        return recedeTime;
    }

    public String getExpireTime() {
        return expireTime;
    }
}
