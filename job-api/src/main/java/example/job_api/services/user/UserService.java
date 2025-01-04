package example.job_api.services.user;

import example.job_api.dto.UserDto;
import example.job_api.entities.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserId(Long userId);
    List<User> getUsersByIds(List<Long> ids);
    User getUserByModelId(Long modelId);
}
