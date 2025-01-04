package example.job_api.mappers;

import example.job_api.dto.ApplicationDto;
import example.job_api.entities.Application;
import example.job_api.entities.Job;
import example.job_api.entities.User;
import example.job_api.services.application.ApplicationService;
import example.job_api.services.job.JobSevice;
import example.job_api.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ApplicationMapper {
    private final ApplicationService applicationService;
    private final UserService userService;
    private final JobSevice jobService;
    public  ApplicationDto toDto(Application application) {
        if (application == null) {
            return null;
        }

        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setId(application.getId());
        applicationDto.setStatus(application.getStatus());
        applicationDto.setUserId(application.getUser() != null ? application.getUser().getId() : null);
        applicationDto.setJobId(application.getJob() != null ? application.getJob().getId() : null);

        return applicationDto;
    }

    public Application toEntity(ApplicationDto applicationDto) {
        if (applicationDto == null) {
            return null;
        }
        User user = userService.getUserByModelId(applicationDto.getUserId());
        Job job = jobService.getJobByModelId(applicationDto.getJobId());
        Application application = new Application();
        application.setId(applicationDto.getId());
        application.setStatus(applicationDto.getStatus());
        application.setUser(user);
        application.setJob(job);

        return application;
    }
}
