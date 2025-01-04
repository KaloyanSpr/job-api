package example.job_api.dto;

import jdk.jfr.SettingDefinition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CompanyDto {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Set<Long> jobIds;
}
