package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by Blik on 03/20/2017.
 */
@Entity
@Table(name = "addresses")
@Data
@EqualsAndHashCode(of = "id")
public class Address {

    @Id
    @SequenceGenerator(name = "address_seq", sequenceName = "address_seq", allocationSize = 1, initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "area", nullable = false)
    private String area;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "house_number", nullable = false)
    private Short houseNumber;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;
}
