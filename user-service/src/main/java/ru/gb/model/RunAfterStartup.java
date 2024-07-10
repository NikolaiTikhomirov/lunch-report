package ru.gb.model;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.gb.repository.GroupRepository;
import ru.gb.repository.UserRepository;

@Component
@AllArgsConstructor
public class RunAfterStartup {
    private UserRepository userRepository;
    private GroupRepository groupRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {

        Group group = groupRepository.save(new Group("7A", "2024/2025"));
        Group adminGroup = groupRepository.save(new Group("Admin", "All years"));
        userRepository.save(new User("student", "student", group));
        userRepository.save(new User("admin", "admin", adminGroup));
        userRepository.save(new User("teacher", "admin", adminGroup));
    }
}
