package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter;

import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.AIGenType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class AIGenTypeConverter implements AttributeConverter<AIGenType, String> {
    @Override
    public String convertToDatabaseColumn(AIGenType attribute) {
        return Objects.requireNonNullElse(attribute, AIGenType.USER).getValue();
    }

    @Override
    public AIGenType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return AIGenType.USER;
        }
        return AIGenType.of(dbData);
    }
}
