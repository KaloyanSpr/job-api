package example.job_api.services.job.impl;

import example.job_api.dto.ApplicationDto;
import example.job_api.dto.JobDto;
import example.job_api.entities.Application;
import example.job_api.entities.Job;
import example.job_api.mappers.ApplicationMapper;
import example.job_api.mappers.JobMapper;
import example.job_api.repositories.JobRepository;
import example.job_api.services.job.JobService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final ApplicationMapper applicationMapper;

    @Override
    public JobDto getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return jobMapper.toDto(job);
    }

    @Override
    public List<ApplicationDto> getApplicationsForJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        List<Application> applications = job.getApplications().stream()
                .collect(Collectors.toList());

        return applications.stream()
                .map(applicationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Job> getJobsByIds(Set<Long> ids) {
        return jobRepository.findAllById(ids).stream()
                .collect(Collectors.toSet());
    }

    @Override
    public Job getJobByModelId(Long id) {
        return jobRepository.findByModelId(id)
                .orElseThrow(() -> new RuntimeException("Job with the given modelId not found"));
    }

    public JobDto createJob(JobDto jobDto) {
        Job job = jobMapper.toEntity(jobDto);
        job = jobRepository.save(job);
        return jobMapper.toDto(job);
    }

    public JobDto updateJob(Long id, JobDto jobDto) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setLocation(jobDto.getLocation());
        // Update company and applications as needed

        job = jobRepository.save(job);
        return jobMapper.toDto(job);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }



    public Set<JobDto> getAllJobs() {
        Set<Job> jobs = jobRepository.findAll().stream()
                .collect(Collectors.toSet());
        return jobs.stream()
                .map(jobMapper::toDto)
                .collect(Collectors.toSet());
    }
}