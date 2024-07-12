package ru.gb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reports")
@ToString(exclude = "users")
@Data
@Schema(name = "Отчеты")
public class Report {
    public static long sequence = 1L;

    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Идентификатор")
    private final Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "group_id")
    private final Group group;

    @Column(name = "date")
    @Schema(name = "Дата отчета")
    private final LocalDateTime date;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="users_reports",
            joinColumns = @JoinColumn(name="report_id", referencedColumnName="report_id"),
            inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName="id") )
    private final List<User> users;

    public Report() {
        this.id = sequence++;
        this.group = null;
        this.date = LocalDateTime.now().withNano(0);
        this.users = new ArrayList<>();
    }

    public Report(List<User> users) {
        this.id = sequence++;
        this.group = null;
        this.date = LocalDateTime.now().withNano(0);
        this.users = users;
    }

    public Report(Group group, List<User> users) {
        this.id = sequence++;
        this.group = group;
        this.date = LocalDateTime.now().withNano(0);
        this.users = users;
    }
}

