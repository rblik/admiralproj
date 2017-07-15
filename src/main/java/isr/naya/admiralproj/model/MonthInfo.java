package isr.naya.admiralproj.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by jonathan on 14/07/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "month_infos")
@AllArgsConstructor
@NoArgsConstructor
public class MonthInfo extends BaseEntity {

    public MonthInfo(MonthInfo info) {
        this.year = info.getYear();
        this.month = info.getMonth();
        this.hoursSum = info.getHoursSum();
    }

    public MonthInfo(Integer year, Integer month, Integer hoursSum) {
        this.year = year;
        this.month = month;
        this.hoursSum = hoursSum;
    }

    @NotNull
    @Column(name = "year")
    private Integer year;

    @NotNull
    @Column(name = "month")
    private Integer month;

    @Column(name = "locked", nullable = false, columnDefinition = "boolean default true")
    private boolean locked = false;

    @Column(name = "hours_sum")
    private Integer hoursSum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
}
