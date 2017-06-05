package project.operation.model;

import common.Util.DateUtil;
import project.navigator.service.CacheManager;
import project.operation.entity.Reservation;
import project.operation.pojo.OwnerType;
import project.operation.pojo.ReservationStatus;

/**
 * Created by Errol on 17/6/2.
 */
public class ReservationList {

    private String id;
    private String bookId;
    private String clientId;
    private String bookName;
    private String holder;
    private String client;
    private String status;
    private String createTime;
//    private String borrowedTime;
//    private String recedeTime;
//    private String expireTime;

    public ReservationList(Reservation reservation, CacheManager cacheManager) {
        this.id = String.valueOf(reservation.getId());
        this.bookId = String.valueOf(reservation.getBookId());
        this.clientId = String.valueOf(reservation.getClientId());
        this.bookName = cacheManager.getBookCache(reservation.getBookId()).getName();
        if (reservation.getStatus().equals(ReservationStatus.RESERVE)){
            this.holder = "";
        }else {
            this.holder = reservation.getOwnerType().equals(OwnerType.AGENCY) ?
                    cacheManager.getAgencyCache((int)reservation.getOwnerId()).getName():cacheManager.getClientCache(reservation.getOwnerId()).getNickName();
        }
        this.client = cacheManager.getClientCache(reservation.getClientId()).getNickName();
        this.status = reservation.getStatus().getName();
        this.createTime = DateUtil.date2String(reservation.getCreateTime(), DateUtil.PATTERN_C);
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
}
