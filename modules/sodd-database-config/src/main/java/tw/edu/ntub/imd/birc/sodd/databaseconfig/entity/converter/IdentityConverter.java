package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter;

import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class IdentityConverter implements AttributeConverter<Identity, String> {
    @Override
    public String convertToDatabaseColumn(Identity attribute) {
        return Objects.requireNonNullElse(attribute, Identity.NO_PERMISSION).getValue();
    }

    @Override
    public Identity convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return Identity.NO_PERMISSION;
        }
        return Identity.of(dbData);
    }
}
