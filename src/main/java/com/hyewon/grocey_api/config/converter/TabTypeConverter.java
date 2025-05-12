package com.hyewon.grocey_api.config.converter;

import com.hyewon.grocey_api.domain.product.TabType;
import com.hyewon.grocey_api.global.exception.InvalidEnumValueException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TabTypeConverter implements Converter<String, TabType> {
    @Override
    public TabType convert(String source) {
        if (source == null || source.isBlank()) {
            return TabType.NEW; // default value
        }

        try {
            return TabType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumValueException(source, "TabType");
        }
    }
}
