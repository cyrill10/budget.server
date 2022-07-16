package ch.bader.budget.server.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

import java.time.YearMonth;

@ReadingConverter
public class StringToYearMonthConverter implements Converter<String, YearMonth> {

    @Override
    public YearMonth convert(@NonNull String source) {
        return YearMonth.parse(source);
    }
}