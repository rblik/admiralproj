package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Blik on 03/21/2017.
 */
@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "work_units")
public class WorkUnit {

    @Id
    @SequenceGenerator(name = "work_unit_seq", sequenceName = "work_unit_seq", allocationSize = 1, initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_unit_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "start", nullable = false)
    private LocalDateTime start;

    @Column(name = "finish", nullable = false)
    private LocalDateTime finish;

    @Column(name = "approved")
    private boolean approved;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "work_agreement_id", referencedColumnName = "id")
    private WorkAgreement workAgreement;
}
