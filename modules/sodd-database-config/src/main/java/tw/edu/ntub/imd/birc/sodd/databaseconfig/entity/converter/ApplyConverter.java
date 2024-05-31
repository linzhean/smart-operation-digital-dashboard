package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter;

import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Apply;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class ApplyConverter implements AttributeConverter<Apply, String> {
    @Override
    public String convertToDatabaseColumn(Apply attribute) {
        return Objects.requireNonNullElse(attribute, Apply.CLOSED).getValue();
    }

    @Override
    public Apply convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return Apply.CLOSED;
        }
        return Apply.of(dbData);
    }
}
