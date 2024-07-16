package ru.gb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User getUserById(long id);

  Optional<User> findByLogin(String login);

  Optional<User> findByName(String name);
}
