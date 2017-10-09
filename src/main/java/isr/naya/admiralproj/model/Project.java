package isr.naya.admiralproj.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Entity
@Table(name = "projects")
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseEntity{
    public Project(Integer id, String name) {
        super(id);
        this.name = name;
    }

    @Column( name = "is_enabled")
    private boolean isEnabled;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<WorkAgreement> workAgreements;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;
}