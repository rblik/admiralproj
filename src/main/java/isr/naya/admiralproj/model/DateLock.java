package isr.naya.admiralproj.model;

import isr.naya.admiralproj.util.YearMonthConverter;
import lombok.*;

import javax.persistence.*;
import java.time.YearMonth;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "datelocks")
public class DateLock extends BaseEntity{

    @Convert(converter = YearMonthConverter.class)
    @Column(name = "year_month")
    private YearMonth yearMonth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
}
