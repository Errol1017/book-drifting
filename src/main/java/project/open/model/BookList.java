package project.open.model;

import project.operation.entity.Book;
import project.operation.model.BookCache;

/**
 * Created by Errol on 17/5/9.
 */
public class BookList extends BookListParent {

    private String author;
    private String status;

    public BookList(Book book, BookCache bookCache) {
        super(bookCache);
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
