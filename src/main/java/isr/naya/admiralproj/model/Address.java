package isr.naya.admiralproj.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "addresses")
@Data
@EqualsAndHashCode(callSuper = false,of = {"area", "city", "street", "houseNumber"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends BaseEntity{

    @NotNull
    @Column(name = "area", nullable = false)
    private String area;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "house_number")
    private String houseNumber;
}