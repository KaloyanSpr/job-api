package example.job_api.controllerTest;
import example.job_api.controllers.CompanyController;
import example.job_api.dto.CompanyDto;
import example.job_api.dto.JobDto;
import example.job_api.services.company.CompanyService;
import example.job_api.services.job.JobService;
import example.job_api.services.company.CompanyService;
import example.job_api.services.job.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CompanyControllerTest {

    @Mock
    private CompanyService companyService;

    @Mock
    private JobService jobService;

    @InjectMocks
    private CompanyController companyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCompany_shouldReturnCreatedCompany() {
        CompanyDto companyDto = new CompanyDto(1L, "TechCorp", "A tech company", "NYC", Set.of());
        when(companyService.createCompany(companyDto)).thenReturn(companyDto);

        ResponseEntity<CompanyDto> response = companyController.createCompany(companyDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(companyDto, response.getBody());
        verify(companyService, times(1)).createCompany(companyDto);
    }
    @Test
    void getAllCompanies_shouldReturnListOfCompanies() {
        CompanyDto company1 = new CompanyDto(1L, "TechCorp", "A tech company", "NYC", Set.of());
        CompanyDto company2 = new CompanyDto(2L, "BizCorp", "A business company", "LA", Set.of());
        List<CompanyDto> companies = Arrays.asList(company1, company2);

        when(companyService.getAllCompanies()).thenReturn(companies);

        ResponseEntity<?> response = companyController.getAllCompanies();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(companies, response.getBody());
        verify(companyService, times(1)).getAllCompanies();
    }

    @Test
    void getAllCompanies_shouldReturnNotFoundWhenNoCompanies() {
        when(companyService.getAllCompanies()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = companyController.getAllCompanies();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No companies found", response.getBody());
        verify(companyService, times(1)).getAllCompanies();
    }

    @Test
    void getCompanyById_shouldReturnCompany() {
        CompanyDto companyDto = new CompanyDto(1L, "TechCorp", "A tech company", "NYC", Set.of());
        when(companyService.getCompanyById(1L)).thenReturn(companyDto);

        ResponseEntity<?> response = companyController.getCompanyById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(companyDto, response.getBody());
        verify(companyService, times(1)).getCompanyById(1L);
    }

    @Test
    void getCompanyById_shouldReturnNotFoundWhenCompanyDoesNotExist() {
        when(companyService.getCompanyById(1L)).thenThrow(new RuntimeException("Company not found"));

        ResponseEntity<?> response = companyController.getCompanyById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Company with ID 1 not found.", response.getBody());
        verify(companyService, times(1)).getCompanyById(1L);
    }

    @Test
    void updateCompany_shouldReturnUpdatedCompany() {
        CompanyDto companyDto = new CompanyDto(1L, "TechCorp", "Updated description", "NYC", Set.of());
        when(companyService.updateCompany(1L, companyDto)).thenReturn(companyDto);

        ResponseEntity<CompanyDto> response = companyController.updateCompany(1L, companyDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(companyDto, response.getBody());
        verify(companyService, times(1)).updateCompany(1L, companyDto);
    }

    @Test
    void deleteCompany_shouldReturnNoContent() {
        doNothing().when(companyService).deleteCompany(1L);

        ResponseEntity<Void> response = companyController.deleteCompany(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(companyService, times(1)).deleteCompany(1L);
    }


}
