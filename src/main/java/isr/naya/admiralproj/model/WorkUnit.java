package isr.naya.admiralproj.model;

import isr.naya.admiralproj.dto.AbsenceType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Data
@EqualsAndHashCode(callSuper = true, of = {})
@Entity
@Table(name = "work_units")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkUnit extends BaseEntity {

    @NotNull
    @Column(name = "work_date", nullable = false, columnDefinition = "date")
    private LocalDate date;

    @NotNull
    @Column(name = "start", nullable = false, columnDefinition = "time")
    private LocalTime start;

    @NotNull
    @Column(name = "finish", nullable = false, columnDefinition = "time")
    private LocalTime finish;

    @Column(name = "duration")
    private Long duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "absence_type")
    private AbsenceType absenceType;

    @Size(max = 75)
    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_agreement_id", referencedColumnName = "id", nullable = false)
    private WorkAgreement workAgreement;

    @PrePersist
    @PreUpdate
    public void calculateDuration() {
        this.duration = ChronoUnit.MINUTES.between(this.start, this.finish);
        this.duration = this.duration == 1439 ? 1440 : this.duration;
    }
}
