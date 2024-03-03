package edu.ucsd.cse110.successorator.util;

import androidx.room.TypeConverter;

import java.time.ZonedDateTime;
import java.util.Objects;

/*
 * Class to convert a ZonedDateTime object to a String
 * representation and back; to be invoked automatically when
 * storing items in the Item entity, converting ZonedDateTime
 * to a store-able form for persistence.
 */
public class ZonedDateTimeConverter {

    // Convert from String representation to ZonedDateTime
    @TypeConverter
    public static ZonedDateTime fromString(String value) {
        if (value == null) { return null; }

        return ZonedDateTime.parse(value);
    }

    // Convert from ZonedDateTime to String representation
    @TypeConverter
    public static String dateToString(ZonedDateTime dateTime) {
        return Objects.isNull(dateTime) ? null: dateTime.toString();
    }
}