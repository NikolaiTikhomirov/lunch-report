package ru.gb.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.model.Group;
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
        return "home";
    }

    @GetMapping("/addreport")
    public String addReportPage (Model model) {
        return "addreport";
    }

    @GetMapping("/reports")
    public String reportsPage (Model model) {
        return "reports";
    }

    @GetMapping("/adduser")
    public String addUserPage (Model model) {
        List<Group> groups = Objects.requireNonNull(webClient.get()
                        .uri("http://user-service/group")
                        .retrieve()
                        .toEntityList(Group.class)
                        .block())
                .getBody();
        model.addAttribute("groups", groups);
        model.addAttribute("user", new User());
//        model.addAttribute("om", new ObjectMapper());
        return "adduser";
    }

    @PostMapping("/adduser")
    public String addUser (@ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error);
            }
            model.addAttribute("adduserfail", result.getAllErrors());
            return "adduserfail";
        }
        try {
            User finalUser = webClient.post()
                    .uri("http://user-service/user")
                    .bodyValue(user)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            model.addAttribute("user", finalUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return "addusersuccess";
    }

    @GetMapping("/addgroup")
    public String addGroupPage (Model model) {
        return "addgroup";
    }
}
