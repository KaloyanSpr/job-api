package example.job_api.services.job.impl;

import example.job_api.dto.JobDto;
import example.job_api.entities.Job;
import example.job_api.repositories.JobRepository;
import example.job_api.services.job.JobSevice;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
@AllArgsConstructor
@Service
public class JobServiceImpl implements JobSevice {
    private final JobRepository jobRepository;

    @Override
    public JobDto getJobById(Long id) {
        return null;
    }

    @Override
    public Set<Job> getJobsByIds(Set<Long> ids) {
        return ids != null
                ? new HashSet<>(jobRepository.findAllById(ids))
                : Collections.emptySet();
    }

    @Override
    public Job getJobByModelId(Long id) {
        return jobRepository.findById(id).orElse(null);
    }
}
