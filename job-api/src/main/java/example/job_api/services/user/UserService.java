package example.job_api.services.user;

import example.job_api.dto.SkillDto;
import example.job_api.dto.UserDto;
import example.job_api.entities.Application;
import example.job_api.entities.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long userId);
    List<User> getUsersByIds(List<Long> ids);
    User getUserByModelId(Long modelId);
    void updateUser(Long id,UserDto userDto);
    void deleteUser(Long id);
    void addSkillToUser(Long userId, SkillDto skillDto);
    List<UserDto> getUsersBySkill(Long skillId);
}
