package isr.naya.admiralproj.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * Created by Blik on 03/20/2017.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
public class WorkRecord {
    private LocalDateTime start;
}
