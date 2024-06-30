package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.converter;

import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.ProcessStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter
public class ProcessStatusConverter implements AttributeConverter<ProcessStatus, String> {
    @Override
    public String convertToDatabaseColumn(ProcessStatus attribute) {
        return Objects.requireNonNullElse(attribute, ProcessStatus.PENDING).getValue();
    }

    @Override
    public ProcessStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return ProcessStatus.PENDING;
        }
        return ProcessStatus.of(dbData);
    }
}
