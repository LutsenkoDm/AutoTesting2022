package main.tests.model;

import main.entity.Book;
import main.entity.BookType;
import main.pages.BookTypesPage;
import main.pages.BooksPage;
import main.pages.LoginPage;
import main.valueObjects.BookWithType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Books CRUD tests")
public class BooksTest extends FunctionalTest {

    private static BooksPage booksPage;
    private static BookWithType bookWithType;

    private static int generateCounter = 1;
    private static final BookType[] bookTypes = new BookType[]{
            new BookType("forBookName1", 1L, 11L, 111L),
            new BookType("forBookName2", 2L, 22L, 222L),
            new BookType("forBookName3", 3L, 33L, 333L)
    };

    @BeforeClass
    public static void init() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.get();
        loginPage.loginAsDefaultAdmin();
        BookTypesPage bookTypesPage = new BookTypesPage(driver);
        bookTypesPage.get();
        LinkedList<Long> ids = bookTypesPage.ids();
        long lastBookTypeId = ids.isEmpty() ? 0 : ids.getLast();
        bookTypes[0].setId(lastBookTypeId + 1);
        bookTypes[1].setId(lastBookTypeId + 2);
        bookTypes[2].setId(lastBookTypeId + 3);
        bookTypesPage.addBookType(bookTypes[0]);
        bookTypesPage.addBookType(bookTypes[1]);
        bookTypesPage.addBookType(bookTypes[2]);
        booksPage = new BooksPage(driver);
        booksPage.get();
    }

    @Before
    public void generate() {
        bookWithType = generateBookWithRandomType();
        generateCounter++;
    }

    @Test
    @Tag("add")
    @Timeout(10)
    public void addSimpleOnlyNameCheck() {
        assertTrue(booksPage
                .addBook(bookWithType)
                .containsName(bookWithType.getName())
        );
    }

    @Test
    @Tag("add")
    @Timeout(10)
    public void addSimple() {
        assertTrue(booksPage
                .addBook(bookWithType)
                .contains(bookWithType)
        );
    }

    @Test
    @Tag("add")
    @Timeout(25)
    public void addMultiple() {
        BookWithType bookWithType2 = generateBookWithType(1);
        BookWithType bookWithType3 = generateBookWithType(2);
        assertTrue(booksPage
                .addBook(bookWithType)
                .addBook(bookWithType2)
                .addBook(bookWithType3)
                .contains(List.of(bookWithType, bookWithType2, bookWithType3))
        );
        List<Book> books = booksPage.all();
        List<String> names = booksPage.names();
        Book book1 = bookWithType.getBook();
        Book book2 = bookWithType2.getBook();
        Book book3 = bookWithType3.getBook();
        assertAll(
                () -> assertThat(books).extracting("name", "cnt", "typeId")
                        .contains(tuple(book1.getName(),book1.getCnt(), book1.getTypeId())),
                () -> assertThat(books).contains(book1),
                () -> assertThat(books).contains(book2),
                () -> assertThat(books).contains(book3),
                () -> assertThat(names).contains(book1.getName()),
                () -> assertThat(names).contains(book2.getName()),
                () -> assertThat(names).contains(book3.getName()),
                () -> assertThat(names).contains(book1.getName(), book2.getName(), book3.getName()),
                () -> assertThat(bookWithType3).extracting(BookWithType::getBook).isEqualTo(books.get(books.size()-1)),
                () -> assertThat(bookWithType2).extracting(BookWithType::getBook).isEqualTo(books.get(books.size()-2)),
                () -> assertThat(bookWithType).extracting(BookWithType::getBook).isEqualTo(books.get(books.size()-3)),
                () -> assertThat(books).filteredOn(book -> book.getTypeId().longValue() == bookTypes[1].getId())
                        .contains(book2),
                () -> assertThat(books).filteredOn(book -> book.getTypeId().longValue() == bookTypes[2].getId())
                        .contains(book3)
        );
    }

    @Test
    @Tag("delete")
    @Timeout(10)
    public void deleteLast() {
        assertFalse(booksPage
                .addBook(bookWithType)
                .deleteBook(booksPage.ids().getLast())
                .contains(bookWithType)
        );
    }

    @Test
    @Tag("delete")
    @Timeout(10)
    public void deleteInTheMiddle() {
        BookWithType bookWithType2 = generateBookWithRandomType();
        BookWithType bookWithType3 = generateBookWithRandomType();
        assertFalse(booksPage
                .addBook(bookWithType)
                .addBook(bookWithType2)
                .addBook(bookWithType3)
                .deleteBook(booksPage.ids().getLast() - 1)
                .contains(bookWithType2)
        );
    }

    @Test
    @Tag("update")
    @Timeout(10)
    public void updateSimple() {
        BookWithType updatedBookWithType = generateBookWithRandomType();
        assertTrue(booksPage
                .addBook(bookWithType)
                .updateBook(booksPage.ids().getLast(), updatedBookWithType)
                .contains(updatedBookWithType)
        );
    }

    @Test
    @Tag("scenario")
    @Timeout(10)
    public void multiOps() {
        BookWithType bookWithType2 = generateBookWithRandomType();
        BookWithType bookWithType3 = generateBookWithRandomType();
        BookWithType bookWithType4 = generateBookWithRandomType();
        BookWithType bookWithType5 = generateBookWithRandomType();
        BookWithType updatedBookWithType = generateBookWithRandomType();
        assertTrue(booksPage
                .addBook(bookWithType)
                .addBook(bookWithType2)
                .deleteBook(booksPage.ids().getLast())
                .addBook(bookWithType3)
                .updateBook(booksPage.ids().getLast(), updatedBookWithType)
                .addBook(bookWithType4)
                .addBook(bookWithType5)
                .contains(updatedBookWithType)
        );
        assertFalse(booksPage.contains(bookWithType2));
        assertFalse(booksPage.contains(bookWithType3));
        assertTrue(booksPage.contains(List.of(bookWithType, bookWithType4, bookWithType5)));
    }

    @ParameterizedTest
    @Timeout(20)
    @DisplayName("Add books with different bookTypes")
    @ValueSource(ints = {0, 1, 2}) //bookTypes indexes
    void addSimple(int number) {
        BookWithType bookWithType2 = generateBookWithType(number);
        assertTrue(booksPage
                .addBook(bookWithType2)
                .contains(bookWithType2)
        );
    }

    public static BookWithType generateBookWithType(int bookTypeIndex) {
        return new BookWithType("" +
                "book" + generateCounter,
                generateCounter + 10,
                bookTypes[bookTypeIndex]
        );
    }

    public static BookWithType generateBookWithRandomType() {
        return new BookWithType("" +
                "book" + generateCounter,
                generateCounter + 10,
                getRandomBookType()
        );
    }

    public static BookType getRandomBookType() {
        int min = 0;
        int max = bookTypes.length;
        return bookTypes[(int) ((Math.random() * (max - min)) + min)];
    }
}