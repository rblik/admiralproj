package isr.naya.admiralproj.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.YearMonth;

@Converter
public class YearMonthConverter implements AttributeConverter<YearMonth, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(YearMonth yearMonth) {
        return Timestamp.valueOf(yearMonth.atDay(1).atStartOfDay());
    }

    @Override
    public YearMonth convertToEntityAttribute(Timestamp timestamp) {
        return YearMonth.from(timestamp.toLocalDateTime());
    }
}
