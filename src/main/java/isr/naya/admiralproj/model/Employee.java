package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Created by Blik on 03/21/2017.
 */
@Data
@Entity
@Table(name = "employees")
@EqualsAndHashCode(of = "id")
public class Employee {

    @Id
    @SequenceGenerator(name = "empl_seq", sequenceName = "empl_seq", allocationSize = 1, initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empl_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "passportId", nullable = false)
    private String passportId;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "hired", columnDefinition = "timestamp default now()")
    private LocalDate hired;

    @Column(name = "active")
    private boolean active;

    @Column(name = "private_phone")
    private String privatePhone;

    @Column(name = "company_phone")
    private String companyPhone;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "employee_roles", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE)
    private List<WorkAgreement> workAgreements;
}
