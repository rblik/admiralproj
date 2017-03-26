package isr.naya.admiralproj;

import isr.naya.admiralproj.model.AbsenceType;
import isr.naya.admiralproj.model.WorkUnit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TestData {
    public static final WorkUnit WORK_UNIT_1 = WorkUnit.builder().
            start(LocalDateTime.of(LocalDate.of(2016, 1, 1), LocalTime.of(12, 0))).
            finish(LocalDateTime.of(LocalDate.of(2016, 1, 1), LocalTime.of(15, 0))).build();
    public static final WorkUnit HOLIDAY = WorkUnit.builder().
            start(LocalDateTime.of(LocalDate.of(2016, 1, 3), LocalTime.now())).
            finish(LocalDateTime.of(LocalDate.of(2016, 1, 3), LocalTime.now())).
            absenceType(AbsenceType.HOLIDAY).build();
}
