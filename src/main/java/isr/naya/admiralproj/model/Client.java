package isr.naya.admiralproj.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "clients")
@EqualsAndHashCode(callSuper = true, of = {})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client extends BaseEntity{

    @NotNull
    @Column(name = "company_number", nullable = false)
    private Integer companyNumber;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "client_phones", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "phone")
    @Singular
    private Set<String> phones;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", nullable = false)
    @Singular
    private Set<Address> addresses;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @Singular
    private List<Project> projects;
}