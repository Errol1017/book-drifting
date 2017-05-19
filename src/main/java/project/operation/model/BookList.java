package project.operation.model;

import project.navigator.service.CacheManager;
import project.operation.entity.Book;
import project.operation.pojo.OwnerType;

/**
 * Created by Errol on 17/5/2.
 */
public class BookList {

    private String id;
    private String name;
    private String author;
    private String bookClass;
    private String ownerType;
    private String owner;//agency name
    private String status;

    public BookList(Book book, CacheManager cacheManager) {
        this.id = String.valueOf(book.getId());
        this.name = book.getName();
        this.author = book.getAuthor();
        this.bookClass = cacheManager.getBookClassificationName(book.getClassificationId());
        this.ownerType = book.getOwnerType().getName();
        this.owner = book.getOwnerType().equals(OwnerType.AGENCY)?cacheManager.getAgencyCache((int)book.getOwnerId()).getName():"";
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

    public String getOwnerType() {
        return ownerType;
    }

    public String getOwner() {
        return owner;
    }

    public String getStatus() {
        return status;
    }
}
