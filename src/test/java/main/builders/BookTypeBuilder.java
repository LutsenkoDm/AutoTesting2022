package main.builders;

import main.entity.BookType;

public class BookTypeBuilder {

    private final BookType bookType;

    public BookTypeBuilder() {
        bookType = new BookType();
    }

    public BookTypeBuilder setName(String name) {
        bookType.setName(name);
        return this;
    }

    public BookTypeBuilder setCnt(Long cnt) {
        bookType.setCnt(cnt);
        return this;
    }

    public BookTypeBuilder setFine(Long fine) {
        bookType.setFine(fine);
        return this;
    }

    public BookTypeBuilder setDayCount(Long dayCount) {
        bookType.setDayCount(dayCount);
        return this;
    }

    public BookType build() {
        return bookType;
    }
}
