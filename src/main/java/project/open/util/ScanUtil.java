package project.open.util;

import common.CRUD.service.ComService;
import common.DataFormatter.ErrorCode;
import common.DataFormatter.Result;
import project.navigator.service.CacheManager;
import project.operation.entity.Book;
import project.operation.entity.Reservation;
import project.operation.model.BookCache;
import project.operation.model.ClientCache;
import project.operation.pojo.BookStatus;
import project.operation.pojo.ReservationStatus;

import java.util.Date;

/**
 * Created by Errol on 17/5/22.
 */
public class ScanUtil {


    //检查是否是第一次扫描
    public static boolean isFirstScan(BookCache bookCache) {
        if (bookCache.getScannerId() != -1 && new Date().getTime() < bookCache.getExpireTime()) {
            return false;
        } else {
            clearFirstScanRecord(bookCache);
            return true;
        }
    }

    //根据图书状态返回扫描后的指引
    public static Result getFirstScanDirection(BookCache bookCache, ClientCache clientCache) {
        if (bookCache.getStatus().equals(BookStatus.UNPREPARED)) {
            if (bookCache.getOwnerId() == clientCache.getId()) { //所有人 -- 入库
                return Result.SUCCESS("感谢您上传图书！您正在申请图书入库");
            } else { //图书入库前就流转
                return checkClientBorrowingSum(clientCache);
            }
        } else if (bookCache.getStatus().equals(BookStatus.IN_STOCK)) {
            if (bookCache.getAgencyId() == -1) { //用户自己管理
                if (bookCache.getOwnerId() == clientCache.getId()) { //且自己扫描
                    return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "无效操作。如果您希望图书出库，请先进入【我的图书】栏目，提交相关图书的出库申请。");
                }
            }
            return checkClientBorrowingSum(clientCache);
        } else if (bookCache.getStatus().equals(BookStatus.BORROWED) || bookCache.getStatus().equals(BookStatus.EXPIRED)) {
            if (bookCache.getHolderId() == clientCache.getId()) {    //持有人 -- 归还或流转
                return Result.SUCCESS("您正在申请交接图书");
            } else {
                return checkClientBorrowingSum(clientCache);
            }
        } else if (bookCache.getStatus().equals(BookStatus.RELEASED)) {//图书已申请出库
            if (bookCache.getOwnerId() == clientCache.getId()) { //等待出库且用户即图书所有人
                return Result.SUCCESS("您已申请该图书出库");
            } else if (bookCache.getAgencyId() != -1 && bookCache.getAgencyId() == clientCache.getAgencyId()) {   //图书由机构管理且用户由管理权限
                return Result.SUCCESS("您正在审核该图书的归还申请");
            } else {
                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "无效操作。该图书已申请出库，无法再借阅或流转。");
            }
        }
        return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "无效操作。");
    }

    //检查用户是否还能再借图书
    private static Result checkClientBorrowingSum(ClientCache clientCache) {
        if (clientCache.getBorrowingSum()>=2){
            return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "您借书已满2本，无法再借");
        }else {
            return Result.SUCCESS("您正在申请借阅图书");
        }
    }

    //根据图书状态记录用户id
    public static Result setFirstScanConfirm(BookCache bookCache, ClientCache clientCache, ComService comService, CacheManager cacheManager) {
        if (bookCache.getStatus().equals(BookStatus.UNPREPARED)) {
            if (bookCache.getOwnerId() == clientCache.getId()) { //所有人 -- 入库
                saveFirstScanRecord(bookCache, clientCache);
                return Result.SUCCESS();
            } else { //图书入库前就流转
                saveFirstScanRecord(bookCache, clientCache);
                return Result.SUCCESS();
            }
        } else if (bookCache.getStatus().equals(BookStatus.IN_STOCK)) {
            if (bookCache.getAgencyId() == -1) { //用户自己管理
                if (bookCache.getOwnerId() == clientCache.getId()) { //且自己扫描
                    return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "无效操作。如果您希望图书出库，请先进入【我的图书】栏目，提交相关图书的出库申请。");
                }
            }
            saveFirstScanRecord(bookCache, clientCache);
            return Result.SUCCESS();
        } else if (bookCache.getStatus().equals(BookStatus.BORROWED) || bookCache.getStatus().equals(BookStatus.EXPIRED)) {
            if (bookCache.getHolderId() == clientCache.getId()) {    //持有人 -- 归还或流转
                saveFirstScanRecord(bookCache, clientCache);
                return Result.SUCCESS();
            } else {
                saveFirstScanRecord(bookCache, clientCache);
                return Result.SUCCESS();
            }
        } else if (bookCache.getStatus().equals(BookStatus.RELEASED)) {
            if (bookCache.getOwnerId() == clientCache.getId()) { //等待出库且用户即图书所有人 -- 直接出库
                ReservationUtil.dealWidthRecede(bookCache, BookStatus.FROZEN, comService, cacheManager);
                bookCache.setAgencyId(-1);
                bookCache.setStatus(BookStatus.FROZEN);
                return Result.SUCCESS("图书已出库");
            } else if (bookCache.getAgencyId() != -1 && bookCache.getAgencyId() == clientCache.getAgencyId()) {   //图书由机构管理且用户有管理权限 -- 机构暂收
                ReservationUtil.dealWidthRecede(bookCache, BookStatus.RELEASED, comService, cacheManager);
                return Result.SUCCESS("图书已归还");
            } else {
                return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "无效操作。该图书已申请出库，无法再借阅或流转。");
            }
        }
        return Result.ERROR(ErrorCode.CUSTOMIZED_ERROR, "无效操作。");
    }

    //记录第一次扫描的用户id
    private static void saveFirstScanRecord(BookCache bookCache, ClientCache clientCache) {
        bookCache.setScannerId(clientCache.getId());
        bookCache.setExpireTime(new Date().getTime() + 3600000);
    }

    //清除失效的第一次扫描记录
    private static void clearFirstScanRecord(BookCache bookCache) {
        bookCache.setScannerId(-1);
        bookCache.setExpireTime(-1);
    }

    //获取第二次扫描后的指引
    public static Result getSecondScanDirection(BookCache bookCache, ClientCache clientCache) {
        if (bookCache.getStatus().equals(BookStatus.UNPREPARED)) {
            if (bookCache.getOwnerId() == bookCache.getScannerId()) { //所有人 -- 入库中
                if (clientCache.getAgencyId() != -1 && bookCache.getAgencyId() == clientCache.getAgencyId()) {  //用户有图书委托的机构管理权限，否则中断入库流程
                    return Result.SUCCESS("您正在审批该图书的入库申请");
                }
            } else { //图书入库前就流转中
                if (bookCache.getOwnerId() == clientCache.getId()) { //用户就是图书所有人，否则中断入库前流转
                    return Result.SUCCESS("您正在审核该图书的流转申请");
                }
            }
        } else if (bookCache.getStatus().equals(BookStatus.IN_STOCK)) {   //图书借阅中
            if (bookCache.getAgencyId() == -1) { //用户自己管理
                if (bookCache.getOwnerId() == clientCache.getId()) { //用户就是图书所有人，否则中断
                    return Result.SUCCESS("您正在审核该图书的流转申请");
                }
            } else { //委托机构管理
                if (clientCache.getAgencyId() != -1 && bookCache.getAgencyId() == clientCache.getAgencyId()) {    //用户有图书委托的机构管理权限，否则中断
                    return Result.SUCCESS("您正在审核该图书的借阅申请");
                }
            }
        } else if (bookCache.getStatus().equals(BookStatus.BORROWED) || bookCache.getStatus().equals(BookStatus.EXPIRED)) {
            if (bookCache.getHolderId() == bookCache.getScannerId()) {    //第一次扫描是持有人
                if ((bookCache.getAgencyId() == -1 && bookCache.getOwnerId() == clientCache.getId()) || (bookCache.getAgencyId() != -1 && bookCache.getAgencyId() == clientCache.getAgencyId())) {
                    return Result.SUCCESS("您正在审核该图书的归还申请");
                } else if (bookCache.getHolderId() != clientCache.getId()) { //不是管理员且不是持有人
                    return checkClientBorrowingSum(clientCache);
                }
            } else { //第一次扫描是普通用户，流转中
                if (bookCache.getHolderId() == clientCache.getId()) { //第二次扫描是持有人，否则中断
                    return Result.SUCCESS("您正在审核该图书的流转申请");
                }
            }
//        } else if (bookCache.status.equals(BookStatus.RELEASED)) {
//
        }
        clearFirstScanRecord(bookCache);
        return getFirstScanDirection(bookCache, clientCache);
    }

    //建立或修改相关reservation
    public static Result setSecondScanConfirm(BookCache bookCache, ClientCache clientCache, ComService comService, CacheManager cacheManager) {
        if (bookCache.getStatus().equals(BookStatus.UNPREPARED)) {
            if (bookCache.getOwnerId() == bookCache.getScannerId()) { //所有人 -- 入库中
                if (clientCache.getAgencyId() != -1 && bookCache.getAgencyId() == clientCache.getAgencyId()) {  //用户有图书委托的机构管理权限，否则中断入库流程
                    Book book = comService.getDetail(Book.class, bookCache.getId());
                    book.setStatus(BookStatus.IN_STOCK);
                    comService.saveDetail(book);
                    return Result.SUCCESS("图书已入库");
                }
            } else { //图书入库前就流转中
                if (bookCache.getOwnerId() == clientCache.getId()) { //用户就是图书所有人，否则中断入库前流转
                    ReservationUtil.dealWidthBorrow(bookCache, clientCache, comService, cacheManager);
                    clearFirstScanRecord(bookCache);
                    return Result.SUCCESS("图书已借阅");
                }
            }
        } else if (bookCache.getStatus().equals(BookStatus.IN_STOCK)) {   //图书借阅中
            if (bookCache.getAgencyId() == -1) { //用户自己管理
                if (bookCache.getOwnerId() == clientCache.getId()) { //用户就是图书所有人，否则中断
                    ReservationUtil.dealWidthBorrow(bookCache, clientCache, comService, cacheManager);
                    return Result.SUCCESS("图书已借阅");
                }
            } else { //委托机构管理
                if (clientCache.getAgencyId() != -1 && bookCache.getAgencyId() == clientCache.getAgencyId()) {    //用户有图书委托的机构管理权限，否则中断
                    ReservationUtil.dealWidthBorrow(bookCache, clientCache, comService, cacheManager);
                    return Result.SUCCESS("图书已借阅");
                }
            }
        } else if (bookCache.getStatus().equals(BookStatus.BORROWED) || bookCache.getStatus().equals(BookStatus.EXPIRED)) {
            if (bookCache.getHolderId() == bookCache.getScannerId()) {    //第一次扫描是持有人
                if ((bookCache.getAgencyId() == -1 && bookCache.getOwnerId() == clientCache.getId()) || (bookCache.getAgencyId() != -1 && bookCache.getAgencyId() == clientCache.getAgencyId())) {
                    ReservationUtil.dealWidthRecede(bookCache, BookStatus.IN_STOCK, comService, cacheManager);
                    return Result.SUCCESS("图书已归还");
                } else if (bookCache.getHolderId() != clientCache.getId()) { //不是管理员且不是持有人
                    ReservationUtil.dealWidthBorrow(bookCache, clientCache, comService, cacheManager);
                    return Result.SUCCESS("图书已借阅");
                }
            } else { //第一次扫描是普通用户，流转中
                if (bookCache.getHolderId() == clientCache.getId()) { //第二次扫描是持有人，否则中断
                    ReservationUtil.dealWidthBorrow(bookCache, clientCache, comService, cacheManager);
                    return Result.SUCCESS("图书已借阅");
                }
            }
//        } else if (bookCache.status.equals(BookStatus.RELEASED)) {
//
        }
        clearFirstScanRecord(bookCache);
        return getFirstScanDirection(bookCache, clientCache);
    }


}
