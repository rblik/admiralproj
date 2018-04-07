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

    @Column(name = "company_number")
    private String companyNumber;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column( name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "client_number")
    private String clientNumber;

    @Column(name = "color")
    private String color;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "client_phones", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "phone")
    @Singular
    private Set<String> phones;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "client_id", nullable = false)
    @Singular
    private Set<Address> addresses;

    @Transient
    //@OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @Singular
    private List<Project> projects;
}