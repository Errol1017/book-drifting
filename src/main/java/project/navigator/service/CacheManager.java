package project.navigator.service;

import common.CRUD.service.ComService;
import common.Util.InvitationCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import project.basic.entity.Agency;
import project.basic.entity.BookClassification;
import project.basic.entity.InvitationCode;
import project.basic.model.AgencyCache;
import project.navigator.model.Navigation;
import project.operation.entity.Book;
import project.operation.entity.Client;
import project.operation.entity.Reservation;
import project.operation.entity.Stacks;
import project.operation.model.BookCache;
import project.operation.model.ClientCache;
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

    //admin cache
    private Map<Integer, String> bookClassificationCache;
    private Map<Integer, AgencyCache> agencyCache;
    //dynamic select
    private List<Map<String, String>> bookClassificationSelect;
    private List<Map<String, String>> agencySelect;

    //public cache
    private List<Map<String, String>> publicBookClassificationSelect;
    private List<Map<String, String>> publicBookClassificationSelectMultiple;
    private List<Map<String, String>> publicAgencySelect;
    private Map<String, ClientCache> clientCacheMap;
    private Map<Long, String> clientIdAndOpenIdMap;
    private Set<String[]> bookMatchSet;
    private Map<Long, BookCache> bookCacheMap;
    private Map<String, Long> bookCacheCodeAndIdMap;

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
//                comService.saveDetail(new BookClassification(arr[i]));
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
            Stacks s1 = new Stacks("海虞北路","上午 8:30-11:00 ,下午 13:00-16:30");
            comService.saveDetail(s1);
            comService.saveDetail(new Agency("国土局", "GTJ", s1.getId()));
            Stacks s2 = new Stacks("枫林路","上午 9:00-11:00 ,下午 13:00-16:00");
            comService.saveDetail(s2);
            comService.saveDetail(new Agency("常熟海关", "CSHG", s2.getId()));
            Stacks s3 = new Stacks("长江路","上午 8:00-11:00 ,下午 12:30-16:30");
            comService.saveDetail(s3);
            comService.saveDetail(new Agency("地税", "DS", s3.getId()));
        }

        /**
         * 初始化图书
         */
        Book book = comService.getFirst(Book.class, "id asc");
        if (book ==null) {
            List<Book> list = new ArrayList<>();
            for (int i=0;i<24;i++){
                list.add(new Book(i));
            }
            comService.saveDetail(list);
        }
    }

    private void initCache() {
        //加载图书分类缓存
        resetBookClassificationCache();
        //加载单位信息
        resetAgencyCache();
        //加载用户登录信息
        resetClientCache();
        //加载书名与作者
        resetBookMatchSet();
        //加载图书状态等缓存
        resetBookCache();
    }


    /**
     * 图书分类缓存相关方法
     */
    //获取图书分类下拉列表
    public List<Map<String, String>> getBookClassificationSelect() {
        return bookClassificationSelect;
    }
    public List<Map<String, String>> getPublicBookClassificationSelect() {
        return publicBookClassificationSelect;
    }
    public List<Map<String, String>> getPublicBookClassificationSelectMultiple() {
        return publicBookClassificationSelectMultiple;
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
    public void resetBookClassificationCache() {
        bookClassificationSelect = new ArrayList<>();
        bookClassificationCache = new HashMap<>();
        publicBookClassificationSelect = new ArrayList<>();
        publicBookClassificationSelectMultiple = new ArrayList<>();
        Map<String, String> map;
        List<BookClassification> list = comService.getList(BookClassification.class);
        for (BookClassification bc: list) {
            map = new HashMap<>();
            map.put("val", String.valueOf(bc.getId()));
            map.put("text", bc.getName());
            bookClassificationSelect.add(map);
            map = new HashMap<>();
            map.put("value", String.valueOf(bc.getId()));
            map.put("name", bc.getName());
            publicBookClassificationSelect.add(map);
            map = new HashMap<>();
            map.put("key", String.valueOf(bc.getId()));
            map.put("value", bc.getName());
            publicBookClassificationSelectMultiple.add(map);
            bookClassificationCache.put(bc.getId(), bc.getName());
        }
    }

    /**
     * 单位信息缓存相关方法
     */
    //获取单位下拉列表
    public List<Map<String, String>> getAgencySelect() {
        return agencySelect;
    }
    public List<Map<String, String>> getPublicAgencySelect() {
        return publicAgencySelect;
    }
    //获取单位信息
    public AgencyCache getAgencyCache(int id) {
        return agencyCache.get(id);
    }
    //检查单位id是否存在
    //修改单位信息后重置缓存
    public void resetAgencyCache() {
        agencySelect = new ArrayList<>();
        agencyCache = new HashMap<>();
        publicAgencySelect = new ArrayList<>();
        List<Object[]> list = comService.query("from Agency a left join Stacks s on a.stackId=s.id");
        Map<String, String> map;
        for (Object[] o: list) {
            AgencyCache ac = new AgencyCache(o);
            agencyCache.put(ac.getId(), ac);
            map = new HashMap<>();
            map.put("val", String.valueOf(ac.getId()));
            map.put("code", ac.getCode());
            map.put("text", ac.getName());
            agencySelect.add(map);
            map = new HashMap<>();
            map.put("value", String.valueOf(ac.getId()));
            map.put("name", ac.getName());
            publicAgencySelect.add(map);
        }
        System.out.println();
    }

    /**
     * 用户信息缓存相关方法
     */
    //获取用户缓存
    public ClientCache getClientCache(String openId) {
        return clientCacheMap.get(openId);
    }
    public ClientCache getClientCache(long id) {
        return clientCacheMap.get(clientIdAndOpenIdMap.get(id));
    }
    //添加用户缓存
    public void addClientCache(ClientCache clientCache) {
        clientCacheMap.put(clientCache.getOpenId(), clientCache);
        clientIdAndOpenIdMap.put(clientCache.getId(), clientCache.getOpenId());
    }
    //重置用户缓存
    private void resetClientCache() {
        clientCacheMap = new HashMap<>();
        clientIdAndOpenIdMap = new HashMap<>();
        List<Client> list = comService.getList(Client.class);
        for (Client client: list) {
            clientCacheMap.put(client.getOpenId(), new ClientCache(client, comService));
            clientIdAndOpenIdMap.put(client.getId(), client.getOpenId());
        }
    }

    /**
     * 图书信息set，用于前端快速匹配作者和书名
     */
    //获取匹配图书缓存
    public List<Map<String, String>> getBookMatch(String m) {
        List<Map<String, String>> list = new ArrayList<>();
        Iterator<String[]> i = bookMatchSet.iterator();
        while (i.hasNext() && list.size()<8) {
            String[] s = i.next();
            if (s[0].indexOf(m)!=-1 || s[1].indexOf(m)!=-1) {
                Map<String, String> map = new HashMap<>();
                map.put("title", "《"+s[0]+"》 "+s[1]);
                list.add(map);
            }
        }
        return list;
    }
    //重置图书缓存
    private void resetBookMatchSet() {
        bookMatchSet = new HashSet<>();
        List<Object[]> res = comService.getFields(Book.class,"name,author");
        for (Object[] o: res){
            bookMatchSet.add(new String[]{String.valueOf(o[0]),String.valueOf(o[1])});
        }
    }

    /**
     * 图书缓存，缓存图书状态、所有人等
     */
    //获取图书缓存
    public BookCache getBookCache(long id) {
        return bookCacheMap.get(id);
    }
    public BookCache getBookCache(String code) {
        Object id = bookCacheCodeAndIdMap.get(code);
        if (id!=null){
            return bookCacheMap.get(id);
        }
        return null;
    }
    //重置图书缓存
    private void resetBookCache() {
        bookCacheMap = new HashMap<>();
        bookCacheCodeAndIdMap = new HashMap<>();
        List<Book> list = comService.getList(Book.class);
        for (Book book: list) {
            BookCache bookCache = new BookCache(book);
            if (book.getReservationId() != -1) {
                Reservation reservation = comService.getDetail(Reservation.class, book.getReservationId());
                bookCache.setHolderId(reservation.getClientId());
            }
            bookCacheMap.put(bookCache.getId(), bookCache);
            bookCacheCodeAndIdMap.put(bookCache.getCode(), bookCache.getId());
        }
    }





    private void test() {
//        comService.test();
    }

    public static void main(String[] args) {
        String s = "wqedq-asda";
        s=s.replace("-","");
        System.out.println(s);
    }

}
