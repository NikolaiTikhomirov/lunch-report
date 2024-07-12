package ru.gb.api;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.model.User;
import ru.gb.repository.UserRepository;

import java.util.List;
import java.util.Objects;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class UserControllerTest {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserRepository userRepository;

    @Data
    static class JUnitReaderRequest {
        private String name;
        private String login;
        private String password;
        private String role;

        public JUnitReaderRequest(String name) {
            this.name = name;
            this.login = name;
            this.password = name;
            this.role = name;
        }
    }

    @Test
    void getReaderByIdSuccess() {
        User expected = userRepository.save(new User("just reader"));

        User responseBody = webTestClient.get()
                .uri("/reader/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getName(), responseBody.getName());
        Assertions.assertEquals(expected.getLogin(), responseBody.getLogin());
        Assertions.assertEquals(expected.getPassword(), responseBody.getPassword());
        Assertions.assertEquals(expected.getRole(), responseBody.getRole());
    }

    @Test
    void testFindByIdNotFound() {
        Long maxId = jdbcTemplate.queryForObject("select max(id) from readers", Long.class);

        webTestClient.get()
                .uri("/readers/" + maxId + 1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getReaderByName() {
        User expected = userRepository.save(new User("superReader"));

        User responseBody = webTestClient.get()
                .uri("/reader/name/" + expected.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getName(), responseBody.getName());
        Assertions.assertEquals(expected.getLogin(), responseBody.getLogin());
        Assertions.assertEquals(expected.getPassword(), responseBody.getPassword());
        Assertions.assertEquals(expected.getRole(), responseBody.getRole());
    }

    @Test
    void getAllReaders() {
        userRepository.saveAll(List.of(
                new User("first"),
                new User("second")
        ));

        List<User> expected = userRepository.findAll();

        List<User> responseBody = webTestClient.get()
                .uri("/reader")
                // .retrieve
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<User>>() {
                })
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(expected.size(), responseBody.size());
        for (User customerResponse : responseBody) {
            boolean found = expected.stream()
                    .filter(it -> Objects.equals(it.getId(), customerResponse.getId()))
                    .filter(it -> Objects.equals(it.getName(), customerResponse.getName()))
                    .filter(it -> Objects.equals(it.getLogin(), customerResponse.getLogin()))
                    .filter(it -> Objects.equals(it.getPassword(), customerResponse.getPassword()))
                    .anyMatch(it -> Objects.equals(it.getRole(), customerResponse.getRole()));
            Assertions.assertTrue(found);
        }
    }

    @Test
    void addReader() {
        JUnitReaderRequest request = new JUnitReaderRequest("anyReader");

        User responseBody = webTestClient.post()
                .uri("/reader")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(request.getName(), responseBody.getName());
        Assertions.assertEquals(request.getLogin(), responseBody.getLogin());
        Assertions.assertEquals(request.getPassword(), responseBody.getPassword());
        Assertions.assertEquals(request.getRole(), responseBody.getRole());

        Assertions.assertTrue(userRepository.findById(responseBody.getId()).isPresent());
    }

    @Test
    void deleteReaderById() {
        User request = userRepository.save(new User("reader to delete"));

        webTestClient.delete()
                .uri("/reader/" + request.getId())
                .exchange()
                .expectStatus().isOk();

        Assertions.assertTrue(userRepository.findById(request.getId()).isEmpty());
    }
}