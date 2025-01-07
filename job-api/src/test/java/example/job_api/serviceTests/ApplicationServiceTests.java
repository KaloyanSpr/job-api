package example.job_api.serviceTests;

import example.job_api.dto.ApplicationDto;
import example.job_api.entities.Application;
import example.job_api.entities.Job;
import example.job_api.entities.User;
import example.job_api.mappers.ApplicationMapper;
import example.job_api.repositories.ApplicationRepository;
import example.job_api.services.application.impl.ApplicationServiceImpl;
import example.job_api.services.job.JobService;
import example.job_api.services.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTests {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ApplicationMapper applicationMapper;

    @Mock
    private UserService userService;

    @Mock
    private JobService jobService;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Test
    void testGetApplicationById() {
        Long applicationId = 1L;

        Application application = new Application();
        application.setId(applicationId);
        application.setStatus("PENDING");

        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setId(applicationId);
        applicationDto.setStatus("PENDING");

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(applicationMapper.toDto(application)).thenReturn(applicationDto);

        ApplicationDto result = applicationService.getApplicationById(applicationId);

        assertNotNull(result);
        assertEquals(applicationId, result.getId());
        assertEquals("PENDING", result.getStatus());

        verify(applicationRepository).findById(applicationId);
        verify(applicationMapper).toDto(application);
    }

    @Test
    void testGetApplicationsByIds() {
        Set<Long> applicationIds = Set.of(1L, 2L);

        Application application1 = new Application();
        application1.setId(1L);

        Application application2 = new Application();
        application2.setId(2L);

        when(applicationRepository.findAllById(applicationIds)).thenReturn(List.of(application1, application2));

        Set<Application> result = applicationService.getApplicationsByIds(applicationIds);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(application1));
        assertTrue(result.contains(application2));

        verify(applicationRepository).findAllById(applicationIds);
    }

    @Test
    void testGetApplicationsByUserId() {
        Long userId = 1L;
        Set<Application> applications = new HashSet<>();
        Application application = new Application();
        application.setId(1L);
        applications.add(application);

        when(applicationRepository.findByUser_Id(userId)).thenReturn(applications);

        Set<Application> result = applicationService.getApplicationsByUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(application));
        verify(applicationRepository).findByUser_Id(userId);
    }

    @Test
    void testCreateApplication() {
        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setUserId(1L);
        applicationDto.setJobId(2L);

        User user = new User();
        user.setId(1L);

        Job job = new Job();
        job.setId(2L);

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);

        Application savedApplication = new Application();
        savedApplication.setId(1L);
        savedApplication.setUser(user);
        savedApplication.setJob(job);

        ApplicationDto savedApplicationDto = new ApplicationDto();
        savedApplicationDto.setId(1L);

        when(applicationMapper.toEntity(applicationDto)).thenReturn(application);
        when(userService.getUserByModelId(1L)).thenReturn(user);
        when(jobService.getJobByModelId(2L)).thenReturn(job);
        when(applicationRepository.save(application)).thenReturn(savedApplication);
        when(applicationMapper.toDto(savedApplication)).thenReturn(savedApplicationDto);

        ApplicationDto result = applicationService.createApplication(applicationDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(applicationMapper).toEntity(applicationDto);
        verify(userService).getUserByModelId(1L);
        verify(jobService).getJobByModelId(2L);
        verify(applicationRepository).save(application);
        verify(applicationMapper).toDto(savedApplication);
    }

    @Test
    void testUpdateApplication() {
        Long applicationId = 1L;
        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setStatus("APPROVED");

        Application existingApplication = new Application();
        existingApplication.setId(applicationId);
        existingApplication.setStatus("PENDING");

        Application updatedApplication = new Application();
        updatedApplication.setId(applicationId);
        updatedApplication.setStatus("APPROVED");

        ApplicationDto updatedApplicationDto = new ApplicationDto();
        updatedApplicationDto.setId(applicationId);
        updatedApplicationDto.setStatus("APPROVED");

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(existingApplication));
        when(applicationRepository.save(existingApplication)).thenReturn(updatedApplication);
        when(applicationMapper.toDto(updatedApplication)).thenReturn(updatedApplicationDto);

        ApplicationDto result = applicationService.updateApplication(applicationId, applicationDto);

        assertNotNull(result);
        assertEquals("APPROVED", result.getStatus());
        verify(applicationRepository).findById(applicationId);
        verify(applicationRepository).save(existingApplication);
        verify(applicationMapper).toDto(updatedApplication);
    }

    @Test
    void testDeleteApplication() {
        Long applicationId = 1L;
        Application application = new Application();
        application.setId(applicationId);

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        doNothing().when(applicationRepository).delete(application);

        applicationService.deleteApplication(applicationId);

        verify(applicationRepository).findById(applicationId);
        verify(applicationRepository).delete(application);
    }

    @Test
    void testGetApplicationsByJobId() {
        Long jobId = 1L;
        Application application = new Application();
        application.setId(1L);
        application.setStatus("PENDING");
        application.setJob(new Job());
        application.setUser(new User());

        List<Application> applications = new ArrayList<>();
        applications.add(application);

        when(applicationRepository.findByJob_Id(jobId)).thenReturn(applications);

        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setStatus("PENDING");
        applicationDto.setJobId(1L);
        applicationDto.setUserId(2L);

        when(applicationMapper.toDto(application)).thenReturn(applicationDto);

        List<ApplicationDto> result = applicationService.getApplicationsByJobId(jobId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getStatus());
        assertEquals(1L, result.get(0).getJobId());
        assertEquals(2L, result.get(0).getUserId());
    }

    @Test
    void testCreateApplicationForUser() {
        Long userId = 1L;
        Long jobId = 1L;

        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setStatus("PENDING");
        applicationDto.setJobId(jobId);

        User user = new User();
        user.setId(userId);

        Job job = new Job();
        job.setId(jobId);

        Application application = new Application();
        application.setStatus("PENDING");
        application.setUser(user);
        application.setJob(job);

        ApplicationDto returnedDto = new ApplicationDto();
        returnedDto.setStatus("PENDING");
        returnedDto.setUserId(userId);
        returnedDto.setJobId(jobId);

        when(userService.getUserByModelId(userId)).thenReturn(user);
        when(jobService.getJobByModelId(jobId)).thenReturn(job);
        when(applicationMapper.toEntity(applicationDto)).thenReturn(application);
        when(applicationRepository.save(application)).thenReturn(application);
        when(applicationMapper.toDto(application)).thenReturn(returnedDto);

        ApplicationDto result = applicationService.createApplicationForUser(userId, applicationDto);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals(userId, result.getUserId());
        assertEquals(jobId, result.getJobId());
        verify(applicationRepository).save(application);
    }
}
