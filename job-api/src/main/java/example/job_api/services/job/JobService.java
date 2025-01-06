package example.job_api.services.job;

import example.job_api.dto.ApplicationDto;
import example.job_api.dto.JobDto;
import example.job_api.entities.Job;

import java.util.List;
import java.util.Set;

public interface JobService {
    JobDto getJobById(Long id);
    Set<Job> getJobsByIds(Set<Long> ids);
    Job getJobByModelId(Long id);
    List<ApplicationDto> getApplicationsForJob(Long jobId);
    JobDto createJob(JobDto jobDto);
    Set<JobDto> getAllJobs();
    JobDto updateJob(Long id, JobDto jobDto);
    void deleteJob(Long id);
    List<JobDto> getJobsByCompanyId(Long companyId);
}
