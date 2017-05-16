package project.open.model;

import common.CRUD.service.ComService;
import common.Util.Base64Util;
import project.operation.entity.Book;

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
    private List<String> pics = new ArrayList<>();
    private String status;

    public BookInfoForm(Book book, ComService comService) {
        this.id = String.valueOf(book.getId());
        this.name = book.getName();
        this.author = book.getAuthor();
        this.intro = book.getIntroduction();
        String[] arr = book.getPictures().split(",");
        for (String s : arr) {
            this.pics.add(Base64Util.img2String(comService.getFileBathPath(), s));
        }
        this.status = book.getStatus().getName();
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

    public List<String> getPics() {
        return pics;
    }

    public String getStatus() {
        return status;
    }
}
