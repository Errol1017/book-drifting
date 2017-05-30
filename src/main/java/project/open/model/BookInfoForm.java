package project.open.model;

import common.Util.Base64Util;
import project.navigator.service.CacheManager;
import project.operation.entity.Book;
import project.operation.pojo.BookStatus;
import project.operation.pojo.OwnerType;
import project.resource.properties.ServerProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Errol on 17/5/9.
 */
public class BookInfoForm {

    private String id;
    private String name;
    private String author;
//    private String sort;
    private String intro;
    private String agency = "";
    private List<String> pics = new ArrayList<>();
    private String status;

    public BookInfoForm(Book book, CacheManager cacheManager) {
        this.id = String.valueOf(book.getId());
        this.name = book.getName();
        this.author = book.getAuthor();
        this.intro = book.getIntroduction();
        String[] arr = book.getPictures().split(",");
        for (String s : arr) {
            this.pics.add(Base64Util.img2String(ServerProperties.getInstance().getFileBasePath(), s));
        }
        this.status = book.getStatus().getName();
        if (book.getStatus().equals(BookStatus.IN_STOCK) && book.getStackType().equals(OwnerType.AGENCY)) {
            this.agency = cacheManager.getAgencyCache((int)book.getStackId()).getName();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getIntro() {
        return intro;
    }

    public String getAgency() {
        return agency;
    }

    public List<String> getPics() {
        return pics;
    }

    public String getStatus() {
        return status;
    }
}
