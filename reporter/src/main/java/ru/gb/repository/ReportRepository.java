package ru.gb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.model.Group;
import ru.gb.model.Report;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByGroup(Group group);
}
