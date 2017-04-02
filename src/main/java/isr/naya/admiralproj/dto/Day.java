package isr.naya.admiralproj.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Day {

    SUNDAY("א", 7),
    MONDAY("ב", 1),
    TUESDAY("ג", 2),
    WEDNESDAY("ד", 3),
    THURSDAY("ה", 4),
    FRIDAY("ו", 5),
    SATURDAY("ש", 6);

    private String symbol;
    private int index;
}
