package example.job_api.services.skill;

import example.job_api.dto.SkillDto;
import example.job_api.entities.Skill;

import java.util.Set;

public interface SkillService {

    SkillDto getSkillById(Long skillId);
    Set<Skill> getSkillsByIds(Set<Long> skillIds);
}
