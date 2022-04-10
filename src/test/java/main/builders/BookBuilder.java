package main.builders;

import main.entity.Book;

public class BookBuilder {

    private final Book book;

    public BookBuilder() {
        book = new Book();
    }

    public BookBuilder setName(String name) {
        book.setName(name);
        return this;
    }

    public BookBuilder setCnt(Long cnt) {
        book.setCnt(cnt);
        return this;
    }

    public BookBuilder setTypeId(Long typeId) {
        book.setTypeId(typeId);
        return this;
    }

    public Book build() {
        return book;
    }
}
