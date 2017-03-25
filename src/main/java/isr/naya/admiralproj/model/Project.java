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

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "project")
    private List<WorkAgreement> workAgreements;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;
}