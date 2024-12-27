package example.job_api.repositories;

import example.job_api.entities.Job;
import example.job_api.entities.Skill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SkillRepository {

        // Find skill by name
        Optional<Skill> findByName(String name);

        // Custom query to list all job postings requiring a specific skill
        @Query("SELECT jp FROM Job jp JOIN jp.skills s WHERE s.name = :skillName")
        List<Job> findJobPostingsBySkillName(String skillName);
    }

