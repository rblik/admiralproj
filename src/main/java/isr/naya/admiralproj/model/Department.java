package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Data
@Entity
@Table(name = "departments")
@EqualsAndHashCode(callSuper = true, of = "")
public class Department extends BaseEntity{

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.REMOVE)
    private Set<Employee> employees;
}
