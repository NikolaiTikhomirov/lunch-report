package api.src.main.java.ru.gb.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import api.src.main.java.ru.gb.api.Group;

@Entity
@Table(name = "users")
@Data
@RequiredArgsConstructor
@Schema(name = "Пользователь")
public class User {

  public static long sequence = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(name = "Идентификатор")
  private final long id;

  @Column(name = "name")
  @Schema(name = "Имя")
  private final String name;

  @Column(name = "login")
  private final String login;

  @Column(name = "password")
  private final String password;

  @Column(name = "role")
  private final String role;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn()
  private final Group group;

  public User() {
    this(sequence++, "unknown", "unknown", "unknown", "unknown", null);
  }

  public User(String name) {
    this(sequence++, name, name, name, name, null);
  }

  public User(String name, String role, Group group) {
    this(sequence++, name, name, name, role, group);
  }

  public User(String name, String login, String password, String role, Group group) {
    this(sequence++, name, login, password, role, group);
  }

}
