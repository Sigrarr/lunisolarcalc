package com.github.sigrarr.lunisolarcalc.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalcsTest {

    @Test
    public void shouldNormalizeLongitudinallyToSubRound() {
        assertEquals(0.0, Calcs.normalizeLongitudinally(0.0), Calcs.EPSILON_12);
        assertEquals(0.5 * Math.PI, Calcs.normalizeLongitudinally(0.5 * Math.PI), Calcs.EPSILON_12);
        assertEquals(0.1 * Math.PI, Calcs.normalizeLongitudinally( 12.1 * Math.PI), Calcs.EPSILON_12);
        assertEquals(1.9 * Math.PI, Calcs.normalizeLongitudinally(-12.1 * Math.PI), Calcs.EPSILON_12);
    }

    @Test
    public void shouldNormalizeLatitudinallyToSubHalfRounds() {
        assertEquals( 0.0, Calcs.normalizeLatitudinally(0.0), Calcs.EPSILON_12);
        assertEquals( 0.5 * Math.PI, Calcs.normalizeLatitudinally( 0.5 * Math.PI), Calcs.EPSILON_12);
        assertEquals(-0.5 * Math.PI, Calcs.normalizeLatitudinally( 1.5 * Math.PI), Calcs.EPSILON_12);
        assertEquals( 0.5 * Math.PI, Calcs.normalizeLatitudinally(-1.5 * Math.PI), Calcs.EPSILON_12);
        assertEquals(-0.25 * Math.PI, Calcs.normalizeLatitudinally(-0.25 * Math.PI), Calcs.EPSILON_12);
    }

    @Test
    public void shouldDecimalizeDegreesFromDegMinSec() {
        assertEquals( 199.0, Calcs.toSingleDegreesValue(199,  0, 0.0), Calcs.EPSILON_MIN);
        assertEquals( 199.5, Calcs.toSingleDegreesValue(199, 30, 0.0), Calcs.EPSILON_MIN);
        assertEquals( 199.008333333, Calcs.toSingleDegreesValue( 199,  0, 30.0 ), Calcs.autoDelta(0.000000001));
        assertEquals( 199.508333333, Calcs.toSingleDegreesValue( 199, 30, 30.0 ), Calcs.autoDelta(0.000000001));
        assertEquals(-199.737830556, Calcs.toSingleDegreesValue(-199, 44, 16.19), Calcs.autoDelta(0.000000001));
    }

    @Test
    public void shouldAutoDeltaBeDefaultEpsilonForZero() {
        assertEquals(Calcs.EPSILON, Calcs.autoDelta(0.0), Calcs.EPSILON_MIN);
    }

    @Test
    public void shouldAutoDeltaBeHalfOfLastDigitUnitForNonZero() {
        assertEquals(0.05,              Calcs.autoDelta(  0.1),              Calcs.EPSILON_MIN);
        assertEquals(0.0000005,         Calcs.autoDelta(-12.123456),         Calcs.EPSILON_MIN);
        assertEquals(0.0000000000005,   Calcs.autoDelta(400.123456789012),   Calcs.EPSILON_MIN);
    }

    @Test
    public void shouldAutoDeltaBeNotLesserThanMinEps() {
        assertEquals(Calcs.EPSILON_MIN, Calcs.autoDelta(0.1234567890123456789012345678901), 0.0000000000000000000000000000001);
    }
}
