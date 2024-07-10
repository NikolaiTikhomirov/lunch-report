package ru.gb.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.model.User;
import ru.gb.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RestController
@RequestMapping("/user")
@Tag(name = "School")
public class UserController {
    private final UserRepository userRepository;

    public UserController(ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction, UserRepository userRepository, WebClient.Builder webClientBuilder) {
        this.userRepository = userRepository;
        WebClient webClient = webClientBuilder
                .filter(loadBalancerExchangeFilterFunction)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "get user by id", description = "Получить пользователя по его ID")
    public User getUserById (@PathVariable Long id) {
        return userRepository.getUserById(id);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "get user by name", description = "Получить пользователя по его имени")
    public Optional<User> getUserByName (@PathVariable String name) {
        return userRepository.findByLogin(name);
    }

    @GetMapping()
    @Operation(summary = "get all users", description = "Получить список всех пользователей")
    public List<User> getAllUsers () {
        return userRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "add user", description = "Добавить пользователя")
    public User addUser (@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "delete user by id", description = "Удалить пользователя по его ID")
    public void deleteUserById (@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
