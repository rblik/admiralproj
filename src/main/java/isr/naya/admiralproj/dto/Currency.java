package isr.naya.admiralproj.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Currency {

    DOLLAR("דולר"),
    SHEKEL("ש\\\"ח"),
    EURO("יורו");

    private String name;
}
