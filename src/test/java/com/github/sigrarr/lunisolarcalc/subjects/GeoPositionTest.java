package com.github.sigrarr.lunisolarcalc.subjects;

import static com.github.sigrarr.lunisolarcalc.testing.TestUtils.*;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.testing.ExampleLocation;

public class GeoPositionTest {

    @Test
    public void shouldEquate() {
        assertEquivalence(GeoPosition.of(ExampleLocation.WROCLAW, 100), GeoPosition.of(ExampleLocation.WROCLAW, 100));
        assertNonEquivalence(GeoPosition.of(ExampleLocation.WROCLAW, 99), GeoPosition.of(ExampleLocation.WROCLAW, 100));
        assertNonEquivalence(
            GeoPosition.of(GeoCoords.ofConventional(0, 0), 19),
            GeoPosition.of(GeoCoords.ofConventional(0, 0 + GeoCoords.EQUIV_UNIT_RADIANS), 19)
        );
    }
}
