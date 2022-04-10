package main.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

public abstract class PageObject extends LoadableComponent<PageObject> {

    protected WebDriver driver;

    public PageObject(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public PageObject get() {
        try {
            this.isLoaded();
            return this;
        } catch (Error var2) {
            this.load();
            this.isLoaded();
            return this;
        }
    }

    @Override
    abstract protected void load();

    @Override
    abstract protected void isLoaded() throws Error;
}