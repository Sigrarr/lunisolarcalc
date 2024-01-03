package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.phenomena.local.GeoCoords.LatitudeDirection.*;
import static com.github.sigrarr.lunisolarcalc.phenomena.local.GeoCoords.LongitudeDirection.*;
import static com.github.sigrarr.lunisolarcalc.util.TestUtils.*;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class GeoCoordsTest {

    @Test
    public void shouldConstructAndConvert() {
        assertCorrectStateAndGettersOutput(GeoCoords.ofDegreesWithDirections(45, N, 90, E),
            Math.toRadians(45), Math.toRadians(-90));

        assertCorrectStateAndGettersOutput(GeoCoords.ofAnglesWithDirections(0.125 * Math.PI, S, 0.4 * Math.PI, W),
            -0.125 * Math.PI, 0.4 * Math.PI);

        assertCorrectStateAndGettersOutput(GeoCoords.ofConventional(6.6 * Math.PI, -6.6 * Math.PI),
            0.4 * Math.PI, 0.6 * Math.PI);

        assertCorrectStateAndGettersOutput(GeoCoords.ofConventionalDegrees(-35.0, +127.5),
            Math.toRadians(-35), Math.toRadians(-127.5));

        assertCorrectStateAndGettersOutput(GeoCoords.ofPlanetographic(6.6 * Math.PI, -6.6 * Math.PI),
            0.4 * Math.PI, -0.6 * Math.PI);
    }

    @Test
    public void shouldEquate() {
        double unit = GeoCoords.EQUIV_UNIT_RADIANS;
        GeoCoords a = new GeoCoords(Math.PI / 8, Math.PI / 2);
        assertEquivalence(a, new GeoCoords(Math.PI / 8, Math.PI / 2));
        assertNonEquivalence(a, new GeoCoords(Math.PI / 8 + unit, Math.PI / 2));
        assertNonEquivalence(a, new GeoCoords(Math.PI / 8 - unit, Math.PI / 2));
        assertNonEquivalence(a, new GeoCoords(Math.PI / 8, Math.PI / 2 + unit));
        assertNonEquivalence(a, new GeoCoords(Math.PI / 8, Math.PI / 2 - unit));

        GeoCoords pp = new GeoCoords(0, 0);
        assertEquivalence(pp, new GeoCoords(-unit/2 + Calcs.EPSILON_MIN, unit/2 - Calcs.EPSILON_MIN));
        assertEquivalence(pp, new GeoCoords(unit/2 - Calcs.EPSILON_MIN, -unit/2 + Calcs.EPSILON_MIN));
        assertNonEquivalence(pp, new GeoCoords(0, unit/2 + Calcs.EPSILON));
        assertNonEquivalence(pp, new GeoCoords(-unit/2 - Calcs.EPSILON, 0));
    }

    private void assertCorrectStateAndGettersOutput(GeoCoords geoCoords, double latitude, double planetographicLongitude) {
        assertEquals(latitude, geoCoords.latitude);
        assertEquals(planetographicLongitude, geoCoords.longitude);
        assertEquals(latitude, geoCoords.getLatitude());
        assertEquals(planetographicLongitude, geoCoords.getPlanetographicLongitude());
        assertEquals(-planetographicLongitude, geoCoords.getConventionalLongitude());
    }
}
