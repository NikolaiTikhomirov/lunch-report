package api.src.main.java.ru.gb.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import api.src.main.java.ru.gb.api.User;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private List<User> users;

    public Group() {
        this.id = sequence++;
        this.name = "";
        this.studyYear = "";
    }

    public Group(String name, String studyYear) {
        this.id = sequence++;
        this.name = name;
        this.studyYear = studyYear;
    }
}
