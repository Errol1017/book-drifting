package project.navigator.service;

import common.CRUD.service.ComService;
import common.Util.DateUtil;
import common.Util.InvitationCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import project.basic.entity.Agency;
import project.basic.entity.BookClassification;
import project.basic.entity.InvitationCode;
import project.navigator.model.Navigation;
import project.system.entity.Admin;
import project.system.entity.AdminRole;

import java.util.*;

/**
 * Created by Errol on 16/10/19.
 */
@Component
public class CacheManager implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ComService comService;

    //cache
    private Map<Integer, String> bookClassificationCache;
    private Map<Integer, Agency> agencyCache;

    //dynamic select
    private List<Map<String, String>> bookClassificationSelect;
    private List<Map<String, String>> agencySelect;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        /** 初始化root管理员和root权限 */
        initRootAdmin();
        /** 初始化数据 */
        initData();
        /** 加载缓存 */
        initCache();

        test();
    }

    private void initRootAdmin() {
        Admin admin = comService.getFirst(Admin.class, "id asc");
        if (admin == null) {
            comService.saveDetail(new Admin().addRootAdmin());
            comService.saveDetail(new AdminRole().addRootAdminRole());
        } else {
            /**
             * 这里不能用updateDetail方法，hibernate会把power字符串中的‘Admin'改成’project.system.entity.Admin’
             * 暂不知原因
             */
            AdminRole adminRole = comService.getDetail(AdminRole.class, 1);
            adminRole.setPower(Navigation.getInstance().getRootPowerString());
            comService.saveDetail(adminRole);
        }
    }

    private void initData() {
        /**
         * 初始化图书分类，或读取数据加入缓存
         * “其他”分类暂未加入，是否加入排序待定 ？
         * getCurrentSession().save() 自动提交数据 ？ 打印 sql 语句， 但是数据表查不到值
         */
        BookClassification bc = comService.getFirst(BookClassification.class, "id asc");
        if (bc == null) {
            String s = "马克思主义、列宁主义、毛泽东思想、邓小平理论,哲学、宗教,社会科学总论,政治、法律,军事,经济,文化、科学、教育、体育,语言、文字,文学,艺术," +
                    "历史、地理,自然科学总论,数理科学和化学,天文学、地球科学,生物科学,医药、卫生,农业科学,工业技术,交通运输,航空、航天,环境科学、安全科学,综合性图书";
            String[] arr = s.split(",");
            List<BookClassification> list = new ArrayList<>();
            for (int i = 0; i < arr.length; i++) {
                list.add(new BookClassification(arr[i]));
            }
            comService.saveDetail(list);
        }

        /**
         * 初始化邀请码
         */
        InvitationCode ic = comService.getFirst(InvitationCode.class, "id asc");
        if (ic == null) {
            List<String> codes = InvitationCodeGenerator.getMany(100);
            List<InvitationCode> list = new ArrayList<>();
            for (String s : codes) {
                list.add(new InvitationCode(s));
            }
            comService.saveDetail(list);
        }

        /**
         * 初始化单位信息
         */
        Agency ag = comService.getFirst(Agency.class, "id asc");
        if (ag == null) {
            comService.saveDetail(new Agency("国土局","海虞北路","8:30-11:00 am , 13:00-16:30 pm"));
            comService.saveDetail(new Agency("常熟海关","枫林路","9:00-11:00 am , 13:00-16:00 pm"));
            comService.saveDetail(new Agency("地税","长江路","8:00-11:00 am , 12:30-16:30 pm"));
        }
    }

    private void initCache() {
        //加载图书分类缓存
        resetBookClassificationList();
        //加载单位信息
        resetAgencyList();
    }


    /**
     * 图书分类相关方法
     */
    //获取图书分类下拉列表
    public List<Map<String, String>> getBookClassificationSelect() {
        return bookClassificationSelect;
    }
    //获取图书分类名称
    public String getBookClassificationName(int id) {
        return bookClassificationCache.get(id);
    }
    //检查图书分类id是否存在
    public boolean checkBookClassificationId(int id) {
        if (bookClassificationCache.containsKey(id)) {
            return true;
        } else {
            return false;
        }
    }
    //修改图书分类后重置缓存
    public void resetBookClassificationList() {
        bookClassificationSelect = new ArrayList<>();
        bookClassificationCache = new HashMap<>();
        List<BookClassification> list = comService.getList(BookClassification.class);
        for (BookClassification bc: list) {
            Map<String, String> map = new HashMap<>();
            map.put("val", String.valueOf(bc.getId()));
            map.put("text", bc.getName());
            bookClassificationSelect.add(map);
            bookClassificationCache.put(bc.getId(), bc.getName());
        }
    }

    /**
     * 单位信息相关方法
     */
    //获取单位下拉列表
    public List<Map<String, String>> getAgencySelect() {
        return agencySelect;
    }
    //获取单位信息
    public Agency getAgency(int id) {
        return agencyCache.get(id);
    }
    //检查单位id是否存在
    //修改单位信息后重置缓存
    public void resetAgencyList() {
        agencySelect = new ArrayList<>();
        agencyCache = new HashMap<>();
        List<Agency> list = comService.getList(Agency.class);
        for (Agency ag: list) {
            Map<String, String> map = new HashMap<>();
            map.put("val", String.valueOf(ag.getId()));
            map.put("text", ag.getName());
            agencySelect.add(map);
            agencyCache.put(ag.getId(), ag);
        }
    }


    private void test() {
//        comService.test();
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.string2Date("201607", DateUtil.PATTERN_H));
        System.out.println(new Date(Long.parseLong("1493183327995")));
        System.out.println(new Date().getTime());
        System.out.println(DateUtil.date2String(new Date(), DateUtil.PATTERN_J));
    }

}
