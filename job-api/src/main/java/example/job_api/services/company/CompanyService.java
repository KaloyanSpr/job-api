package example.job_api.services.company;

import example.job_api.dto.CompanyDto;
import example.job_api.dto.UserDto;
import example.job_api.entities.Company;
import example.job_api.entities.User;

import java.util.List;

public interface CompanyService {
    CompanyDto createCompany(CompanyDto companyDto);
    CompanyDto getCompanyById(Long companyId);
    CompanyDto updateCompany(Long id,CompanyDto companyDto);
    Company getCompanyModelById(Long id);
    List<Company> getCompaniesByIds(List<Long> companyIds);
    void deleteCompany(Long id);
    List<CompanyDto> getAllCompanies();
}
