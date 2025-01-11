package example.job_api.controllerTest;
import example.job_api.controllers.UserController;
import example.job_api.dto.ApplicationDto;
import example.job_api.dto.SkillDto;
import example.job_api.dto.UserDto;
import example.job_api.entities.Application;
import example.job_api.mappers.ApplicationMapper;
import example.job_api.mappers.SkillsMapper;
import example.job_api.services.application.ApplicationService;
import example.job_api.services.skill.SkillService;
import example.job_api.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ApplicationService applicationService;

    @Mock
    private ApplicationMapper applicationMapper;

    @Mock
    private SkillService skillService;

    @Mock
    private SkillsMapper skillMapper;

    @InjectMocks
    private UserController userController;

    private UserDto userDto;
    private ApplicationDto applicationDto;
    private SkillDto skillDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup UserDto
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setRole("JOB_SEEKER");
        userDto.setSkillIds(new HashSet<>());
        userDto.setApplicationIds(new HashSet<>());

        // Setup ApplicationDto
        applicationDto = new ApplicationDto();
        applicationDto.setId(1L);
        applicationDto.setStatus("PENDING");
        applicationDto.setUserId(1L);
        applicationDto.setJobId(1L);

        // Setup SkillDto
        skillDto = new SkillDto();
        skillDto.setId(1L);
        skillDto.setName("Java");
    }

    @Test
    void createUser_shouldReturnCreatedUser() {
        when(userService.createUser(userDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.createUser(userDto);

        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());

        verify(userService, times(1)).createUser(userDto);
    }

    @Test
    void getUserById_shouldReturnUser() {
        when(userService.getUserById(1L)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getUserById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());

        verify(userService, times(1)).getUserById(1L);
    }
    @Test
    void updateUser_shouldCallService() {
        doNothing().when(userService).updateUser(1L, userDto);

        ResponseEntity<Void> response = userController.updateUser(1L, userDto);

        assertEquals(200, response.getStatusCodeValue());

        verify(userService, times(1)).updateUser(1L, userDto);
    }

    @Test
    void deleteUser_shouldCallService() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(204, response.getStatusCodeValue());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void getApplicationsByUserId_shouldReturnApplications() {
        Set<Application> applications = Set.of(new Application());
        Set<ApplicationDto> applicationDtos = Set.of(applicationDto);

        when(applicationService.getApplicationsByUserId(1L)).thenReturn(applications);
        when(applicationMapper.toDto(any(Application.class))).thenReturn(applicationDto);

        ResponseEntity<Set<ApplicationDto>> response = userController.getApplicationsByUserId(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(applicationDtos, response.getBody());

        verify(applicationService, times(1)).getApplicationsByUserId(1L);
    }

    @Test
    void addSkillToUser_shouldCallService() {
        doNothing().when(userService).addSkillToUser(1L, skillDto);

        ResponseEntity<String> response = userController.addSkillToUser(1L, skillDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Skill added successfully to the user.", response.getBody());

        verify(userService, times(1)).addSkillToUser(1L, skillDto);
    }
}