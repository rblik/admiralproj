package isr.naya.admiralproj.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@Table(name = "work_agreements")
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkAgreement extends BaseEntity{

    @Column(name = "active", columnDefinition = "boolean default true")
    private boolean active  = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id",nullable = false)
    private Employee employee;

    @OneToMany(mappedBy = "workAgreement", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<WorkUnit> workUnits;
}
