package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
@Entity
@Table(name = "work_agreements")
@EqualsAndHashCode(of = "id")
public class WorkAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tariff_type")
    @Enumerated(EnumType.STRING)
    private TariffType tariffType = TariffType.HOURLY;

    @NotNull
    @Column(name = "tariff_amount", nullable = false)
    private Integer tariffAmount;

    @Column(name = "since", columnDefinition = "date default now()")
    private LocalDate since;

    @Column(name = "until", columnDefinition = "date")
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
