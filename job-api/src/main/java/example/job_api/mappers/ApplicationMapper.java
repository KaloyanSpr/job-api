package example.job_api.mappers;

import example.job_api.dto.ApplicationDto;
import example.job_api.entities.Application;
import example.job_api.entities.Job;
import example.job_api.entities.User;
import example.job_api.services.application.ApplicationService;
import example.job_api.services.job.JobService;
import example.job_api.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ApplicationMapper {

    public ApplicationDto toDto(Application application) {
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

        Application application = new Application();
        application.setId(applicationDto.getId());
        application.setStatus(applicationDto.getStatus());

        return application;
    }
}
