package example.job_api.serviceTests;

import example.job_api.dto.UserDto;
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

import java.util.Optional;

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

        verify(userRepository, times(1)).save(user); // Ensure save was called
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

        verify(userRepository, times(1)).findById(1L); // Ensure findById was called
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
        assertEquals("User not found with ID: 1", exception.getMessage());

        verify(userRepository, times(1)).findById(1L); // Ensure findById was called
    }
}

