package project.open.model;

import common.CRUD.service.ComService;
import common.Util.Base64Util;
import project.navigator.service.CacheManager;
import project.operation.entity.Book;
import project.operation.entity.Reservation;
import project.operation.model.BookCache;
import project.operation.pojo.BookStatus;
import project.operation.pojo.OwnerType;

/**
 * Created by Errol on 17/5/19.
 */
public class BookListParent {

    private String id;
    private String name;
    private String face;

    public BookListParent(BookCache bookCache) {
        this.id = String.valueOf(bookCache.getId());
        this.name = bookCache.getName();
        this.face = bookCache.getFace();
    }

    protected String getOwnerString(Book book, CacheManager cacheManager, ComService comService) {
        if (book.getStatus().equals(BookStatus.IN_STOCK)) {
            if (book.getStackType().equals(OwnerType.AGENCY)) {
                return "委托 " + cacheManager.getAgencyCache((int) book.getStackId()).getName() + " 进行管理";
            } else {
                return "由您亲自管理";
            }
        } else if (book.getStatus().equals(BookStatus.UNPREPARED)) {
            return "尚未移交 " + cacheManager.getAgencyCache((int) book.getStackId()).getName() + " 入库管理";
        } else if (book.getStatus().equals(BookStatus.FROZEN)) {
            return "已出库";
        }else if (book.getStatus().equals(BookStatus.RELEASED)) {
            if (book.getReservationId() == -1) {
                return "委托 " + cacheManager.getAgencyCache((int) book.getStackId()).getName() + " 进行管理";
            } else {
                Reservation r = comService.getDetail(Reservation.class, book.getReservationId());
                return "目前用户 " + cacheManager.getClientCache(r.getClientId()).getNickName() + " 正在借阅";
            }
        } else {
            Reservation r = comService.getDetail(Reservation.class, book.getReservationId());
            return "目前用户 " + cacheManager.getClientCache(r.getClientId()).getNickName() + " 正在借阅";
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }
}
