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
  private String name;

  @Column(name = "login")
  private String login;

  @Column(name = "password")
  private String password;

  @Column(name = "role")
  private String role;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "group_id")
  private Group group;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnore
  @JoinTable(name="users_reports",
          joinColumns=  @JoinColumn(name="user_id", referencedColumnName="id"),
          inverseJoinColumns= @JoinColumn(name="report_id", referencedColumnName="report_id") )
  private List<Report> reports;

  public User() {
    this.id = sequence++;
    this.name = "unknown";
    this.login = "unknown";
    this.password = "unknown";
    this.role = "unknown";
    this.group = null;
    this.reports = new ArrayList<>();
  }

  public User(String name) {
    this.id = sequence++;
    this.name = name;
    this.login = name;
    this.password = name;
    this.role = name;
    this.group = null;
    this.reports = new ArrayList<>();
  }

  public User(String name, String role, Group group) {
    this.id = sequence++;
    this.name = name;
    this.login = name;
    this.password = name;
    this.role = role;
    this.group = group;
    this.reports = new ArrayList<>();
  }

  public User(String name, String login, String password, String role, Group group, List<Report> reports) {
    this.id = sequence++;
    this.name = name;
    this.login = login;
    this.password = password;
    this.role = role;
    this.group = group;
    this.reports = reports;
  }

}
