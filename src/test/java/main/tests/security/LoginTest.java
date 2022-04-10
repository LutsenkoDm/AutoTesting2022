package main.tests.security;

import main.pages.JournalPage;
import main.pages.LoginPage;
import main.tests.model.FunctionalTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginTest extends FunctionalTest {

    @Test
    public void login() {
        driver.get(LoginPage.url);

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isInitialized());

        loginPage.enterCredential(
                AdminCredsSingleton.get().getUsername(),
                AdminCredsSingleton.get().getPassword()
        );

        JournalPage journalPage = loginPage.submit();
        assertEquals(JournalPage.url, driver.getCurrentUrl());
    }

    static class AdminCredsSingleton {

        private static AdminCredsSingleton instance = null;

        public String username;
        public String password;

        private AdminCredsSingleton() {
            username = "admin";
            password = "123";
        }

        public static AdminCredsSingleton get() {
            if (instance == null) {
                instance = new AdminCredsSingleton();
            }
            return instance;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}