package isr.naya.admiralproj.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isr.naya.admiralproj.dto.Role;
import lombok.*;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static com.google.common.collect.Sets.newHashSet;
import static isr.naya.admiralproj.web.security.password.PasswordUtil.encode;

@Data
@Entity
@Table(name = "employees", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"passport_id"}),
        @UniqueConstraint(columnNames = {"email"})})
@EqualsAndHashCode(callSuper = false, of = "passportId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee extends BaseEntity{

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotNull
    @Column(name = "passport_id", nullable = false, unique = true)
    private String passportId;

    @Column(name = "birthday", columnDefinition = "date")
    private LocalDate birthday;

    @NotNull
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

//    @Size(min = 10)
    @Column(name = "private_phone")
    private String privatePhone;

//    @Size(min = 10)
    @Column(name = "company_phone")
    private String companyPhone;

    /**
     * regexp
     * https://github.com/srinath4ever/JavaTest/blob/master/JavaTest/src/com/core/regex/RegExDemo.java
     */
    @NotNull
    @JsonIgnore
    @JsonProperty(access = WRITE_ONLY)
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,20}$")
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "employee_roles", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.LAZY)
    @Singular
    private Set<Role> roles = newHashSet(Role.ROLE_USER);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "id", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE)
    private List<WorkAgreement> workAgreements;

    @PrePersist
    @PreUpdate
    public void encodePassword() {
        this.password = encode(this.password);
    }
}