package example.job_api.services.application.impl;

import example.job_api.dto.ApplicationDto;
import example.job_api.entities.Application;
import example.job_api.repositories.ApplicationRepository;
import example.job_api.services.application.ApplicationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@AllArgsConstructor
@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;

    @Override
    public ApplicationDto getApplicationById(Long id) {
        return null;
    }

    @Override
    public Set<Application> getApplicationsByIds(Set<Long> ids) {
        return new HashSet<>(applicationRepository.findAllById(ids));
    }
}
