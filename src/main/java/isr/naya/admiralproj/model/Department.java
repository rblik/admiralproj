package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Blik on 03/21/2017.
 */
@Data
@Entity
@Table(name = "departments")
@EqualsAndHashCode(of = "id")
public class Department {

    @Id
    @SequenceGenerator(name = "dep_seq", sequenceName = "dep_seq", allocationSize = 1, initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dep_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.REMOVE)
    private List<Employee> employees;
}
