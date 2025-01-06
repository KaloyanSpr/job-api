package example.job_api.services.skill;

import example.job_api.dto.SkillDto;
import example.job_api.entities.Skill;

import java.util.List;
import java.util.Set;

public interface SkillService {

    SkillDto getSkillById(Long skillId);
    Set<Skill> getSkillsByIds(Set<Long> skillIds);
    SkillDto createSkill(SkillDto skillDto);
    List<SkillDto> getAllSkills();
    SkillDto updateSkill(Long id, SkillDto skillDto);
    void deleteSkill(Long id);
    Set<Skill> getSkillsForUser(Long userId);
    Skill createOrGetSkill(SkillDto skillDto);
}
