package ch.bader.budget.server.converter;

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.filter.StandardAnnotationCustomizableTypeExcludeFilter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class DataMongoTypeExcludeFilterWithConverter extends StandardAnnotationCustomizableTypeExcludeFilter<DataMongoTest> {
    private static final Set<Class<?>> DEFAULT_INCLUDES;

    static {
        Set<Class<?>> includes = new LinkedHashSet<>();
        includes.add(Converter.class);
        includes.add(GenericConverter.class);
        includes.add(YearMonthToStringConverter.class);
        includes.add(StringToYearMonthConverter.class);
        try {
            includes.add(Module.class);
        } catch (Throwable ex) {
            // ignore
        }
        DEFAULT_INCLUDES = unmodifiableSet(includes);
    }

    protected DataMongoTypeExcludeFilterWithConverter(Class<?> testClass) {
        super(testClass);
    }

    @Override
    protected Set<Class<?>> getDefaultIncludes() {
        return DEFAULT_INCLUDES;
    }
}
