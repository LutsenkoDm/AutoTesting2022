package main.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "books")
public class Book {

    public Book() {
    }

    public Book(String name, Long cnt, Long typeId) {
        this.name = name;
        this.cnt = cnt;
        this.typeId = typeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(name, book.name) && Objects.equals(cnt, book.cnt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cnt);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cnt")
    private Long cnt;

    @Column(name = "typeId")
    private Long typeId;

    @OneToMany(targetEntity = JournalRecord.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "bookId", referencedColumnName = "id")
    private Set<JournalRecord> journalRecords = new HashSet<>();
}
