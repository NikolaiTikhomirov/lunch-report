package ru.gb.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reports")
@Data
//@RequiredArgsConstructor
@Schema(name = "Отчеты")
public class Report {
    public static long sequence = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Идентификатор")
    private final Long id;

    @Column(name = "Group")
    @Schema(name = "Группа")
    private final Group group;

    @Column(name = "date")
    @Schema(name = "Дата отчета")
    private final LocalDateTime date;

    @Column(name = "users_list")
    @Schema(name = "Список пользователей")
    private final List<User> users;

    public Report() {
        this.id = sequence++;
        this.group = null;
        this.date = LocalDateTime.now();
        this.users = null;
    }

    public Report(Group group, List<User> users) {
        this.id = sequence++;
        this.group = group;
        this.date = LocalDateTime.now();
        this.users = users;
    }
}
