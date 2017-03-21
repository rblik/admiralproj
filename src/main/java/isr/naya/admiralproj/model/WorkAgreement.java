package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


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
    private TariffType tariffType = TariffType.HOURLY;

    @NotNull
    @Column(name = "tariff_amount", nullable = false)
    private Integer tariffAmount;

    @Column(name = "since", columnDefinition = "timestamp default now()")
    private LocalDate since;

    @Column(name = "until")
    private LocalDate until;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id",nullable = false)
    private Employee employee;
}
