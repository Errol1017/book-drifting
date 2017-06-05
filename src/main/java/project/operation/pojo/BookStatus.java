package project.operation.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Errol on 17/4/16.
 */
public enum  BookStatus {

    //委托机构管理而尚未交给机构
    UNPREPARED("尚未入库"),

    //在机构或在所有人手里
    IN_STOCK("在架/闲置"),

    BORROWED("外借/流转"),

    EXPIRED("到期未还"),

    RELEASED("等待出库"),

    FROZEN("已出库")
    ;

    private String name;

    BookStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<Map<String, String>> getBookStatusSelect() {
        List<Map<String, String>> select = new ArrayList<>();
        Map<String, String> map;
        for (BookStatus bookStatus: BookStatus.values()) {
            map = new HashMap<>();
            map.put("val", bookStatus.toString());
            map.put("text", bookStatus.getName());
            select.add(map);
        }
        return select;
    }
}
