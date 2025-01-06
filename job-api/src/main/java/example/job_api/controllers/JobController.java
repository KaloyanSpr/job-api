package example.job_api.controllers;

import example.job_api.dto.ApplicationDto;
import example.job_api.dto.JobDto;
import example.job_api.entities.Job;
import example.job_api.services.application.ApplicationService;
import example.job_api.services.job.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/job")
public class JobController {
    private final JobService jobService;
    private final ApplicationService applicationService;

    @Autowired
    public JobController(JobService jobService, ApplicationService applicationService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<JobDto> createJobPosting(@RequestBody JobDto jobDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(jobDto));
    }

    @GetMapping
    public ResponseEntity<Set<JobDto>> getAllJobPostings() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getJobPostingById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobDto> updateJobPosting(@PathVariable Long id, @RequestBody JobDto jobDto) {
        return ResponseEntity.ok(jobService.updateJob(id, jobDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPosting(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/applications")
    public ResponseEntity<List<ApplicationDto>> getApplicationsByJobId(@PathVariable Long jobId) {
        List<ApplicationDto> applicationDtos = applicationService.getApplicationsByJobId(jobId);
        return ResponseEntity.ok(applicationDtos);
    }

}
