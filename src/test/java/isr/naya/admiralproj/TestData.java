package isr.naya.admiralproj;

import isr.naya.admiralproj.model.WorkUnit;

import java.time.LocalTime;

public class TestData {
    public static final WorkUnit WORK_UNIT_1 = WorkUnit.builder().
            start(LocalTime.of(12, 0)).
            finish(LocalTime.of(15, 0)).build();
    public static final WorkUnit HOLIDAY = WorkUnit.builder().
            start(LocalTime.now()).
            finish(LocalTime.now()).build();
}
