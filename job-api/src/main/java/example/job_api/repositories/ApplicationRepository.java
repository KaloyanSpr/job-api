package example.job_api.repositories;

import example.job_api.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Set<Application> findByUser_Id(Long userId);  // Searching by User ID
    List<Application> findByJob_Id(Long jobId);
}
