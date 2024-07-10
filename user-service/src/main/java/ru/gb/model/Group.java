package ru.gb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@Data
@ToString(exclude = "users")
@Schema(name = "классы")
public class Group {

    public static long sequence = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Идентификатор")
    private final Long id;
    @Column(name = "название класса")
    private final String name;

    @Column(name = "год обучения")
    private final String studyYear;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private final List<User> users;

    public Group() {
        this.id = sequence++;
        this.name = "";
        this.studyYear = "";
        this.users = new ArrayList<>();
    }

    public Group(String name, String studyYear) {
        this.id = sequence++;
        this.name = name;
        this.studyYear = studyYear;
        this.users = new ArrayList<>();
    }

    void addPerson (User user) {
        users.add(user);
    }
}
