package example.job_api.controllers;

import example.job_api.dto.ApplicationDto;
import example.job_api.entities.Application;
import example.job_api.mappers.ApplicationMapper;
import example.job_api.repositories.ApplicationRepository;
import example.job_api.services.application.ApplicationService;
import example.job_api.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import example.job_api.dto.UserDto;

import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ApplicationService applicationService;
    private final ApplicationMapper applicationMapper;

    @Autowired
    public UserController(UserService userService, ApplicationService applicationService, ApplicationMapper applicationMapper) {
        this.userService = userService;
        this.applicationService = applicationService;
        this.applicationMapper = applicationMapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
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

    @GetMapping("/{id}/applications")
    public ResponseEntity<Set<ApplicationDto>> getApplicationsByUserId(@PathVariable Long userId) {
        Set<Application> applications = applicationService.getApplicationsByUserId(userId);
        Set<ApplicationDto> applicationDtos = applications.stream()
                .map(applicationMapper::toDto)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(applicationDtos);
    }

}

