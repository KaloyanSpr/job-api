package example.job_api.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.job_api.controllers.SkillController;
import example.job_api.dto.SkillDto;
import example.job_api.dto.UserDto;
import example.job_api.services.skill.SkillService;
import example.job_api.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SkillControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SkillService skillService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SkillController skillController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(skillController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createSkill_ShouldReturnCreatedSkill() throws Exception {
        SkillDto skillDto = new SkillDto();
        skillDto.setId(1L);
        skillDto.setName("Java");

        when(skillService.createSkill(any(SkillDto.class))).thenReturn(skillDto);

        mockMvc.perform(post("/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skillDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java"));

        verify(skillService, times(1)).createSkill(any(SkillDto.class));
    }
    @Test
    void getAllSkills_ShouldReturnListOfSkills() throws Exception {
        SkillDto skill1 = new SkillDto();
        skill1.setId(1L);
        skill1.setName("Java");

        SkillDto skill2 = new SkillDto();
        skill2.setId(2L);
        skill2.setName("Python");

        List<SkillDto> skills = Arrays.asList(skill1, skill2);

        when(skillService.getAllSkills()).thenReturn(skills);

        mockMvc.perform(get("/skills")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Java"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Python"));

        verify(skillService, times(1)).getAllSkills();
    }
    @Test
    void getSkillById_ShouldReturnSkill() throws Exception {
        SkillDto skillDto = new SkillDto();
        skillDto.setId(1L);
        skillDto.setName("Java");

        when(skillService.getSkillById(1L)).thenReturn(skillDto);

        mockMvc.perform(get("/skills/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java"));

        verify(skillService, times(1)).getSkillById(1L);
    }

    @Test
    void updateSkill_ShouldReturnUpdatedSkill() throws Exception {
        SkillDto skillDto = new SkillDto();
        skillDto.setId(1L);
        skillDto.setName("Updated Java");

        when(skillService.updateSkill(eq(1L), any(SkillDto.class))).thenReturn(skillDto);

        mockMvc.perform(put("/skills/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skillDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Java"));

        verify(skillService, times(1)).updateSkill(eq(1L), any(SkillDto.class));
    }
    @Test
    void deleteSkill_ShouldReturnNoContent() throws Exception {
        doNothing().when(skillService).deleteSkill(1L);

        mockMvc.perform(delete("/skills/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(skillService, times(1)).deleteSkill(1L);
    }

    @Test
    void getUsersBySkill_ShouldReturnListOfUsers() throws Exception {
        UserDto user1 = new UserDto();
        user1.setId(1L);
        user1.setFirstName("John");
        user1.setLastName("Doe");

        UserDto user2 = new UserDto();
        user2.setId(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");

        List<UserDto> users = Arrays.asList(user1, user2);

        when(userService.getUsersBySkill(1L)).thenReturn(users);

        mockMvc.perform(get("/skills/1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(userService, times(1)).getUsersBySkill(1L);
    }

}
