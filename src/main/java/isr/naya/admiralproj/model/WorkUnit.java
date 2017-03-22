package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "work_units")
public class WorkUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

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
            this.start = this.start.truncatedTo(ChronoUnit.DAYS);
            this.finish = this.finish.withHour(23).withMinute(59);
        }
    }
}
