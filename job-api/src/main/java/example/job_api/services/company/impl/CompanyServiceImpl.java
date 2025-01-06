package example.job_api.services.company.impl;

import example.job_api.dto.ApplicationDto;
import example.job_api.dto.CompanyDto;
import example.job_api.dto.JobDto;
import example.job_api.entities.Company;
import example.job_api.entities.Job;
import example.job_api.mappers.ApplicationMapper;
import example.job_api.mappers.CompanyMapper;
import example.job_api.mappers.JobMapper;
import example.job_api.repositories.CompanyRepository;
import example.job_api.services.company.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.Override;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@AllArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final JobMapper jobMapper;
    private final ApplicationMapper applicationMapper;


    @Override
    public CompanyDto createCompany(CompanyDto companyDto) {
        Company company = companyMapper.toEntity(companyDto);
        Company savedCompany = companyRepository.save(company);
        return companyMapper.toDto(savedCompany);
    }


    public List<CompanyDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(companyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));
        return companyMapper.toDto(company);
    }

    @Override
    public Company getCompanyModelById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Company ID cannot be null");
        }
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + id));
    }

    @Override
    public List<Company> getCompaniesByIds(List<Long> companyIds) {
        return List.of();
    }

    @Override
    public CompanyDto updateCompany(Long id, CompanyDto companyDto) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));

        existingCompany.setName(companyDto.getTitle());
        existingCompany.setLocation(companyDto.getLocation());

        if (companyDto.getJobIds() != null) {
            Set<Job> jobs = companyMapper.toEntity(companyDto).getJobs();
            existingCompany.setJobs(jobs);
        }


        Company updatedCompany = companyRepository.save(existingCompany);
        return companyMapper.toDto(updatedCompany);
    }

    @Override
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new EntityNotFoundException("Company not found with id: " + id);
        }
        companyRepository.deleteById(id);
    }

    @Override
    public List<JobDto> getAllJobsForCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));

        return company.getJobs().stream()
                .map(job -> {
                    JobDto jobDto = jobMapper.toDto(job);
                    jobDto.setCompanyId(companyId); // Add companyId if not present in the JobDto
                    jobDto.setApplicationIds(job.getApplications().stream()
                            .map(application -> application.getId())
                            .collect(Collectors.toSet())); // Collect application ids

                    return jobDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public JobDto getJobByIdForCompany(Long companyId, Long jobId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        Job job = company.getJobs().stream()
                .filter(j -> j.getId().equals(jobId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        return jobMapper.toDto(job);
    }

    @Override
    public List<ApplicationDto> getAllApplicationsForJob(Long companyId, Long jobId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        Job job = company.getJobs().stream()
                .filter(j -> j.getId().equals(jobId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        return job.getApplications().stream()
                .map(application -> {
                    ApplicationDto applicationDto = applicationMapper.toDto(application);
                    applicationDto.setUserId(application.getUser().getId());
                    applicationDto.setJobId(job.getId());
                    return applicationDto;
                })
                .collect(Collectors.toList());
    }
}
