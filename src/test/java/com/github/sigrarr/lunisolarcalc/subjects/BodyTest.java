package com.github.sigrarr.lunisolarcalc.subjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.coords.Key;

public class BodyTest {

    @Test
    public void shouldBeWellDefined() throws IllegalArgumentException, IllegalAccessException{
        List<Field> coordFields = Arrays.stream(Body.class.getDeclaredFields())
            .filter(f -> Key.QuantityIndetifier.class.isAssignableFrom(f.getType()))
            .peek(f -> assumeTrue(f.getName().endsWith("Coord")))
            .collect(Collectors.toList());

        for (Body body : Body.values()) {
            assertEquals(body.name(), body.getTitle().toUpperCase());

            for (Field coordField : coordFields) {
                String fieldName = coordField.getName();
                String reducedFieldName = fieldName.substring(0, fieldName.length() - 5).toUpperCase();

                Key.QuantityIndetifier coord = (Key.QuantityIndetifier) coordField.get(body);
                String reducedCoordName = coord.name().replace("_", "").toUpperCase();

                assertEquals(reducedCoordName, body.name() + reducedFieldName);
            }
        }
    }
}
