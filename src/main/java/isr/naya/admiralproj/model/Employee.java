package isr.naya.admiralproj.model;

import com.google.common.collect.Sets;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;


@Data
@Entity
@Table(name = "employees")
@EqualsAndHashCode(of = "passportId")
public class Employee {

    @Id
    @GenericGenerator(name = "empl_seq", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = "empl_seq"),
            @Parameter(name = "initial_value", value = "1000"),
            @Parameter(name = "increment_size", value = "1")
    })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empl_seq")
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotNull
    @Column(name = "passportid", nullable = false)
    private String passportId;

    @Column(name = "birthday")
    private LocalDate birthday;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "hired", columnDefinition = "timestamp default now()")
    private LocalDate hired;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "private_phone")
    private String privatePhone;

    @Column(name = "company_phone")
    private String companyPhone;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "employee_roles", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles = newHashSet(Role.ROLE_USER);

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE)
    private List<WorkAgreement> workAgreements;
}
