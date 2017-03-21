package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Created by Blik on 03/21/2017.
 */
@Data
@Entity
@Table(name = "work_agreements")
@EqualsAndHashCode(of = "id")
public class WorkAgreement {

    @Id
    @GenericGenerator(name = "work_seq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = "work_seq"),
            @Parameter(name = "initial_value", value = "1000"),
            @Parameter(name = "increment_size", value = "1")
    })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "tariff_type")
    @Enumerated(EnumType.STRING)
    private TariffType tariffType;

    @Column(name = "tariff_amount")
    private Integer tariffAmount;

    @Column(name = "since", nullable = false, columnDefinition = "timestamp default now()")
    private LocalDate since;

    @Column(name = "until", nullable = false, columnDefinition = "timestamp default now()")
    private LocalDate until;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
}
