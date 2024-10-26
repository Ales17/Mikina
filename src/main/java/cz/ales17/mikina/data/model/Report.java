package cz.ales17.mikina.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Report extends AbstractEntity {

    private ReportType type;

    private byte[] bytes;

    private String fileName;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}