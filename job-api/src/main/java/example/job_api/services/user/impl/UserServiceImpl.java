package example.job_api.services.user.impl;

import example.job_api.dto.UserDto;
import example.job_api.entities.User;
import example.job_api.mappers.UserMapper;
import example.job_api.repositories.UserRepository;
import example.job_api.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User createUser(UserDto userDto) {
        if (userDto == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }
        User user = UserMapper.toEntity(userDto);

        return userRepository.save(user);
    }

    @Override
    public UserDto getUserId(Long userId) {
        return null;
    }

    @Override
    public List<User> getUsersByIds(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    public User getUserByModelId(Long modelId) {
        return userRepository.findById(modelId).orElse(null);
    }
}
