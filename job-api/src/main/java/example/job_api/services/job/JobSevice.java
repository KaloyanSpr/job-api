package example.job_api.services.job;

import example.job_api.dto.JobDto;
import example.job_api.entities.Job;

import java.util.List;
import java.util.Set;

public interface JobSevice {
    JobDto getJobById(Long id);
    Set<Job> getJobsByIds(Set<Long> ids);
    Job getJobByModelId(Long id);
}
