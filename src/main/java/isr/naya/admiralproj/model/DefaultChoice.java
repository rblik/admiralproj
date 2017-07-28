package isr.naya.admiralproj.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "default_choices")
@Builder
public class DefaultChoice implements Serializable{

    @Id
    private Integer id;

    @OneToOne
    @JoinColumn(name = "agreement_id", referencedColumnName = "id")
    private WorkAgreement agreement;

    @Column(name = "start", columnDefinition = "time")
    private LocalTime start;

    @Column(name = "finish", columnDefinition = "time")
    private LocalTime finish;

}
