package ru.gb.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@RequiredArgsConstructor
@Schema(name = "Пользователь")
public class SimpleUser {

  public static long sequence = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(name = "Идентификатор")
  private final Long id;

  @Column(name = "name")
  @Schema(name = "Имя")
  private final String name;

  @Column(name = "role")
  private final String role;

//  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//  private final List<User> lunch;


  public SimpleUser() {
    this(sequence++, "unknown", "unknown");
  }

  public SimpleUser(String name) {
    this(sequence++, name, name);
  }

  public SimpleUser(String name, String role) {
    this(sequence++, name, role);
  }


}
