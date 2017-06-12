package project.navigator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Errol on 17/3/11.
 */
public class Navigation {

    private static ArrayList<Module> modules = new ArrayList<>();
    private static final Navigation instance = new Navigation();

    private Navigation() {
        modules.add(new Module(1, "Operation", "运营管理", "com-res/common/img/sidebar/tables.png", "", ""));
        modules.add(new Module(2, "Reservation", "借阅记录", "", "Reservation", "ReservationPage"));
        modules.add(new Module(2, "Comment", "书评管理", "", "Comment", "CommentPage"));
        modules.add(new Module(2, "Books", "图书管理", "", "Books", "BooksPage"));
        modules.add(new Module(2, "Client", "用户管理", "", "Client", "ClientPage"));

        modules.add(new Module(1, "Basic", "基础设置", "com-res/common/img/sidebar/tables.png", "", ""));
        modules.add(new Module(2, "Invitation", "邀请码使用情况", "", "Invitation", "InvitationPage"));
        modules.add(new Module(2, "Stacks", "固定起漂点管理", "", "Stacks", "StacksPage"));
        modules.add(new Module(2, "Class", "图书分类管理", "", "Class", "ClassPage"));
        modules.add(new Module(2, "Export", "导入与导出", "", "Export", "ExportPage"));

        modules.add(new Module(1, "System", "系统管理", "com-res/common/img/sidebar/tables.png", "", ""));
        modules.add(new Module(2, "Admin", "管理员设置", "", "Admin", "AdminPage"));
        modules.add(new Module(2, "AdminLog", "操作记录", "", "AdminLog", "AdminLogPage"));

    }

    public static Navigation getInstance() {
        return instance;
    }

    /**
     * 开发阶段初始化root权限
     *
     * @return
     */
    public String getRootPowerString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Module module : modules) {
            stringBuffer.append(module.getId() + ",");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    public String[] getPageNameAndPageId(String reqId) {
        for (Module m : modules) {
            if (m.getId().equals(reqId)) {
                return new String[]{m.getPageName(), m.getPageId()};
            }
        }
        return new String[]{};
    }

    public ArrayList<String> getPowerList(String power) {
        String[] arr = power.split(",");
        int i = 0;
        ArrayList<String> list = new ArrayList<>();
        for (Module m : modules) {
            if (m.getId().equals(arr[i])) {
                list.add(m.getId());
                if (++i >= arr.length) {
                    break;
                }
            }
        }
        return list;
    }

    public ArrayList<Module> getSidebarList(ArrayList<String> power) {
        int i = 0;
        ArrayList<Module> list = new ArrayList<>();
        for (Module m : modules) {
            if (m.getId().equals(power.get(i))) {
                list.add(m);
                if (++i >= power.size()) {
                    break;
                }
            }
        }
        return list;
    }

    public List<Map<String, String>> getPowerCascade() {
        List<Map<String, String>> list = new ArrayList<>();
        for (Module m : modules) {
            Map<String, String> map = new HashMap<>();
            map.put("level", String.valueOf(m.getLevel()));
            map.put("val", m.getId());
            map.put("text", m.getName());
            list.add(map);
        }
        return list;
    }
}
