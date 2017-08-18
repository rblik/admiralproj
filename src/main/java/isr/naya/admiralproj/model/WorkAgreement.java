package isr.naya.admiralproj.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;


@Data
@Entity
@Table(name = "work_agreements")
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"active"})
@Builder
public class WorkAgreement extends BaseEntity{

    @Column(name = "active")
    private boolean active;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "tariff_id", referencedColumnName = "id")
    private Tariff tariff;

    @JsonIgnore
    @JsonProperty(access = WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id",nullable = false)
    private Employee employee;

    @Transient
    @OneToMany(mappedBy = "workAgreement", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<WorkUnit> workUnits;
}
