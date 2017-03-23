package isr.naya.admiralproj.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@Table(name = "work_agreements")
@EqualsAndHashCode(callSuper = true, of = "")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkAgreement extends BaseEntity{

    @NotNull
    @Column(name = "since", columnDefinition = "date", nullable = false)
    private LocalDate since;

    @NotNull
    @Column(name = "until", columnDefinition = "date", nullable = false)
    private LocalDate until;

    @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    private Tariff tariff;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id",nullable = false)
    private Employee employee;

    @OneToMany(mappedBy = "workAgreement", cascade = CascadeType.REMOVE)
    private List<WorkUnit> workUnits;
}
