package ru.gb.api;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.model.SimpleUser;
import ru.gb.model.User;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/ui")
public class SimpleController {
    private final WebClient webClient;

    public SimpleController(ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.filter(loadBalancerExchangeFilterFunction).build();
    }

    @GetMapping("/users")
    public String getUsers (Model model) {
        try {
            List<SimpleUser> users = Objects.requireNonNull(webClient.get()
                            .uri("http://user-service/user")
                            .retrieve()
                            .toEntityList(SimpleUser.class)
                            .block())
                    .getBody();
            model.addAttribute("users", users);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return "users";
    }

    @GetMapping("/home")
    public String getHomePage (Model model) {
        model.addAttribute("home");
        return "home";
    }

    @GetMapping("/addreport")
    public String addReportPage (Model model) {
        model.addAttribute("addReportPage");
        return "addreport";
    }

    @GetMapping("/reports")
    public String reportsPage (Model model) {
        model.addAttribute("reportsPage");
        return "reports";
    }

    @GetMapping("/adduser")
    public String addUserPage (Model model) {
        model.addAttribute("addUserPage");
        return "adduser";
    }

    @GetMapping("/addgroup")
    public String addGroupPage (Model model) {
        model.addAttribute("addGroupPage");
        return "addgroup";
    }
}
