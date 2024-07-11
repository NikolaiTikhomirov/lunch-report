package ru.gb.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.model.Group;
import ru.gb.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

@Service
@RestController
@RequestMapping("/group")
@Tag(name = "School")
public class GroupController {
    private final GroupRepository groupRepository;

    public GroupController(ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction, GroupRepository groupRepository, WebClient.Builder webClientBuilder) {
        this.groupRepository = groupRepository;
        WebClient webClient = webClientBuilder
                .filter(loadBalancerExchangeFilterFunction)
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "get group by id", description = "Получить группу по ID")
    public Group getGroupById (@PathVariable Long id) {
        return groupRepository.findById(id).get();
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "get group by name", description = "Получить группу по его имени")
    public Optional<Group> getGroupByName (@PathVariable String name) {
        return groupRepository.findByName(name);
    }

    @GetMapping()
    @Operation(summary = "get all group", description = "Получить список всех групп")
    public List<Group> getAllGroups () {
        return groupRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "add group", description = "Добавить группу")
    public Group addGroup (@RequestBody Group group) {
        return groupRepository.save(group);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "delete user by id", description = "Удалить группу по ID")
    public void deleteGroupById (@PathVariable Long id) {
        groupRepository.deleteById(id);
    }
}
