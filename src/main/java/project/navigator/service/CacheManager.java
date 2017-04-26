package project.navigator.service;

import common.CRUD.service.ComService;
import common.Util.DateUtil;
import common.Util.InvitationCodeGenerator;
import common.Util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import project.basic.entity.BookClassification;
import project.basic.entity.InvitationCode;
import project.navigator.model.Navigation;
import project.navigator.route.Types;
import project.system.entity.Admin;
import project.system.entity.AdminRole;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Errol on 16/10/19.
 */
@Component
public class CacheManager implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ComService comService;

    private List<BookClassification> bookClassificationList = new ArrayList<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        /** 初始化root管理员和root权限 */
        initRootAdmin();
        /** 初始化数据 */
        initData();
        /** 加载缓存 */
        initCache();

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
            for (int i = 0; i<arr.length; i++) {
                list.add(new BookClassification(arr[i]));
            }
            comService.saveDetail(list);
        }

        /**
         * 初始化邀请码
         */
        InvitationCode ic = comService.getFirst(InvitationCode.class, "id asc");
            List<String> codes = InvitationCodeGenerator.getMany(100);
        if (ic == null) {
            List<InvitationCode> list = new ArrayList<>();
            for (String s: codes) {
                System.out.println(s);
                list.add(new InvitationCode(s));
            }
            comService.saveDetail(list);
        }
    }

    private void initCache() {
        //加载图书分类缓存
        resetBookClassificationList();
    }


    //获取图书分类下拉列表
    public List<BookClassification> getBookClassificationList() {
        return bookClassificationList;
    }
    //获取图书分类名称
    public String getBookClassificationName(int id) {
        for (BookClassification bc: bookClassificationList) {
            if (bc.getId() == id) {
                return bc.getName();
            }
        }
        return "";
    }
    //检查图书分类id是否存在
    public boolean checkBookClassificationId(int id) {
        for (BookClassification bc: bookClassificationList) {
            if (bc.getId() == id) {
                return true;
            }
        }
        return false;
    }
    //修改图书分类后重置缓存
    public void resetBookClassificationList() {
        bookClassificationList = new ArrayList<>();
        bookClassificationList = comService.getList(BookClassification.class);
    }


    public static void main(String[] args) {
        System.out.println(DateUtil.string2Date("201607", DateUtil.PATTERN_H));
        System.out.println(new Date(Long.parseLong("1493183327995")));
        System.out.println(new Date().getTime());
    }

}
