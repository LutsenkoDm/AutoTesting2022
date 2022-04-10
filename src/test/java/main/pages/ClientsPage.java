package main.pages;

import main.builders.ClientBuilder;
import main.entity.Client;
import main.valueObjects.FIO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClientsPage extends PageObject {

    public static final String url = "http://localhost:8080/clients";
    private final ClientsTable clientsTable = new ClientsTable();

    @FindBy(id = "FirstName")
    private WebElement firstName;

    @FindBy(id = "LastName")
    private WebElement lastName;

    @FindBy(id = "PatherName")
    private WebElement patherName;

    @FindBy(id = "PassportSeria")
    private WebElement passportSeria;

    @FindBy(id = "PassportNum")
    private WebElement passportNum;

    @FindBy(id = "idForDelete")
    private WebElement idForDelete;

    @FindBy(id = "IdOfUpdatedClient")
    private WebElement idOfUpdatedClient;

    @FindBy(id = "FirstNameToUpdate")
    private WebElement firstNameToUpdate;

    @FindBy(id = "LastNameToUpdate")
    private WebElement lastNameToUpdate;

    @FindBy(name = "patherNameToUpdate")
    private WebElement patherNameToUpdate;

    @FindBy(name = "passportSeriaToUpdate")
    private WebElement passportSeriaToUpdate;

    @FindBy(name = "passportNumToUpdate")
    private WebElement passportNumToUpdate;

    @FindBy(name = "subm")
    private WebElement addSubmitButton;

    @FindBy(name = "deleteSubmit")
    private WebElement deleteSubmitButton;

    @FindBy(name = "updateSubmit")
    private WebElement updateSubmitButton;

    @FindBy(id = "table")
    private WebElement table;

    public ClientsPage(WebDriver driver) {
        super(driver);
    }

    public ClientsPage addClient(Client client) {
        firstName.clear();
        lastName.clear();
        patherName.clear();
        passportSeria.clear();
        passportNum.clear();

        firstName.sendKeys(client.getFirstName());
        lastName.sendKeys(client.getLastName());
        patherName.sendKeys(client.getPatherName());
        passportSeria.sendKeys(client.getPassportSeria());
        passportNum.sendKeys(client.getPassportNum());
        addSubmitButton.click();
        return this;
    }

    public ClientsPage deleteClient(Long id) {
        idForDelete.sendKeys(id.toString());
        deleteSubmitButton.click();
        return this;
    }

    public ClientsPage updateClient(Long id, Client client) {
        idForDelete.clear();
        firstName.clear();
        lastName.clear();
        patherName.clear();
        passportSeria.clear();
        passportNum.clear();

        idOfUpdatedClient.sendKeys(id.toString());
        firstNameToUpdate.sendKeys(client.getFirstName());
        lastNameToUpdate.sendKeys(client.getLastName());
        patherNameToUpdate.sendKeys(client.getPatherName());
        passportSeriaToUpdate.sendKeys(client.getPassportSeria());
        passportNumToUpdate.sendKeys(client.getPassportNum());
        updateSubmitButton.click();
        return this;
    }

    public boolean contains(Client client) {
        return clientsTable.elements().contains(client);
    }

    public boolean contains(List<Client> clients) {
        return clientsTable.elements().containsAll(clients);
    }

    public boolean containsFIO(FIO fio) {
        return clientsTable.FIOs().contains(fio);
    }

    public boolean containsFIOs(List<FIO> fios) {
        return clientsTable.FIOs().containsAll(fios);
    }

    public LinkedList<Long> ids() {
        return clientsTable.ids();
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

    class ClientsTable {
        public List<Client> elements() {
            List<Client> clients = new ArrayList<>();
            List<WebElement> trs = findTrs();
            for (WebElement tr : trs) {
                List<WebElement> tds = tr.findElements(By.tagName("td"));
                Client client = new ClientBuilder()
                        .setFirstName(tds.get(1).getText())
                        .setLastName(tds.get(2).getText())
                        .setPatherName(tds.get(3).getText())
                        .setPassportSeria(tds.get(4).getText())
                        .setPassportNum(tds.get(5).getText())
                        .build();
                clients.add(client);
            }
            return clients;
        }

        public LinkedList<Long> ids() {
            LinkedList<Long> ids = new LinkedList<>();
            List<WebElement> trs = findTrs();
            for (WebElement tr : trs) {
                ids.add(Long.valueOf(tr.findElement(By.xpath("./td[1]")).getText()));
            }
            return ids;
        }

        public List<FIO> FIOs() {
            List<FIO> fios = new ArrayList<>();
            List<WebElement> trs = findTrs();
            for (WebElement tr : trs) {
                fios.add(new FIO(
                        tr.findElement(By.xpath("./td[2]")).getText(),
                        tr.findElement(By.xpath("./td[3]")).getText(),
                        tr.findElement(By.xpath("./td[4]")).getText())
                );
            }
            return fios;
        }

        private List<WebElement> findTrs() {
            return table.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
        }
    }
}
