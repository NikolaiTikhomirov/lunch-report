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
import ru.gb.model.Report;
import ru.gb.model.SimpleUser;
import ru.gb.model.User;

import java.util.ArrayList;
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

    @GetMapping("/choosegroup")
    public String chooseGroup (Model model) {
        model.addAttribute("choosegroups", getGroups());
        model.addAttribute("group", new Group());
        return "choosegroup";
    }

    @GetMapping("/addreport")
    public String addReportPage (@ModelAttribute Group group, Model model) {
        System.out.println(group);
        List<User> users = Objects.requireNonNull(webClient.get()
                        .uri("http://user-service/group/users/" + group.getName())
                        .retrieve()
                        .toEntityList(User.class)
                        .block())
                .getBody();
        System.out.println(users);
        model.addAttribute("users", users);
        model.addAttribute("report", new Report());
        return "addreport";
    }

    @PostMapping("/addreport")
    public String addReport (@ModelAttribute Report report, BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error);
            }
            model.addAttribute("adduserfail", result.getAllErrors());
            return "adduserfail";
        }
        System.out.println(report);
        List<User> usersListToReport = new ArrayList<>();
        for (String name : report.getUsersNames()) {
            User user = webClient.get()
                    .uri("http://user-service/user/name/" + name)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            usersListToReport.add(user);
        }
        report.setUsers(usersListToReport);
        webClient.post()
                .uri("http://reporter/report")
                .bodyValue(report)
                .retrieve()
                .bodyToMono(Report.class)
                .block();
        model.addAttribute("report", report);
        return "addreportsuccess";
    }

    @GetMapping("/reports")
    public String reportsPage (Model model) {
        List<Report> reports = Objects.requireNonNull(webClient.get()
                        .uri("http://reporter/report")
                        .retrieve()
                        .toEntityList(Report.class)
                        .block())
                .getBody();
        model.addAttribute("reports", reports);
        return "reports";
    }

    @GetMapping("/adduser")
    public String addUserPage (Model model) {
        model.addAttribute("groups", getGroups());
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

    private List<Group> getGroups() {
        return Objects.requireNonNull(webClient.get()
                        .uri("http://user-service/group")
                        .retrieve()
                        .toEntityList(Group.class)
                        .block())
                .getBody();
    }
}
