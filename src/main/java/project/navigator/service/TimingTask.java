package project.navigator.service;

import common.CRUD.service.ComService;
import common.Util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.operation.entity.Reservation;
import project.operation.service.MessageService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Errol on 17/6/16.
 */
@Component
public class TimingTask {

    @Autowired
    private ComService comService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CacheManager cacheManager;

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkReservation() {
        System.out.println("定时任务触发");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date date2 = calendar.getTime();
        List<Reservation> list = comService.getList(Reservation.class, "expireTime>='" + DateUtil.date2String(date1, DateUtil.PATTERN_A)
                + "' and expireTime<'" + DateUtil.date2String(date2, DateUtil.PATTERN_A) + "'");
        for (Reservation r : list) {
            messageService.send(-1, r.getClientId(), "您借阅的图书【" + cacheManager.getBookCache(r.getBookId()).getName() + "】即将于一周后到期，请注意避免逾期。");
        }
    }


}
