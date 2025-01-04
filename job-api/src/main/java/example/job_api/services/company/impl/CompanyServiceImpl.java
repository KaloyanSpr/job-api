package example.job_api.services.company.impl;

import example.job_api.dto.CompanyDto;
import example.job_api.entities.Company;
import example.job_api.repositories.CompanyRepository;
import example.job_api.services.company.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    @Override
    public CompanyDto createCompany(CompanyDto companyDto) {
        return null;
    }

    @Override
    public CompanyDto getCompanyById(Long companyId) {
        return null;
    }

    @Override
    public Company getCompanyModelById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public List<Company> getCompaniesByIds(List<Long> companyIds) {
        return List.of();
    }
}
