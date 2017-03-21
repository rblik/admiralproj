package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Blik on 03/20/2017.
 */

@Data
@Entity
@Table(name = "projects")
@EqualsAndHashCode(of = "id")
public class Project {

    @Id
    @SequenceGenerator(name = "proj_seq", sequenceName = "proj_seq", allocationSize = 1, initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proj_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<WorkAgreement> workAgreements;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;
}
