package ru.gb.model;

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
@ToString(exclude = {"users", "reports"})
@Schema(name = "классы")
public class Group {

    public static long sequence = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Идентификатор")
    private Long id;

    @Column(name = "название класса")
    private String name;

    @Column(name = "год обучения")
    private String studyYear;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "group")
    private List<User> users;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "group")
    private List<Report> reports;

    public Group() {
        this.id = sequence++;
        this.name = "";
        this.studyYear = "";
        this.users = new ArrayList<>();
        this.reports = new ArrayList<>();
    }

    public Group(String name, String studyYear) {
        this.id = sequence++;
        this.name = name;
        this.studyYear = studyYear;
        this.users = new ArrayList<>();
        this.reports = new ArrayList<>();
    }

    public Group(String name, String studyYear, List<User> users, List<Report> reports) {
        this.id = sequence++;
        this.name = name;
        this.studyYear = studyYear;
        this.users = users;
        this.reports = reports;
    }

    public Group(Long id, String name, String studyYear, List<User> users, List<Report> reports) {
        this.id = id;
        this.name = name;
        this.studyYear = studyYear;
        this.users = users;
        this.reports = reports;
    }

    void addPerson (User user) {
        users.add(user);
    }
}
