package isr.naya.admiralproj.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = {"year", "month"})
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"year", "month"})
@Builder
@Table(name = "datelocks")
public class DateLock extends BaseEntity {

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
}
