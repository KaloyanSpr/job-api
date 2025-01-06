package example.job_api.mappers;

import example.job_api.dto.UserDto;
import example.job_api.entities.Application;
import example.job_api.entities.User;
import example.job_api.enums.Role;
import example.job_api.entities.Skill;
import example.job_api.services.application.ApplicationService;
import example.job_api.services.skill.SkillService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().name());
        userDto.setSkillIds(user.getSkills() != null ?
                user.getSkills().stream().map(Skill::getId).collect(Collectors.toSet()) : null);
        userDto.setApplicationIds(user.getApplications() != null ?
                user.getApplications().stream().map(Application::getId).collect(Collectors.toSet()) : null);

        return userDto;
    }

    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setRole(Role.valueOf(userDto.getRole()));

        return user;
    }
}
