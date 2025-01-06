package example.job_api.services.skill.impl;

import example.job_api.dto.SkillDto;
import example.job_api.entities.Skill;
import example.job_api.mappers.SkillsMapper;
import example.job_api.repositories.SkillRepository;
import example.job_api.services.skill.SkillService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final SkillsMapper skillsMapper;

    @Override
    public SkillDto getSkillById(Long skillId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found with id: " + skillId));
        return skillsMapper.toDto(skill);
    }

    @Override
    public Set<Skill> getSkillsByIds(Set<Long> skillIds) {
        List<Skill> skills = skillRepository.findAllById(skillIds);
        if (skills.size() != skillIds.size()) {
            throw new EntityNotFoundException("One or more skills not found with provided IDs.");
        }
        return new HashSet<>(skills);
    }

    public SkillDto createSkill(SkillDto skillDto) {
        Skill skill = skillsMapper.toEntity(skillDto);
        Skill savedSkill = skillRepository.save(skill);
        return skillsMapper.toDto(savedSkill);
    }

    public List<SkillDto> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(skillsMapper::toDto)
                .collect(Collectors.toList());
    }

    public SkillDto updateSkill(Long id, SkillDto skillDto) {
        Skill existingSkill = skillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found with id: " + id));

        existingSkill.setName(skillDto.getName());

        if (skillDto.getUserIds() != null && !skillDto.getUserIds().isEmpty()) {
            existingSkill.setUsers(new HashSet<>(
                    skillsMapper.toEntity(skillDto).getUsers()
            ));
        }

        Skill updatedSkill = skillRepository.save(existingSkill);
        return skillsMapper.toDto(updatedSkill);
    }

    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new EntityNotFoundException("Skill not found with id: " + id);
        }
        skillRepository.deleteById(id);
    }

    @Override
    public Set<Skill> getSkillsForUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return skillRepository.findSkillsByUserId(userId);
    }

    @Override
    public Skill createOrGetSkill(SkillDto skillDto) {
        return skillRepository.findByName(skillDto.getName())
                .orElseGet(() -> {
                    Skill newSkill = new Skill();
                    newSkill.setName(skillDto.getName());
                    return skillRepository.save(newSkill);
                });
    }
}
