package example.job_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Job {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String title;

        @Column(nullable = false, length = 1000)
        private String description;

        private String location;

        @ManyToOne
        @JoinColumn(name = "company_id", nullable = false)
        private Company company;

        @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<Application> applications;
    }

