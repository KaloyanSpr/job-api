package example.job_api.serviceTests;

import example.job_api.dto.ApplicationDto;
import example.job_api.dto.CompanyDto;
import example.job_api.dto.JobDto;
import example.job_api.entities.Application;
import example.job_api.entities.Company;
import example.job_api.entities.Job;
import example.job_api.entities.User;
import example.job_api.mappers.ApplicationMapper;
import example.job_api.mappers.CompanyMapper;
import example.job_api.mappers.JobMapper;
import example.job_api.repositories.CompanyRepository;
import example.job_api.services.company.impl.CompanyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTests {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private JobMapper jobMapper;

    @Mock
    private ApplicationMapper applicationMapper;

    @InjectMocks
    private CompanyServiceImpl companyService;


    @Test
    void testCreateCompany() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTitle("Tech Corp");
        companyDto.setLocation("New York");

        Company company = new Company();
        company.setId(1L);
        company.setName("Tech Corp");
        company.setLocation("New York");

        Company savedCompany = new Company();
        savedCompany.setId(1L);
        savedCompany.setName("Tech Corp");
        savedCompany.setLocation("New York");

        when(companyMapper.toEntity(companyDto)).thenReturn(company);
        when(companyRepository.save(company)).thenReturn(savedCompany);
        when(companyMapper.toDto(savedCompany)).thenReturn(companyDto);

        CompanyDto result = companyService.createCompany(companyDto);

        assertNotNull(result);
        assertEquals("Tech Corp", result.getTitle());
        assertEquals("New York", result.getLocation());
        verify(companyRepository).save(company);
    }

    @Test
    void testGetAllCompanies() {
        Company company1 = new Company();
        company1.setId(1L);
        company1.setName("Tech Corp");
        company1.setLocation("New York");

        Company company2 = new Company();
        company2.setId(2L);
        company2.setName("Innovate Inc");
        company2.setLocation("San Francisco");

        List<Company> companies = List.of(company1, company2);

        CompanyDto companyDto1 = new CompanyDto();
        companyDto1.setTitle("Tech Corp");
        companyDto1.setLocation("New York");

        CompanyDto companyDto2 = new CompanyDto();
        companyDto2.setTitle("Innovate Inc");
        companyDto2.setLocation("San Francisco");

        when(companyRepository.findAll()).thenReturn(companies);
        when(companyMapper.toDto(company1)).thenReturn(companyDto1);
        when(companyMapper.toDto(company2)).thenReturn(companyDto2);

        List<CompanyDto> result = companyService.getAllCompanies();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Tech Corp", result.get(0).getTitle());
        assertEquals("Innovate Inc", result.get(1).getTitle());
        verify(companyRepository).findAll();
    }

    @Test
    void testGetCompanyById() {
        Long companyId = 1L;
        Company company = new Company(companyId, "Tech Corp","Tech Company", "New York", new HashSet<>());
        CompanyDto companyDto = new CompanyDto(companyId, "Tech Corp","Tech Company","New York", null);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(companyMapper.toDto(company)).thenReturn(companyDto);

        CompanyDto result = companyService.getCompanyById(companyId);

        assertNotNull(result);
        assertEquals(companyId, result.getId());
        assertEquals("Tech Corp", result.getTitle());
        verify(companyRepository).findById(companyId);
        verify(companyMapper).toDto(company);
    }


    @Test
    void testGetCompanyModelById() {
        Long companyId = 1L;
        Company company = new Company(companyId, "Tech Corp","Tech Company", "New York", new HashSet<>());

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

        Company result = companyService.getCompanyModelById(companyId);

        assertNotNull(result);
        assertEquals(companyId, result.getId());
        assertEquals("Tech Corp", result.getName());
        verify(companyRepository).findById(companyId);
    }

    @Test
    void testUpdateCompany() {
        Long companyId = 1L;
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTitle("Tech Corp SF");
        companyDto.setDescription("Tech Company in New Location");
        companyDto.setLocation("San Francisco");
        companyDto.setJobIds(Set.of(1L, 2L));

        Company existingCompany = new Company(companyId, "Tech Corp", "Tech Company", "New York", new HashSet<>());
        Company updatedCompany = new Company(companyId, "Tech Corp SF","Tech Company in New Location", "San Francisco", new HashSet<>());

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
        when(companyMapper.toEntity(companyDto)).thenReturn(updatedCompany);
        when(companyRepository.save(existingCompany)).thenReturn(updatedCompany);
        when(companyMapper.toDto(updatedCompany)).thenReturn(companyDto);

        CompanyDto result = companyService.updateCompany(companyId, companyDto);

        assertNotNull(result);
        assertEquals("Tech Corp SF", result.getTitle());
        assertEquals("San Francisco", result.getLocation());
        verify(companyRepository).findById(companyId);
        verify(companyRepository).save(existingCompany);
    }

    @Test
    void testDeleteCompany() {
        Long companyId = 1L;

        when(companyRepository.existsById(companyId)).thenReturn(true);

        assertDoesNotThrow(() -> companyService.deleteCompany(companyId));

        verify(companyRepository).existsById(companyId);
        verify(companyRepository).deleteById(companyId);
    }

    @Test
    void testGetAllJobsForCompany() {
        Long companyId = 1L;

        Job job1 = new Job();
        job1.setId(1L);
        Job job2 = new Job();
        job2.setId(2L);

        Application application1 = new Application();
        application1.setId(101L);
        Application application2 = new Application();
        application2.setId(102L);

        job1.setApplications(Set.of(application1));
        job2.setApplications(Set.of(application2));

        Company company = new Company();
        company.setId(companyId);
        company.setJobs(Set.of(job1, job2));

        JobDto jobDto1 = new JobDto();
        jobDto1.setId(1L);
        jobDto1.setApplicationIds(Set.of(101L));

        JobDto jobDto2 = new JobDto();
        jobDto2.setId(2L);
        jobDto2.setApplicationIds(Set.of(102L));

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(jobMapper.toDto(job1)).thenReturn(jobDto1);
        when(jobMapper.toDto(job2)).thenReturn(jobDto2);

        List<JobDto> result = companyService.getAllJobsForCompany(companyId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId());
        assertEquals(102L, result.get(0).getApplicationIds().iterator().next());
        assertEquals(1L, result.get(1).getId());
        assertEquals(101L, result.get(1).getApplicationIds().iterator().next());
        verify(companyRepository).findById(companyId);
        verify(jobMapper).toDto(job1);
        verify(jobMapper).toDto(job2);
    }

    @Test
    void testGetJobByIdForCompany() {
        Long companyId = 1L;
        Long jobId = 101L;

        Job job = new Job();
        job.setId(jobId);

        Company company = new Company();
        company.setId(companyId);
        company.setJobs(Set.of(job));

        JobDto jobDto = new JobDto();
        jobDto.setId(jobId);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(jobMapper.toDto(job)).thenReturn(jobDto);

        JobDto result = companyService.getJobByIdForCompany(companyId, jobId);

        assertNotNull(result);
        assertEquals(jobId, result.getId());
        verify(companyRepository).findById(companyId);
        verify(jobMapper).toDto(job);
    }

    @Test
    void testGetAllApplicationsForJob() {
        Long companyId = 1L;
        Long jobId = 101L;

        Application application1 = new Application();
        application1.setId(201L);
        User user1 = new User();
        user1.setId(301L);
        application1.setUser(user1);

        Application application2 = new Application();
        application2.setId(202L);
        User user2 = new User();
        user2.setId(302L);
        application2.setUser(user2);

        Job job = new Job();
        job.setId(jobId);
        job.setApplications(Set.of(application1, application2));

        Company company = new Company();
        company.setId(companyId);
        company.setJobs(Set.of(job));

        ApplicationDto applicationDto1 = new ApplicationDto();
        applicationDto1.setId(201L);
        applicationDto1.setUserId(301L);
        applicationDto1.setJobId(jobId);

        ApplicationDto applicationDto2 = new ApplicationDto();
        applicationDto2.setId(202L);
        applicationDto2.setUserId(302L);
        applicationDto2.setJobId(jobId);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(applicationMapper.toDto(application1)).thenReturn(applicationDto1);
        when(applicationMapper.toDto(application2)).thenReturn(applicationDto2);


        List<ApplicationDto> result = companyService.getAllApplicationsForJob(companyId, jobId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(202L, result.get(0).getId());
        assertEquals(302L, result.get(0).getUserId());
        assertEquals(201L, result.get(1).getId());
        assertEquals(301L, result.get(1).getUserId());
        verify(companyRepository).findById(companyId);
        verify(applicationMapper).toDto(application1);
        verify(applicationMapper).toDto(application2);
    }



}
