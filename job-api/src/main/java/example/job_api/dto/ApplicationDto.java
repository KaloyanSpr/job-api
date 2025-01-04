package example.job_api.dto;

import example.job_api.entities.Job;
import example.job_api.entities.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDto {
    private Long id;
    private String status;
    private Long userId;
    private Long jobId;
}
