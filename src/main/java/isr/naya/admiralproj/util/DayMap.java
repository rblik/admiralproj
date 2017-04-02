package isr.naya.admiralproj.util;


import isr.naya.admiralproj.exception.NotFoundException;

import java.util.Arrays;

public class DayMap {

    public static String getDay(int index) throws NotFoundException {
        return Arrays.stream(Day.values()).filter(d -> d.getIndex() == index).findFirst().map(Day::getSymbol).orElse("N/A");
    }
}
