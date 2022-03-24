package main;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTest {

    @Test
    public void myLoginTest() {
        System.setProperty("webdriver.chrome.driver", "src/test/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:8080/login");
        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement login = driver.findElement(By.xpath("//button[contains(text(), \"Sign in\")]"));
        username.sendKeys("admin");
        password.sendKeys("123");
        login.click();
        String expectedUrl = "http://localhost:8080/journal";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(actualUrl, expectedUrl);
        assertEquals(1, 1);
    }
}
