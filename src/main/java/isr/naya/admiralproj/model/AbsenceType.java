package isr.naya.admiralproj.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AbsenceType {

    ILLNESS("מחלה"),
    HOLIDAY("חג"),
    VACATION("חופשה"),
    ARMY("מלוים");

    private String translation;
}