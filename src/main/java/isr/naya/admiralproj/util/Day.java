package isr.naya.admiralproj.util;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Day {

    SUNDAY("א", 1),
    MONDAY("ב", 2),
    TUESDAY("ג", 3),
    WEDNESDAY("ד", 4),
    THURSDAY("ה", 5),
    FRIDAY("ו", 6),
    SATURDAY("ש", 7);

    private String symbol;
    private int index;
}
