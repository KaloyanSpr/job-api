package example.job_api.services.application;

import example.job_api.dto.ApplicationDto;
import example.job_api.entities.Application;

import java.util.Set;

public interface ApplicationService {
    ApplicationDto getApplicationById(Long id);
    Set<Application> getApplicationsByIds(Set<Long> ids);
}
