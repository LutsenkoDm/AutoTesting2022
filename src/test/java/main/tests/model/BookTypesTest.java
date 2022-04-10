package main.tests.model;

import main.entity.BookType;
import main.pages.BookTypesPage;
import main.pages.LoginPage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Disabled
@DisplayName("Book types CRUD tests")
public class BookTypesTest extends FunctionalTest {

    private static BookTypesPage bookTypesPage;
    private final BookType bookType1 = new BookType("name1", 1L,11L,111L);
    private final BookType bookType2 = new BookType("name2", 2L,22L,222L);
    private final BookType bookType3 = new BookType("name3", 3L,33L,333L);
    private final BookType bookType4 = new BookType("name4", 4L,44L,444L);
    private final BookType bookType5 = new BookType("name5", 5L,55L,555L);
    private final BookType bookType6 = new BookType("name6", 5L,55L,555L);


    @BeforeClass
    public static void login() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.get();
        loginPage.loginAsDefaultAdmin();
        bookTypesPage = new BookTypesPage(driver);
        bookTypesPage.get();
    }

    @Test
    @Tag("add")
    public void addSimpleOnlyNameCheck(){
        assertTrue(bookTypesPage
                .addBookType(bookType1)
                .containsName(bookType1.getName())
        );
    }

    @Test
    @Tag("add")
    public void addMultipleOnlyNameCheck(){
        assertTrue(bookTypesPage
                .addBookType(bookType1)
                .addBookType(bookType2)
                .addBookType(bookType3)
                .containsNames(List.of(bookType1.getName(), bookType2.getName(), bookType3.getName()))
        );
    }

    @Test
    @Tag("add")
    public void addSimple(){
        assertTrue(bookTypesPage
                .addBookType(bookType1)
                .contains(bookType1)
        );
    }

    @Test
    @Tag("add")
    public void addMultiple(){
        assertTrue(bookTypesPage
                .addBookType(bookType1)
                .addBookType(bookType2)
                .addBookType(bookType3)
                .contains(List.of(bookType1, bookType2, bookType3))
        );
    }

    @Test
    @Tag("delete")
    public void delete(){
        assertFalse(bookTypesPage
                .addBookType(bookType4)
                .deleteBookType(bookTypesPage.ids().getLast())
                .contains(bookType4)
        );
    }

    @Test
    @Tag("update")
    public void updateSimple(){
        assertTrue(bookTypesPage
                .addBookType(bookType5)
                .updateBookType(bookTypesPage.ids().getLast(), bookType6)
                .contains(bookType6)
        );
    }
}