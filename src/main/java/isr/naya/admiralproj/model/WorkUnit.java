package isr.naya.admiralproj.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true, of = "")
@Entity
@Table(name = "work_units")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkUnit extends BaseEntity{

    @NotNull
    @Column(name = "start", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime start;

    @NotNull
    @Column(name = "finish", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime finish;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "absence_type")
    private AbsenceType absenceType;

    @Column(name = "approved")
    private Boolean approved = false;

    @Size(max = 50)
    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_agreement_id", referencedColumnName = "id", nullable = false)
    private WorkAgreement workAgreement;

    @PrePersist
    public void checkAbsence() {
        this.duration = this.finish.getMinute() - this.start.getMinute();
    }
}
