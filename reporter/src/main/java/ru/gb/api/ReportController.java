package ru.gb.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.model.Group;
import ru.gb.model.Report;
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
    public Report addReport (@RequestBody Report report) {
        return reportRepository.save(report);
    }

    @GetMapping("/{id}")
    @Operation(summary = "get user by id", description = "Получить пользователя по его ID")
    public Report getUserById (@PathVariable Long id) {
        return reportRepository.findById(id).get();
    }

    @GetMapping("/group/{groupName}")
    @Operation(summary = "get report by group", description = "Получить отчет по группе")
    public Optional<Report> getUserByGroup (@PathVariable String groupName) {
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
    public List<Report> getAllUsers () {
        return reportRepository.findAll();
    }

}
