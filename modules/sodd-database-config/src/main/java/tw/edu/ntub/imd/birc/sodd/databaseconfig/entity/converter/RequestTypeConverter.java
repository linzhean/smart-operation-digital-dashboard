package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter;

import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.RequestType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class RequestTypeConverter implements AttributeConverter<RequestType, String> {

    @Override
    public String convertToDatabaseColumn(RequestType attribute) {
        return Objects.requireNonNullElse(attribute, RequestType.GET).getValue();
    }

    @Override
    public RequestType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return RequestType.GET;
        }
        return RequestType.of(dbData);
    }
}
