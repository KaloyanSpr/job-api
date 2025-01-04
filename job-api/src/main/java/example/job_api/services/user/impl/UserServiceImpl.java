package example.job_api.services.user.impl;

import example.job_api.dto.UserDto;
import example.job_api.entities.Application;
import example.job_api.entities.Skill;
import example.job_api.entities.User;
import example.job_api.enums.Role;
import example.job_api.mappers.UserMapper;
import example.job_api.repositories.UserRepository;
import example.job_api.services.skill.SkillService;
import example.job_api.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SkillService skillService;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userDto == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        return userMapper.toDto(user);
    }


    @Override
    public List<User> getUsersByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("The list of IDs cannot be null or empty");
        }
        List<User> users = userRepository.findAllById(ids);
        if (users.isEmpty()) {
            throw new RuntimeException("No users found for the given IDs: " + ids);
        }
        return users;
    }

    @Override
    public User getUserByModelId(Long modelId) {
        if (modelId == null) {
            throw new IllegalArgumentException("Model ID cannot be null");
        }
        return userRepository.findById(modelId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + modelId));
    }
    @Override
    public void updateUser(Long id, UserDto userDto) {
        if (id == null || userDto == null) {
            throw new IllegalArgumentException("User ID and UserDto cannot be null");
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setRole(Role.valueOf(userDto.getRole()));

        Set<Skill> skills = skillService.getSkillsByIds(userDto.getSkillIds());
        existingUser.setSkills(skills);

        userRepository.save(existingUser);
    }


    @Override
    public void deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

}
