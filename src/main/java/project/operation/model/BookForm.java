package project.operation.model;

import project.operation.entity.Book;

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
    private String ownerId;
    private String stackIds;
    private String status;

    public BookForm(Book book) {
        this.id = String.valueOf(book.getId());
        this.name = book.getName();
        this.author = book.getAuthor();
        this.bookClass = String.valueOf(book.getClassificationId());
        this.introduction = book.getIntroduction();
        this.pictures = book.getPictures();
        this.ownerType = book.getOwnerType().getName();
        this.ownerId = "";
        this.stackIds = "";
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

    public String getOwnerId() {
        return ownerId;
    }

    public String getStackIds() {
        return stackIds;
    }

    public String getStatus() {
        return status;
    }
}
