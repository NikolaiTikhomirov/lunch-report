package ru.gb.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.model.Group;
import ru.gb.model.Report;
import ru.gb.model.User;
import ru.gb.repository.ReportRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RestController
@RequestMapping("/report")
@Tag(name = "Report")
public class ReportController {
    private final ReportRepository reportRepository;
    private final WebClient webClient;

    public ReportController(ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction, ReportRepository reportRepository, WebClient.Builder webClientBuilder) {
        this.reportRepository = reportRepository;
        this.webClient = webClientBuilder
                .filter(loadBalancerExchangeFilterFunction)
                .build();
    }

    @PostMapping
    @Operation(summary = "create report", description = "Создать отчет")
    public Report addReport (@RequestBody List<User> users) {
        Report report = new Report(users);
        try {
            for (User user : users) {
                user.getReports().add(report);
            }
            System.out.println(report);
            System.out.println(report.getUsers().size());
            for (User user : report.getUsers()) {
                System.out.println(user);
            }
            reportRepository.save(report);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return report;
    }

    @GetMapping("/{id}")
    @Operation(summary = "get report by id", description = "Получить отчёт по ID")
    public Report getReportById (@PathVariable Long id) {
        return reportRepository.findById(id).get();
    }

    @GetMapping("/group/{groupName}")
    @Operation(summary = "get report by group", description = "Получить отчет по названию группы")
    public Optional<Report> getReportByGroup (@PathVariable String groupName) {
        Group group = Objects.requireNonNull(webClient.get()
                        .uri("http://user-service/group/name/{groupName}", groupName)
                        .retrieve()
                        .toEntity(Group.class)
                        .block())
                .getBody();
        return reportRepository.findByGroup(group);
    }

    @GetMapping()
    @Operation(summary = "get all reports", description = "Получить список всех отчетов")
    public List<Report> getAllReports () {
        return reportRepository.findAll();
    }

}
