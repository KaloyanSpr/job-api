package example.job_api.repositories;

import example.job_api.entities.Job;
import example.job_api.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SkillRepository  extends JpaRepository<Skill, Long>{
    @Query("SELECT s FROM Skill s JOIN s.users u WHERE u.id = :userId")
    Set<Skill> findSkillsByUserId(@Param("userId") Long userId);

    Optional<Skill> findByName(String name);
}