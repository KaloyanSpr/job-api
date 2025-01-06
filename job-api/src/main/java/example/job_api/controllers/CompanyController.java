package example.job_api.controllers;

import example.job_api.dto.ApplicationDto;
import example.job_api.dto.CompanyDto;
import example.job_api.dto.JobDto;
import example.job_api.services.company.CompanyService;
import example.job_api.services.job.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;
    private final JobService jobService;

    @Autowired
    public CompanyController(CompanyService companyService, JobService jobService) {
        this.companyService = companyService;
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto companyDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(companyDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAllCompanies() {
        List<CompanyDto> companies = companyService.getAllCompanies();
        if (companies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No companies found");
        }
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        try {
            CompanyDto company = companyService.getCompanyById(id);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Company with ID " + id + " not found.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable Long id, @RequestBody CompanyDto companyDTO) {
        return ResponseEntity.ok(companyService.updateCompany(id, companyDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/job")
    public ResponseEntity<List<JobDto>> getJobsByCompanyId(@PathVariable Long companyId) {
        List<JobDto> jobs = jobService.getJobsByCompanyId(companyId);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{companyId}/jobs")
    public ResponseEntity<List<JobDto>> getAllJobsForCompany(@PathVariable Long companyId) {
        try {
            List<JobDto> jobDtos = companyService.getAllJobsForCompany(companyId);
            if (jobDtos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.emptyList());
            }
            return ResponseEntity.ok(jobDtos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/{companyId}/jobs/{jobId}")
    public ResponseEntity<JobDto> getJobByIdForCompany(@PathVariable Long companyId, @PathVariable Long jobId) {
        try {
            JobDto jobDto = companyService.getJobByIdForCompany(companyId, jobId);
            return ResponseEntity.ok(jobDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping("/{companyId}/jobs/{jobId}/applications")
    public ResponseEntity<List<ApplicationDto>> getApplicationsForJob(
            @PathVariable Long companyId,
            @PathVariable Long jobId) {
        try {
            List<ApplicationDto> applications = companyService.getAllApplicationsForJob(companyId, jobId);
            return ResponseEntity.ok(applications);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }
}
