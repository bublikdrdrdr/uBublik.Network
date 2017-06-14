package ubublik.network.models.converters;

import ubublik.network.models.Gender;

import javax.persistence.AttributeConverter;


public class GenderConverter implements AttributeConverter<Gender, Character> {
    @Override
    public Character convertToDatabaseColumn(Gender from) {
        if (from == null) return null;
        else if (from.equals(Gender.MALE)) return new Character('M');
        else if (from.equals(Gender.FEMALE)) return new Character('F');
        else
            throw new IllegalArgumentException("Unknown gender value");
    }

    @Override
    public Gender convertToEntityAttribute(Character to) {
        if (to==null) return null; else
        if (to.equals('M')) return Gender.MALE; else
        if (to.equals('F')) return Gender.FEMALE; else
        throw new IllegalArgumentException("Invalid gender character");
    }
}