package project.open.model;

import common.Util.DateUtil;
import project.navigator.service.CacheManager;
import project.operation.entity.Client;
import project.operation.entity.Reservation;
import project.operation.pojo.ReservationStatus;

/**
 * Created by Errol on 17/5/23.
 */
public class ReservationListWithMobile extends ClientListParent {

    private String time;
    private String mobile;

    public ReservationListWithMobile(Reservation r, Client client, CacheManager cacheManager) {
        super(cacheManager.getClientCache(client.getId()));
        if (r.getStatus().equals(ReservationStatus.RESERVE)){
            this.time = DateUtil.date2String(r.getCreateTime(), DateUtil.PATTERN_K);
        } else if (r.getStatus().equals(ReservationStatus.BORROW)){
            this.time = DateUtil.date2String(r.getBorrowedTime(), DateUtil.PATTERN_K) +" - 至今";
        }else {
            this.time = "";
        }
        this.mobile = client.getMobile();
    }

    public String getTime() {
        return time;
    }

    public String getMobile() {
        return mobile;
    }
}
