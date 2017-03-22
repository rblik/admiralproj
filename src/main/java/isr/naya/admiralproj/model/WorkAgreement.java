package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@Table(name = "work_agreements")
@EqualsAndHashCode(callSuper = true, of = "")
public class WorkAgreement extends BaseEntity{

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

    @OneToMany(mappedBy = "workAgreement", cascade = CascadeType.REMOVE)
    private List<WorkUnit> workUnits;
}
