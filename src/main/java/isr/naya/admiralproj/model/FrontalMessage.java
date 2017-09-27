package isr.naya.admiralproj.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table()
@NoArgsConstructor
@AllArgsConstructor
public class FrontalMessage extends BaseEntity {

    private String color;
    private String header;
    private String content;
    private Long date;
}
