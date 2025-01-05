package example.job_api.repositories;

import example.job_api.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Set<Application> findByUserId(Long userId);
    List<Application> findByJobId(Long jobId);
}
