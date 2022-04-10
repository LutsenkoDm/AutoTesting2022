package main.pages;

import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

public class JournalPage extends PageObject {

    public static final String url ="http://localhost:8080/journal";

    public JournalPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void load() {
        driver.get(url);
    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue(driver.getCurrentUrl().contains(url));
    }
}
