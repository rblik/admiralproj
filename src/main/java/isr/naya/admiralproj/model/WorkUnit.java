package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "work_units")
public class WorkUnit {

    @Id
    @GenericGenerator(name = "work_unit_seq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = "work_unit_seq"),
            @Parameter(name = "initial_value", value = "1000"),
            @Parameter(name = "increment_size", value = "1")
    })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_unit_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "start", nullable = false)
    private LocalDateTime start;

    @Column(name = "finish", nullable = false)
    private LocalDateTime finish;

    @Enumerated(EnumType.STRING)
    @Column(name = "absence_type")
    private AbsenceType absenceType;

    @Column(name = "approved")
    private Boolean approved = true;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "work_agreement_id", referencedColumnName = "id", nullable = false)
    private WorkAgreement workAgreement;
}
