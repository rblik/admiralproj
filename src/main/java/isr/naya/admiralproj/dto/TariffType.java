package isr.naya.admiralproj.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TariffType {

    HOUR("לשעה"),
    DAY("ליום"),
    MONTH("לחודש"),
    FIX("פיקס");

    private String name;
}