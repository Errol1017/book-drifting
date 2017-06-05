package project.operation.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Errol on 17/5/17.
 */
public enum ReservationStatus {

    RESERVE("预约中"),

    BORROW("借阅中"),

    RECEDE("已归还")
    ;
    private String name;

    ReservationStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<Map<String, String>> getBookReservationStatusSelect() {
        List<Map<String, String>> select = new ArrayList<>();
        Map<String, String> map;
        for (ReservationStatus status: ReservationStatus.values()) {
            map = new HashMap<>();
            map.put("val", status.toString());
            map.put("text", status.getName());
            select.add(map);
        }
        map = new HashMap<>();
        map.put("val", "expire");
        map.put("text", "已过期");
        select.add(map);
        return select;
    }
}
