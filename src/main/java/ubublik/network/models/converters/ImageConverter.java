package ubublik.network.models.converters;

import javax.persistence.AttributeConverter;
import java.util.Base64;

/**
 * Created by Bublik on 16-Jun-17.
 */
public class ImageConverter implements AttributeConverter<String, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(String attribute) {
        if (attribute==null) return null;
        return Base64.getDecoder().decode(attribute);
    }

    @Override
    public String convertToEntityAttribute(byte[] dbData) {
        if (dbData==null) return null;
        return Base64.getEncoder().encodeToString(dbData);
    }
}
