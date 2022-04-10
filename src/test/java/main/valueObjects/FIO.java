package main.valueObjects;


import main.entity.Client;

import java.util.Objects;

public class FIO {

    private final String firstName;
    private final String lastName;
    private final String patherName;

    public FIO(Client client) {
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.patherName = client.getPatherName();
    }

    public FIO(String firstName, String lastName, String patherName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patherName = patherName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FIO fio = (FIO) o;
        return firstName.equals(fio.firstName) && lastName.equals(fio.lastName) && Objects.equals(patherName, fio.patherName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patherName);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatherName() {
        return patherName;
    }
}
