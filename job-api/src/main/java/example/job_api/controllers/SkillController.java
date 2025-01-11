package example.job_api.controllers;

import example.job_api.dto.SkillDto;
import example.job_api.dto.UserDto;
import example.job_api.entities.User;
import example.job_api.services.skill.SkillService;
import example.job_api.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/skills")
public class SkillController {
    private final SkillService skillService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<SkillDto> createSkill(@RequestBody SkillDto skillDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.createSkill(skillDTO));
    }

    @GetMapping
    public ResponseEntity<List<SkillDto>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDto> getSkillById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkillById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillDto> updateSkill(@PathVariable Long id, @RequestBody SkillDto skillDTO) {
        return ResponseEntity.ok(skillService.updateSkill(id, skillDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{skillId}/users")
    public ResponseEntity<List<UserDto>> getUsersBySkill(@PathVariable Long skillId) {
        List<UserDto> users = userService.getUsersBySkill(skillId);
        return ResponseEntity.ok(users);
    }
}