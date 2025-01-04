package example.job_api.services.company.impl;

import example.job_api.dto.CompanyDto;
import example.job_api.entities.Company;
import example.job_api.entities.Job;
import example.job_api.mappers.CompanyMapper;
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

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

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
        return null;
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
}
