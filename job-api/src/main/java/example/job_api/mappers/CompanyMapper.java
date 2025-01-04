package example.job_api.mappers;

import example.job_api.dto.CompanyDto;
import example.job_api.entities.Application;
import example.job_api.entities.Company;
import example.job_api.entities.Job;
import example.job_api.services.job.JobSevice;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
@Component
@AllArgsConstructor
public class CompanyMapper {
    private final JobSevice jobService;
    public CompanyDto toDto(Company company) {
        if (company == null) {
            return null;
        }

        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(company.getId());
        companyDto.setTitle(company.getName());
        companyDto.setLocation(company.getLocation());
        companyDto.setJobIds(
                company.getJobs() != null
                        ? company.getJobs().stream()
                        .map(Job::getId) // Extract job IDs
                        .collect(Collectors.toSet())
                        : null
        );

        return companyDto;
    }
    public Company toEntity(CompanyDto companyDto) {
        if (companyDto == null) {
            return null;
        }

        Set<Job> jobs = companyDto.getJobIds() != null
                ? new HashSet<>(jobService.getJobsByIds(companyDto.getJobIds()))
                : Collections.emptySet();

        Company company = new Company();
        company.setId(companyDto.getId());
        company.setName(companyDto.getTitle());
        company.setLocation(companyDto.getLocation());
        company.setJobs(jobs);

        return company;
    }
}
