package isr.naya.admiralproj.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tariffs")
@Data
@EqualsAndHashCode(callSuper = false, of = {"tariffType", "tariffAmount"})
public class Tariff extends BaseEntity {

    @Column(name = "tariff_type")
    @Enumerated(EnumType.STRING)
    private TariffType tariffType = TariffType.HOURLY;

    @NotNull
    @Column(name = "tariff_amount", nullable = false)
    private Integer tariffAmount;
}
