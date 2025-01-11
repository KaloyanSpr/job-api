package example.job_api.controllerTest;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.job_api.controllers.JobController;
import example.job_api.dto.JobDto;
import example.job_api.services.application.ApplicationService;
import example.job_api.services.job.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.*;

class JobControllerTest {

    @Mock
    private JobService jobService;

    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private JobController jobController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createJobPosting_ShouldReturnCreatedJob() throws Exception {
        JobDto jobDto = new JobDto(1L, "Software Engineer", "Develop applications", "Remote", 1L, new HashSet<>());

        when(jobService.createJob(any(JobDto.class))).thenReturn(jobDto);

        mockMvc.perform(post("/job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jobDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Software Engineer"))
                .andExpect(jsonPath("$.description").value("Develop applications"))
                .andExpect(jsonPath("$.location").value("Remote"));

        verify(jobService, times(1)).createJob(any(JobDto.class));
    }

    @Test
    void getAllJobPostings_ShouldReturnJobList() throws Exception {
        Set<JobDto> jobs = new HashSet<>();
        jobs.add(new JobDto(1L, "Software Engineer", "Develop applications", "Remote", 1L, new HashSet<>()));
        jobs.add(new JobDto(2L, "Product Manager", "Oversee projects", "Onsite", 1L, new HashSet<>()));

        when(jobService.getAllJobs()).thenReturn(jobs);

        mockMvc.perform(get("/job")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id").exists());

        verify(jobService, times(1)).getAllJobs();
    }

}