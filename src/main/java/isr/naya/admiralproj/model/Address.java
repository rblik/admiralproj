package isr.naya.admiralproj.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "addresses")
@Data
@EqualsAndHashCode(of = {"area", "city", "street", "houseNumber"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends BaseEntity{

    @Column(name = "area")
    private String area;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "house_number")
    private String houseNumber;
}