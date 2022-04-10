package main.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "clients")
public class Client {
    public Client() {
    }

    public Client(String firstName, String lastName, String patherName, String passportSeria, String passportNum) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patherName = patherName;
        this.passportSeria = passportSeria;
        this.passportNum = passportNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return firstName.equals(client.firstName) && lastName.equals(client.lastName) && Objects.equals(patherName, client.patherName) && Objects.equals(passportSeria, client.passportSeria) && Objects.equals(passportNum, client.passportNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patherName, passportSeria, passportNum);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "patherName")
    private String patherName;

    @Column(name = "passportSeria")
    private String passportSeria;

    @Column(name = "passportNum")
    private String passportNum;

    @OneToMany(targetEntity = JournalRecord.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "clientId", referencedColumnName = "id")
    private Set<JournalRecord> journalRecords = new HashSet<>();
}
