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

        groupRepository.save(new Group("fake", "fake"));
        Group adminGroup = groupRepository.save(new Group("Admin", "All years"));
        Group studentGroup = groupRepository.save(new Group("7A", "2024/2025"));
        userRepository.save(new User("student", "student", studentGroup));
        userRepository.save(new User("admin", "admin", adminGroup));
        userRepository.save(new User("teacher", "admin", adminGroup));
        userRepository.save(new User("second student", "student", studentGroup));
    }
}
