package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = true, of = "")
@Entity
@Table(name = "work_units")
public class WorkUnit extends BaseEntity{

    @Column(name = "start", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime start;

    @Column(name = "finish", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime finish;

    @Enumerated(EnumType.STRING)
    @Column(name = "absence_type")
    private AbsenceType absenceType;

    @Column(name = "approved")
    private Boolean approved = false;

    @Size(max = 50)
    @Column(name = "comment")
    private String comment;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "work_agreement_id", referencedColumnName = "id", nullable = false)
    private WorkAgreement workAgreement;

    @PrePersist
    public void checkAbsence() {
        if (this.absenceType != null && this.start != null) {
            LocalDate date = this.start.toLocalDate();
            this.start = date.atStartOfDay();
            this.finish = date.atTime(LocalTime.MAX);
        }
    }
}
