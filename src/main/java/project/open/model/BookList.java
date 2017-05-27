package project.open.model;

import common.CRUD.service.ComService;
import project.operation.entity.Book;

/**
 * Created by Errol on 17/5/9.
 */
public class BookList extends BookListParent {

    private String author;
    private String status;

    public BookList(Book book, ComService comService) {
        super(book,comService);
        this.author = book.getAuthor();
        this.status = book.getStatus().getName();
    }

    public String getAuthor() {
        return author;
    }

    public String getStatus() {
        return status;
    }
}
