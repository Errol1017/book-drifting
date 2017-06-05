package project.operation.model;

import project.navigator.service.CacheManager;
import project.operation.entity.Book;
import project.operation.pojo.OwnerType;

/**
 * Created by Errol on 17/5/2.
 */
public class BookForm {

    private String id;
    private String name;
    private String author;
    private String bookClass;
    private String introduction;
    private String pictures;
    private String ownerType;
    private String owner;
    private String stack;
//    private String stackId;
    private String status;

    public BookForm() {
    }

    public BookForm(Book book, CacheManager cacheManager) {
        this.id = String.valueOf(book.getId());
        this.name = book.getName();
        this.author = book.getAuthor();
        this.bookClass = String.valueOf(book.getClassificationId());
        this.introduction = book.getIntroduction();
        this.pictures = book.getPictures();
        this.ownerType = book.getOwnerType().getName() + "所有";
        ClientCache cc = cacheManager.getClientCache(book.getOwnerId());
        this.owner = cc.getNickName() + "（" + cc.getName() + "）";
        this.stack = book.getStackType().equals(OwnerType.INDIVIDUAL)?"用户自己管理":("委托 "+cacheManager.getAgencyCache((int)book.getStackId()).getName()+" 管理");
//        this.stackId = "";
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

    public String getBookClass() {
        return bookClass;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getPictures() {
        return pictures;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public String getOwner() {
        return owner;
    }

    public String getStack() {
        return stack;
    }

//    public String getStackId() {
//        return stackId;
//    }

    public String getStatus() {
        return status;
    }
}
