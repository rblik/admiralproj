package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by Blik on 03/20/2017.
 */
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
    @Column(name = "telephone")
    private List<String> phones;

    @OneToMany(mappedBy = "client", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Address> adresses;

    @OneToMany(mappedBy = "client", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Project> projectssById;
}
