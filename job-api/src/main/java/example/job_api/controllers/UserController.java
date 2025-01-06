package example.job_api.controllers;

import example.job_api.dto.ApplicationDto;
import example.job_api.dto.SkillDto;
import example.job_api.entities.Application;
import example.job_api.entities.Skill;
import example.job_api.mappers.ApplicationMapper;
import example.job_api.mappers.SkillsMapper;
import example.job_api.repositories.ApplicationRepository;
import example.job_api.services.application.ApplicationService;
import example.job_api.services.skill.SkillService;
import example.job_api.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import example.job_api.dto.UserDto;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ApplicationService applicationService;
    private final ApplicationMapper applicationMapper;
    private final SkillService skillService;
    private final SkillsMapper skillMapper;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        try {
            UserDto userDto = userService.getUserById(id);
            return ResponseEntity.ok(userDto);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id, e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UserDto userDTO) {
        userService.updateUser(id, userDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/applications")
    public ResponseEntity<Set<ApplicationDto>> getApplicationsByUserId(@PathVariable Long userId) {
        Set<Application> applications = applicationService.getApplicationsByUserId(userId);
        Set<ApplicationDto> applicationDtos = applications.stream()
                .map(applicationMapper::toDto)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(applicationDtos);
    }

    @PostMapping("/{userId}/applications")
    public ResponseEntity<ApplicationDto> createApplicationForUser(
            @PathVariable Long userId,
            @RequestBody ApplicationDto applicationDto) {
        ApplicationDto createdApplication = applicationService.createApplicationForUser(userId, applicationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApplication);
    }

    @GetMapping("/{userId}/skills")
    public ResponseEntity<Set<SkillDto>> getSkillsForUser(@PathVariable Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        Set<Skill> skills = skillService.getSkillsForUser(userId);

        Set<SkillDto> skillDtos = skills.stream()
                .map(skillMapper::toDto)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(skillDtos);
    }

    @PostMapping("/{userId}/skills")
    public ResponseEntity<String> addSkillToUser(@PathVariable Long userId, @RequestBody SkillDto skillDto) {
        userService.addSkillToUser(userId, skillDto);
        return ResponseEntity.ok("Skill added successfully to the user.");
    }
}

