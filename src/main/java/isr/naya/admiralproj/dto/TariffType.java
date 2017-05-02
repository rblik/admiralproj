package isr.naya.admiralproj.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TariffType {

    HOUR("לפי שעה"),
    MONTH("לפי חודש"),
    FIX("פיקס");

    private String name;
}