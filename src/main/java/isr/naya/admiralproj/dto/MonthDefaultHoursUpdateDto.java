package isr.naya.admiralproj.dto;

import isr.naya.admiralproj.model.MonthInfo;
import lombok.Value;

import java.util.List;

/**
 * Created by jonathan on 15/07/17.
 */
@Value
public class MonthDefaultHoursUpdateDto {
    private MonthInfo monthInfo;
    private List<Integer> employeeIds;
}
