package isr.naya.admiralproj.model;

import isr.naya.admiralproj.dto.Currency;
import isr.naya.admiralproj.dto.TariffType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tariffs")
public class Tariff extends BaseEntity {

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TariffType type;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "amount")
    private Integer amount;
}