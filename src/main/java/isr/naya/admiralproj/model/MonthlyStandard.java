package isr.naya.admiralproj.model;

import isr.naya.admiralproj.util.YearMonthConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.YearMonth;

@Data
@Entity
@Table(name = "monthly_standards")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyStandard implements Serializable{

    @Id
    @Convert(converter = YearMonthConverter.class)
    @Column(name = "year_month")
    private YearMonth yearMonth;

    @Column(name = "hours_sum")
    private Double hoursSum;
}
