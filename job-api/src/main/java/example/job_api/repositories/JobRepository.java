package example.job_api.repositories;

import example.job_api.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    // Custom query to find jobs by company ID
    @Query("SELECT j FROM Job j WHERE j.company.id = :companyId")
    List<Job> findByCompanyId(@Param("companyId") Long companyId);

    // Custom query to search jobs by title containing a keyword
    @Query("SELECT j FROM Job j WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Job> searchByTitle(@Param("keyword") String keyword);

    // Custom query to find jobs in a specific location
    @Query("SELECT j FROM Job j WHERE j.location = :location")
    List<Job> findByLocation(@Param("location") String location);
}
