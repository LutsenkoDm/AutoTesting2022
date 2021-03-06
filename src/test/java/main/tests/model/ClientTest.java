package main.tests.model;

import main.entity.Client;
import main.pages.ClientsPage;
import main.pages.LoginPage;
import main.valueObjects.FIO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Clients CRUD tests")
public class ClientTest extends FunctionalTest {

    private static ClientsPage clientsPage;
    private static Client client;

    @BeforeClass
    public static void init() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.get();
        loginPage.loginAsDefaultAdmin();
        clientsPage = new ClientsPage(driver);
        clientsPage.get();
    }

    @Before
    public void generate() {
        client = ClientDataProvider.generateNextClient();
    }

    @Test
    @Tag("add")
    public void addSimpleOnlyNameCheck() {
        assertTrue(clientsPage
                .addClient(client)
                .containsFIO(new FIO(client))
        );
    }

    @Test
    @Tag("add")
    @Timeout(10)
    public void addSimple() {
        assertTrue(clientsPage
                .addClient(client)
                .contains(client)
        );
    }

    @Test
    @Tag("add")
    @Timeout(25)
    public void addMultiple() {
        Client client2 = ClientDataProvider.getClientFromDataSet(0);
        Client client3 = ClientDataProvider.getRandomClientFromDataSet();
        assertTrue(clientsPage
                .addClient(client)
                .addClient(client2)
                .addClient(client3)
                .contains(List.of(client, client2, client3))
        );
        List<Client> clients = clientsPage.all();
        List<FIO> fios = clientsPage.fios();
        assertAll(
                () -> assertThat(clients).extracting("firstName", "lastname", "patherName")
                        .contains(tuple(client.getFirstName(), client.getLastName(), client.getPatherName())),
                () -> assertThat(clients).contains(client),
                () -> assertThat(clients).contains(client2),
                () -> assertThat(clients).contains(client3),
                () -> assertThat(clients).contains(client, client2, client3),
                () -> assertThat(fios).contains(new FIO(client)),
                () -> assertThat(fios).contains(new FIO(client2)),
                () -> assertThat(fios).contains(new FIO(client3)),
                () -> assertThat(fios).extracting(FIO::getFirstName).contains(client.getFirstName()),
                () -> assertThat(fios).extracting(FIO::getLastName).contains(client.getLastName()),
                () -> assertThat(fios).extracting(FIO::getPatherName).contains(client.getPatherName()),
                () -> assertThat(clients)
                        .filteredOn(client -> client.getFirstName().equals(ClientDataProvider.clientsDataSet.get(0).getFirstName()))
                        .contains(client2),
                () -> assertThat(clients)
                        .extracting(Client::getFirstName)
                        .filteredOn(firstName -> firstName.equals(ClientDataProvider.clientsDataSet.get(0).getFirstName()))
                        .contains(client2.getFirstName())
        );
    }

    @Test
    @Tag("add")
    @Timeout(10)
    public void addUsingStrategyTest1() {
        containsClientStrategy(clientsPage -> clientsPage.addClient(client), client);
    }

    @Test
    @Tag("add")
    @Timeout(10)
    public void addUsingStrategyTest2() {
        containsClientStrategy(clientsPage ->
                        clientsPage
                                .addClient(client)
                                .deleteClient(clientsPage.ids().getLast())
                                .addClient(client),
                client
        );
    }

    @Test
    @Tag("add")
    @Timeout(10)
    public void addUsingStrategyTest3() {
        containsClientStrategy(clientsPage ->
                        clientsPage
                                .addClient(ClientDataProvider.generateNextClient())
                                .updateClient(clientsPage.ids().getLast(), client),
                client
        );
    }

    private void containsClientStrategy(Consumer<ClientsPage> consumer, Client client) {
        consumer.accept(clientsPage);
        assertTrue(clientsPage.contains(client));
        assertTrue(clientsPage.containsFIO(new FIO(client)));
    }

    @Test
    @Tag("delete")
    @Timeout(10)
    public void deleteLast() {
        assertFalse(clientsPage
                .addClient(client)
                .deleteClient(clientsPage.ids().getLast())
                .contains(client)
        );
    }

    @Test
    @Tag("delete")
    @Timeout(10)
    public void deleteInTheMiddle() {
        Client client2 = ClientDataProvider.getClientFromDataSet(1);
        Client client3 = ClientDataProvider.getRandomClientFromDataSet();
        assertFalse(clientsPage
                .addClient(client)
                .addClient(client2)
                .addClient(client3)
                .deleteClient(clientsPage.ids().getLast() - 1)
                .contains(client2)
        );
    }

    @Test
    @Tag("update")
    @Timeout(10)
    public void updateSimple() {
        Client client2 = ClientDataProvider.getClientFromDataSet(2);
        assertTrue(clientsPage
                .addClient(client)
                .updateClient(clientsPage.ids().getLast(), client2)
                .contains(client2)
        );
    }

    @Test
    @Tag("scenario")
    @Timeout(10)
    public void multiOps() {
        Client client2 = ClientDataProvider.generateNextClient();
        Client client3 = ClientDataProvider.generateNextClient();
        Client client4 = ClientDataProvider.generateNextClient();
        Client client5 = ClientDataProvider.generateNextClient();
        Client updatedClient = ClientDataProvider.generateNextClient();
        assertTrue(clientsPage
                .addClient(client)
                .addClient(client2)
                .deleteClient(clientsPage.ids().getLast())
                .addClient(client3)
                .updateClient(clientsPage.ids().getLast(), updatedClient)
                .addClient(client4)
                .addClient(client5)
                .contains(updatedClient)
        );
        assertFalse(clientsPage.contains(client2));
        assertFalse(clientsPage.contains(client3));
        assertTrue(clientsPage.contains(List.of(client, client4, client5)));
    }

    private static class ClientDataProvider {

        private static int generateCounter = 0;
        public static final List<Client> clientsDataSet = List.of(
                new Client("Alex", "Alexov", "ALexovich", "1111", "22334455"),
                new Client("John", "Johnson", "Johnsovich", "2222", "9662052"),
                new Client("Steve", "Stevov", "Stevovich", "3333", "82653662")
        );

        public static Client getClientFromDataSet(int index) {
            return clientsDataSet.get(index);
        }

        public static Client getRandomClientFromDataSet() {
            int min = 0;
            int max = clientsDataSet.size();
            return clientsDataSet.get((int) ((Math.random() * (max - min)) + min));
        }

        public static Client generateNextClient() {
            generateCounter++;
            return new Client(
                    "firstName" + generateCounter,
                    "lastName" + generateCounter,
                    "patherName" + generateCounter,
                    "passportSeria" + generateCounter,
                    "passportNum" + generateCounter
            );
        }
    }


}