package ru.gb.api;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import com.gb.aspect.TimeCounter;
import api.src.main.java.ru.gb.api.User;

import java.util.List;
import java.util.Objects;

@TimeCounter
@Controller
@RequestMapping("/ui")
public class SimpleController {
    private final WebClient webClient;

    public SimpleController(ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.filter(loadBalancerExchangeFilterFunction).build();
    }

    @GetMapping("/users")
    public String getUsers (Model model) {
        List<User> users = Objects.requireNonNull(webClient.get()
                        .uri("http://user-service/user")
                        .retrieve()
                        .toEntityList(User.class)
                        .block())
                .getBody();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/home")
    public String getHomePage (Model model) {
        model.addAttribute("home");
        return "home";
    }
}
