package main.valueObjects;

import main.entity.Book;
import main.entity.BookType;

public class BookWithType {

    private final Book book = new Book();
    private final String typeName;

    public BookWithType(String name, long cnt, BookType type) {
        this.book.setName(name);
        this.book.setCnt(cnt);
        this.book.setTypeId(type.getId());
        this.typeName = type.getName();
    }

    public String getName() {
        return book.getName();
    }

    public Long getCnt() {
        return book.getCnt();
    }

    public Long getTypeId() {
        return book.getTypeId();
    }

    public Book getBook() {
        return book;
    }

    public String getTypeName() {
        return typeName;
    }
}
