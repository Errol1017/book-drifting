package project.navigator.model;

/**
 * Created by Errol on 17/3/11.
 */
class Module {

    private int level;
    private String id;
    private String name;
    private String imgUrl;
    private String pageName;
    private String pageId;

    public Module(int level, String id, String name, String imgUrl, String pageName, String pageId) {
        this.level = level;
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.pageName = pageName;
        this.pageId = pageId;
    }

    public int getLevel() {
        return level;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getPageName() {
        return pageName;
    }

    public String getPageId() {
        return pageId;
    }
}
