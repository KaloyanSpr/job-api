package example.job_api.services.application;

import example.job_api.dto.ApplicationDto;
import example.job_api.entities.Application;

import java.util.Set;

public interface ApplicationService {
    ApplicationDto getApplicationById(Long id);
    Set<Application> getApplicationsByIds(Set<Long> ids);
    Set<Application> getApplicationsByUserId(Long userId);
    ApplicationDto createApplication(ApplicationDto applicationDto);
    void updateApplication(Long id, ApplicationDto applicationDto);
    void deleteApplication(Long id);
}
