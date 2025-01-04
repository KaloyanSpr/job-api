package example.job_api.services.skill.impl;

import example.job_api.dto.SkillDto;
import example.job_api.entities.Skill;
import example.job_api.repositories.SkillRepository;
import example.job_api.services.skill.SkillService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    @Override
    public SkillDto getSkillById(Long skillId) {
     return null;
    }

    @Override
    public Set<Skill> getSkillsByIds(Set<Long> skillIds) {

        List<Skill> skills = skillRepository.findAllById(skillIds);
        return new HashSet<>(skills);
    }
}
