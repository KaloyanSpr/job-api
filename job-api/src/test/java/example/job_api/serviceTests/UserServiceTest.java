package example.job_api.serviceTests;

import example.job_api.dto.SkillDto;
import example.job_api.dto.UserDto;
import example.job_api.entities.Skill;
import example.job_api.entities.User;
import example.job_api.mappers.UserMapper;
import example.job_api.repositories.UserRepository;
import example.job_api.services.skill.SkillService;
import example.job_api.services.user.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import example.job_api.enums.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SkillService skillService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@example.com");

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
    }

    @Test
    void testCreateUser() {
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(userDto.getFirstName(), createdUser.getFirstName());
        assertEquals(userDto.getLastName(), createdUser.getLastName());
        assertEquals(userDto.getEmail(), createdUser.getEmail());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto fetchedUser = userService.getUserById(1L);

        assertNotNull(fetchedUser);
        assertEquals(userDto.getFirstName(), fetchedUser.getFirstName());
        assertEquals(userDto.getLastName(), fetchedUser.getLastName());
        assertEquals(userDto.getEmail(), fetchedUser.getEmail());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
        assertEquals("User not found with ID: 1", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateUser() {
        UserDto updateDto = new UserDto();
        updateDto.setFirstName("Jane");
        updateDto.setLastName("Doe");
        updateDto.setEmail("jane.doe@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.updateUser(1L, updateDto);

        assertEquals("Jane", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("jane.doe@example.com", user.getEmail());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserNotFound() {
        UserDto updateDto = new UserDto();
        updateDto.setFirstName("Jane");
        updateDto.setLastName("Doe");
        updateDto.setEmail("jane.doe@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, updateDto));
        assertEquals("User not found with ID: 1", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        assertEquals("User not found with ID: 1", exception.getMessage());

        verify(userRepository, times(1)).existsById(1L);
    }


    @Test
    void testGetUsersByIds() {
        List<Long> ids = List.of(1L, 2L);
        User user1 = new User(1L, "John", "Doe", "john.doe@example.com", Role.JOB_SEEKER, null, null);
        User user2 = new User(2L, "Jane", "Smith", "jane.smith@example.com", Role.JOB_SEEKER, null, null);
        when(userRepository.findAllById(ids)).thenReturn(List.of(user1, user2));

        List<User> users = userService.getUsersByIds(ids);

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        verify(userRepository, times(1)).findAllById(ids);
    }

    @Test
    void testGetUsersByIdsNotFound() {
        List<Long> ids = List.of(1L, 2L);
        when(userRepository.findAllById(ids)).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUsersByIds(ids));
        assertEquals("No users found for the given IDs: [1, 2]", exception.getMessage());

        verify(userRepository, times(1)).findAllById(ids);
    }

    @Test
    void testGetUserByModelId() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", Role.JOB_SEEKER, null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserByModelId(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getFirstName(), result.getFirstName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserByModelIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserByModelId(1L));
        assertEquals("User not found with ID: 1", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testAddSkillToUser() {

        Long userId = 1L;
        SkillDto skillDto = new SkillDto(1L, "Java",null);
        User user = new User(userId, "John", "Doe", "john.doe@example.com", Role.JOB_SEEKER, new HashSet<Skill>(), null);
        Skill skill = new Skill(1L, "Java", null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(skillService.createOrGetSkill(skillDto)).thenReturn(skill);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.addSkillToUser(userId, skillDto);

        assertTrue(user.getSkills().contains(skill));
        verify(userRepository, times(1)).findById(userId);
        verify(skillService, times(1)).createOrGetSkill(skillDto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testAddSkillToUserNotFound() {
        Long userId = 1L;
        SkillDto skillDto = new SkillDto(1L, "Java",null);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.addSkillToUser(userId, skillDto));
        assertEquals("User not found for ID: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(skillService, never()).createOrGetSkill(any(SkillDto.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUsersBySkill() {
        Long skillId = 1L;
        Skill skill = new Skill(skillId, "Java", null);

        User user1 = new User(1L, "John", "Doe", "john.doe@example.com", Role.JOB_SEEKER, Set.of(skill), null);
        User user2 = new User(2L, "Jane", "Smith", "jane.smith@example.com", Role.JOB_SEEKER, Set.of(skill), null);

        when(userRepository.findUsersBySkillId(skillId)).thenReturn(List.of(user1, user2));
        when(userMapper.toDto(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    UserDto userDto = new UserDto(user.getId(),user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().name(), null, null);
                    userDto.setSkillIds(user.getSkills().stream().map(Skill::getId).collect(Collectors.toSet()));
                    return userDto;
                });
        List<UserDto> users = userService.getUsersBySkill(skillId);

        assertEquals(2, users.size());
        assertEquals("John", users.get(0).getFirstName());
        assertEquals("Jane", users.get(1).getFirstName());
        verify(userRepository, times(1)).findUsersBySkillId(skillId);
        verify(userMapper, times(2)).toDto(any(User.class));
    }

    @Test
    void testGetUsersBySkillNotFound() {
        Long skillId = 1L;
        when(userRepository.findUsersBySkillId(skillId)).thenReturn(List.of());

        List<UserDto> users = userService.getUsersBySkill(skillId);

        assertTrue(users.isEmpty());
        verify(userRepository, times(1)).findUsersBySkillId(skillId);
        verify(userMapper, never()).toDto(any(User.class));
    }


}

