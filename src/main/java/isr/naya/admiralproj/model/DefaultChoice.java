package isr.naya.admiralproj.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "default_choices")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefaultChoice extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "agreement_id", referencedColumnName = "id")
    private WorkAgreement agreement;

    @Column(name = "start", columnDefinition = "time")
    private LocalTime start;

    @Column(name = "finish", columnDefinition = "time")
    private LocalTime finish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
}
