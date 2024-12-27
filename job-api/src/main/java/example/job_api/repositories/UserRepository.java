package example.job_api.repositories;

import example.job_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username
    Optional<User> findByUsername(String username);

    // Find user by email
    Optional<User> findByEmail(String email);

    // Custom query to search users by role
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findUsersByRole(String role);
}
