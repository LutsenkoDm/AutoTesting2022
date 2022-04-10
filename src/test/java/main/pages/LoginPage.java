package main.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.junit.Assert.assertTrue;

public class LoginPage extends PageObject {

    public static final String url ="http://localhost:8080/login";
    public static final String DEFAULT_ADMIN_USERNAME ="admin";
    public static final String DEFAULT_ADMIN_PASSWORD ="123";

    @FindBy(id="username")
    private WebElement username ;

    @FindBy(id="password")
    private WebElement password ;

    @FindBy(xpath="//button[contains(text(), \"Sign in\")]")
    private WebElement submitButton;

    public LoginPage(WebDriver driver) {
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

    public boolean isInitialized() {
        return username.isDisplayed() && password.isDisplayed() && submitButton.isDisplayed();
    }

    public void enterCredential(String username, String password){
        this.username.clear();
        this.username.sendKeys(username);
        this.password.clear();
        this.password.sendKeys(password);
    }

    public JournalPage submit(){
        submitButton.click();
        return new JournalPage(driver);
    }

    public void loginAsDefaultAdmin() {
        enterCredential(DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD);
        submit();
    }
}
