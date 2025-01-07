package example.job_api.services.application.impl;

import example.job_api.dto.ApplicationDto;
import example.job_api.entities.Application;
import example.job_api.entities.Job;
import example.job_api.entities.User;
import example.job_api.mappers.ApplicationMapper;
import example.job_api.repositories.ApplicationRepository;
import example.job_api.services.application.ApplicationService;
import example.job_api.services.job.JobService;
import example.job_api.services.user.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final UserService userService;
    private final JobService jobService;

    @Override
    public ApplicationDto getApplicationById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Application ID cannot be null");
        }
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if (optionalApplication.isEmpty()) {
            return null;
        }

        Application application = optionalApplication.get();
        return applicationMapper.toDto(application);
    }



    @Override
    public Set<Application> getApplicationsByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("The list of application IDs cannot be null or empty.");
        }
        try {
            return new HashSet<>(applicationRepository.findAllById(ids));
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch applications for the provided IDs.");
        }
    }
    @Override
    public Set<Application> getApplicationsByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        try {
            Set<Application> applications = applicationRepository.findByUser_Id(userId);
            return applications;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch applications for user ID: " + userId);
        }
    }


    @Override
    public ApplicationDto createApplication(ApplicationDto applicationDto) {
        if (applicationDto == null) {
            throw new IllegalArgumentException("Application DTO cannot be null");
        }
        try {

            Application application = applicationMapper.toEntity(applicationDto);

            application.setUser(userService.getUserByModelId(applicationDto.getUserId()));
            application.setJob(jobService.getJobByModelId(applicationDto.getJobId()));

            application = applicationRepository.save(application);
            ApplicationDto savedApplicationDto = applicationMapper.toDto(application);
            return savedApplicationDto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create application");
        }
    }

    @Override
    public ApplicationDto updateApplication(Long id, ApplicationDto applicationDto) {
        if (id == null) {
            throw new IllegalArgumentException("Application ID cannot be null");
        }

        if (applicationDto == null) {
            throw new IllegalArgumentException("Application DTO cannot be null");
        }

        try {
            Application existingApplication = applicationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found for id: " + id));

            existingApplication.setStatus(applicationDto.getStatus());
            Application updatedApplication = applicationRepository.save(existingApplication);
            return applicationMapper.toDto(updatedApplication);

        } catch (Exception e) {
            throw new RuntimeException("Failed to update application");
        }
    }


    @Override
    public void deleteApplication(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Application ID cannot be null");
        }

        try {
            Application application = applicationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Application not found for id: " + id));
            applicationRepository.delete(application);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete application");
        }
    }

    @Override
    public List<ApplicationDto> getApplicationsByJobId(Long jobId) {
        List<Application> applications = applicationRepository.findByJob_Id(jobId);
        return applications.stream()
                .map(application -> {
                    ApplicationDto dto = applicationMapper.toDto(application);
                    if (dto.getJobId() == null && application.getJob() != null) {
                        dto.setJobId(application.getJob().getId());
                    }
                    if (dto.getUserId() == null && application.getUser() != null) {
                        dto.setUserId(application.getUser().getId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationDto createApplicationForUser(Long userId, ApplicationDto applicationDto) {
        User user = userService.getUserByModelId(userId);
        Job job = jobService.getJobByModelId(applicationDto.getJobId());

        Application application = applicationMapper.toEntity(applicationDto);
        application.setUser(user);
        application.setJob(job);
        Application savedApplication = applicationRepository.save(application);
        return applicationMapper.toDto(savedApplication);
    }

}
