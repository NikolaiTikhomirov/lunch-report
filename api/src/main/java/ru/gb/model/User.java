package ru.gb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@ToString(exclude = "reports")
@RequiredArgsConstructor
@Schema(name = "Пользователь")
public class User {

  public static long sequence = 1L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(name = "Идентификатор")
  private final Long id;

  @Column(name = "name")
  @Schema(name = "Имя")
  private final String name;

  @Column(name = "login")
  private final String login;

  @Column(name = "password")
  private final String password;

  @Column(name = "role")
  private final String role;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "group_id")
  private final Group group;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnore
  @JoinTable(name="users_reports",
          joinColumns=  @JoinColumn(name="user_id", referencedColumnName="id"),
          inverseJoinColumns= @JoinColumn(name="report_id", referencedColumnName="report_id") )
  private final List<Report> reports;

  public User() {
    this(sequence++, "unknown", "unknown", "unknown", "unknown", null, new ArrayList<>());
  }

  public User(String name) {
    this(sequence++, name, name, name, name, null, new ArrayList<>());
  }

  public User(String name, String role, Group group) {
    this(sequence++, name, name, name, role, group, new ArrayList<>());
  }

  public User(String name, String login, String password, String role, Group group, List<Report> reports) {
    this(sequence++, name, login, password, role, group, reports);
  }

//  public User(Long id, String name, String login, String password, String role, Group group, Boolean lunch) {
//    this(id, name, login, password, role, group, lunch);
//  }

}
