package main.pages;

import main.builders.BookBuilder;
import main.entity.Book;
import main.valueObjects.BookWithType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BooksPage extends PageObject {

    public static final String url = "http://localhost:8080/books";
    private final BooksTable booksTable = new BooksTable();

    @FindBy(id = "Name")
    private WebElement name;

    @FindBy(id = "Cnt")
    private WebElement cnt;

    @FindBy(id = "bookTypesSelectorAdd")
    private WebElement bookTypesSelectorAdd;

    @FindBy(id = "idForDelete")
    private WebElement idForDelete;

    @FindBy(id = "IdOfUpdatedBook")
    private WebElement idOfUpdatedBook;

    @FindBy(id = "NameToUpdate")
    private WebElement nameToUpdate;

    @FindBy(id = "CntToUpdate")
    private WebElement cntToUpdate;

    @FindBy(name = "bookTypesSelectorUpdate")
    private WebElement bookTypesSelectorUpdate;

    @FindBy(name = "submit")
    private WebElement addSubmitButton;

    @FindBy(name = "deleteSubmit")
    private WebElement deleteSubmitButton;

    @FindBy(name = "updateSubmit")
    private WebElement updateSubmitButton;

    @FindBy(id = "table")
    private WebElement table;

    public BooksPage(WebDriver driver) {
        super(driver);
    }

    public BooksPage addBook(BookWithType bookWithType) {
        name.clear();
        cnt.clear();
        name.sendKeys(bookWithType.getName());
        cnt.sendKeys(bookWithType.getCnt().toString());
        bookTypesSelectorAdd.click();
        // Select не получилось использовать так как подгрузка данных происходит после click
        // selectByValue не срабатывал даже после клика выше
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@id=\"bookTypesSelectorAdd\"]//option"), 1));
        bookTypesSelectorAdd.findElement(By.xpath("./option[contains(text(), " + bookWithType.getTypeName() + ")]")).click();
        addSubmitButton.click();
        return this;
    }

    public BooksPage deleteBook(Long id) {
        idForDelete.sendKeys(id.toString());
        deleteSubmitButton.click();
        return this;
    }

    public BooksPage updateBook(Long id, BookWithType bookWithType) {
        idOfUpdatedBook.clear();
        nameToUpdate.clear();
        cntToUpdate.clear();
        idOfUpdatedBook.sendKeys(id.toString());
        nameToUpdate.sendKeys(bookWithType.getName());
        cntToUpdate.sendKeys(bookWithType.getCnt().toString());
        bookTypesSelectorUpdate.click();
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[@id=\"bookTypesSelectorAdd\"]//option"), 1));
        bookTypesSelectorUpdate.findElement(By.xpath("./option[contains(text(), " + bookWithType.getTypeName() + ")]")).click();
        updateSubmitButton.click();
        return this;
    }

    public boolean contains(BookWithType bookWithType) {
        return booksTable.elements().contains(bookWithType.getBook());
    }

    public boolean contains(List<BookWithType> bookWithTypes) {
        List<Book> books = bookWithTypes.stream().map(BookWithType::getBook).collect(Collectors.toList());
        return booksTable.elements().containsAll(books);
    }

    public boolean containsName(String name) {
        return booksTable.names().contains(name);
    }

    public boolean containsNames(List<String> names) {
        return booksTable.names().containsAll(names);
    }

    public LinkedList<Long> ids() {
        return booksTable.ids();
    }

    @Override
    protected void load() {
        driver.get(url);
    }

    @Override
    protected void isLoaded() throws Error {
        assertEquals(url, driver.getCurrentUrl());
        assertTrue(table.isDisplayed() && addSubmitButton.isDisplayed()
                && deleteSubmitButton.isDisplayed() && updateSubmitButton.isDisplayed());
    }

    class BooksTable {
        public List<Book> elements() {
            List<Book> books = new ArrayList<>();
            List<WebElement> trs = findTrs();
            for (WebElement tr : trs) {
                List<WebElement> tds = tr.findElements(By.tagName("td"));
                Book book = new BookBuilder()
                        .setName(tds.get(1).getText())
                        .setCnt(Long.valueOf(tds.get(2).getText()))
                        .setTypeId(Long.valueOf(tds.get(3).getText()))
                        .build();
                books.add(book);
            }
            return books;
        }

        public LinkedList<Long> ids() {
            LinkedList<Long> ids = new LinkedList<>();
            List<WebElement> trs = findTrs();
            for (WebElement tr : trs) {
                ids.add(Long.valueOf(tr.findElement(By.xpath("./td[1]")).getText()));
            }
            return ids;
        }

        public List<String> names() {
            List<String> names = new ArrayList<>();
            List<WebElement> trs = findTrs();
            for (WebElement tr : trs) {
                names.add(tr.findElement(By.xpath("./td[2]")).getText());
            }
            return names;
        }

        private List<WebElement> findTrs() {
            return table.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
        }
    }
}
