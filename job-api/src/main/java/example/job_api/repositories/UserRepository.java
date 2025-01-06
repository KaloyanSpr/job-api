package example.job_api.repositories;

import example.job_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    List<User> findAllByIdIn(List<Long> ids);
    @Query("SELECT u FROM User u JOIN u.skills s WHERE s.id = :skillId")
    List<User> findUsersBySkillId(@Param("skillId") Long skillId);
}
