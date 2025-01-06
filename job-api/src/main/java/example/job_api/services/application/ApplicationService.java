package example.job_api.services.application;

import example.job_api.dto.ApplicationDto;
import example.job_api.entities.Application;

import java.util.List;
import java.util.Set;

public interface ApplicationService {
    ApplicationDto getApplicationById(Long id);
    Set<Application> getApplicationsByIds(Set<Long> ids);
    Set<Application> getApplicationsByUserId(Long userId);
    ApplicationDto createApplication(ApplicationDto applicationDto);
    ApplicationDto updateApplication(Long id, ApplicationDto applicationDto);
    void deleteApplication(Long id);
    List<ApplicationDto> getApplicationsByJobId(Long jobId);
    ApplicationDto createApplicationForUser(Long userId, ApplicationDto applicationDto);
}
