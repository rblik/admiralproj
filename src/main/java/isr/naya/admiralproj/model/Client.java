package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "clients")
@EqualsAndHashCode(of = "id")
@Data
public class Client {

    @Id
    @GenericGenerator(name = "client_seq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = "client_seq"),
            @Parameter(name = "initial_value", value = "1000"),
            @Parameter(name = "increment_size", value = "1")
    })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "company_number", nullable = false)
    private Integer companyNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "company_phones", joinColumns = @JoinColumn(name = "company_id"))
    //@Column(name = "phone")
    private Set<String> phones;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Address> addresses;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Project> projects;
}
