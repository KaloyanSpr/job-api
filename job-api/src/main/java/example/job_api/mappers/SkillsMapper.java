package example.job_api.mappers;

import example.job_api.dto.SkillDto;
import example.job_api.entities.Skill;
import example.job_api.entities.User;
import example.job_api.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Component
@AllArgsConstructor
public class SkillsMapper {
    private final UserService userService;
    public  SkillDto toDto(Skill skill) {
        if (skill == null) {
            return null;
        }

        SkillDto skillDto = new SkillDto();
        skillDto.setId(skill.getId());
        skillDto.setName(skill.getName());
        Set<Long> userIds = skill.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        skillDto.setUserIds(userIds);

        return skillDto;
    }

    public  Skill toEntity(SkillDto skillDto) {
        if (skillDto == null) {
            return null;
        }
        Skill skill = new Skill();

        if (skillDto.getUserIds() != null && !skillDto.getUserIds().isEmpty()) {
            List<User> users = userService.getUsersByIds(new ArrayList<>(skillDto.getUserIds()));
            skill.setUsers(new HashSet<>(users));
        }

        skill.setId(skillDto.getId());
        skill.setName(skillDto.getName());

        return skill;
    }
}
