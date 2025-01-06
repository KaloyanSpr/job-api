package example.job_api.mappers;
import example.job_api.dto.CompanyDto;
import example.job_api.dto.JobDto;
import example.job_api.entities.Application;
import example.job_api.entities.Company;
import example.job_api.entities.Job;
import example.job_api.services.application.ApplicationService;
import example.job_api.services.company.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
@Component
@AllArgsConstructor
public class JobMapper {

    public JobDto toDto(Job job) {
        if (job == null) {
            return null;
        }

        JobDto jobDto = new JobDto();
        jobDto.setId(job.getId());
        jobDto.setTitle(job.getTitle());
        jobDto.setDescription(job.getDescription());
        jobDto.setLocation(job.getLocation());
        jobDto.setCompanyId(job.getCompany() != null ? job.getCompany().getId() : null);
        jobDto.setApplicationIds(
                job.getApplications() != null
                        ? job.getApplications().stream()
                        .map(Application::getId)
                        .collect(Collectors.toSet())
                        : null
        );

        return jobDto;
    }

    public Job toEntity(JobDto jobDto) {
        if (jobDto == null) {
            return null;
        }
        Job job = new Job();
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setLocation(jobDto.getLocation());
        return job;
    }
}
