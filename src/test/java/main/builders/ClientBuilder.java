package main.builders;

import main.entity.Client;

public class ClientBuilder {

    private final Client client;

    public ClientBuilder() {
        client = new Client();
    }

    public ClientBuilder setFirstName(String firstName) {
        client.setFirstName(firstName);
        return this;
    }

    public ClientBuilder setLastName(String lastName) {
        client.setLastName(lastName);
        return this;
    }

    public ClientBuilder setPatherName(String patherName) {
        client.setPatherName(patherName);
        return this;
    }

    public ClientBuilder setPassportSeria(String passportSeria) {
        client.setPassportSeria(passportSeria);
        return this;
    }

    public ClientBuilder setPassportNum(String passportNum) {
        client.setPassportNum(passportNum);
        return this;
    }

    public Client build() {
        return client;
    }
}
