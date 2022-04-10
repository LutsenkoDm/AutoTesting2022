package main.pages;

import main.builders.BookTypeBuilder;
import main.entity.BookType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class BookTypesPage extends PageObject {

    public static final String url = "http://localhost:8080/bookTypes";
    private final BookTypesTable bookTypesTable = new BookTypesTable();

    @FindBy(id = "Name")
    private WebElement name;

    @FindBy(id = "Cnt")
    private WebElement cnt;

    @FindBy(id = "Fine")
    private WebElement fine;

    @FindBy(id = "DayCount")
    private WebElement dayCount;

    @FindBy(id = "idForDelete")
    private WebElement idForDelete;

    @FindBy(id = "IdOfUpdatedBookType")
    private WebElement idOfUpdatedBookType;

    @FindBy(id = "NameToUpdate")
    private WebElement nameToUpdate;

    @FindBy(id = "CntToUpdate")
    private WebElement cntToUpdate;

    @FindBy(id = "FineToUpdate")
    private WebElement fineToUpdate;

    @FindBy(id = "DayCountToUpdate")
    private WebElement dayCountToUpdate;

    @FindBy(name = "addSubmit")
    private WebElement addSubmitButton;

    @FindBy(name = "deleteSubmit")
    private WebElement deleteSubmitButton;

    @FindBy(name = "updateSubmit")
    private WebElement updateSubmitButton;

    @FindBy(id = "table")
    private WebElement table;

    public BookTypesPage(WebDriver driver) {
        super(driver);
    }

    public BookTypesPage addBookType(BookType bookType) {
        name.clear();
        cnt.clear();
        fine.clear();
        dayCount.clear();
        name.sendKeys(bookType.getName());
        cnt.sendKeys(bookType.getCnt().toString());
        fine.sendKeys(bookType.getFine().toString());
        dayCount.sendKeys(bookType.getDayCount().toString());
        addSubmitButton.click();
        return this;
    }

    public BookTypesPage deleteBookType(Long id) {
        idForDelete.sendKeys(id.toString());
        deleteSubmitButton.click();
        return this;
    }

    public BookTypesPage updateBookType(Long id, BookType newBookType) {
        idOfUpdatedBookType.clear();
        nameToUpdate.clear();
        cntToUpdate.clear();
        fineToUpdate.clear();
        dayCountToUpdate.clear();
        idOfUpdatedBookType.sendKeys(id.toString());
        nameToUpdate.sendKeys(newBookType.getName());
        cntToUpdate.sendKeys(newBookType.getCnt().toString());
        fineToUpdate.sendKeys(newBookType.getFine().toString());
        dayCountToUpdate.sendKeys(newBookType.getDayCount().toString());
        updateSubmitButton.click();
        return this;
    }

    public boolean contains(BookType bookType) {
        return bookTypesTable.elements().contains(bookType);
    }

    public boolean contains(List<BookType> bookTypes) {
        return bookTypesTable.elements().containsAll(bookTypes);
    }

    public boolean containsName(String name) {
        return bookTypesTable.names().contains(name);
    }

    public boolean containsNames(List<String> names) {
        return bookTypesTable.names().containsAll(names);
    }

    public LinkedList<Long> ids() {
        return bookTypesTable.ids();
    }

    @Override
    protected void load() {
        driver.get(url);
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue(driver.getCurrentUrl().contains(url));
        assertTrue(table.isDisplayed() && addSubmitButton.isDisplayed()
                && deleteSubmitButton.isDisplayed() && updateSubmitButton.isDisplayed());
    }

    class BookTypesTable {

        public List<BookType> elements() {
            List<BookType> bookTypes = new ArrayList<>();
            List<WebElement> trs = findTrs();
            for (WebElement tr : trs) {
                List<WebElement> tds = tr.findElements(By.tagName("td"));
                BookType bookType = new BookTypeBuilder()
                        .setName(tds.get(1).getText())
                        .setCnt(Long.valueOf(tds.get(2).getText()))
                        .setFine(Long.valueOf(tds.get(3).getText()))
                        .setDayCount(Long.valueOf(tds.get(4).getText()))
                        .build();
                bookTypes.add(bookType);
            }
            return bookTypes;
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
