package isr.naya.admiralproj.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract extends BaseEntity {

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "min_hours")
    private Integer minHours;

    @NotNull
    @Column(name = "since", columnDefinition = "date", nullable = false)
    private LocalDate since;

    @NotNull
    @Column(name = "until", columnDefinition = "date", nullable = false)
    private LocalDate until;
}
