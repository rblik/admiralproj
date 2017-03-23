package isr.naya.admiralproj.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "contracts")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract extends BaseEntity {

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "min_hours")
    private Integer minHours;
}
