package cz.ales17.mikina.data.repository;

import cz.ales17.mikina.data.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {

}
