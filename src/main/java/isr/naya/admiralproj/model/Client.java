package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
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
    @SequenceGenerator(name = "address_seq", sequenceName = "address_seq", allocationSize = 1, initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
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
