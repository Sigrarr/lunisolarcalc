package com.github.sigrarr.lunisolarcalc.util;

import static org.junit.jupiter.api.Assertions.*;
import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import org.junit.jupiter.api.Test;

public class CalcsTest {

    @Test
    public void shouldCompareDoublesWithDelta() {
        assertEquals( 0, Calcs.compare(0.0, 0.0, Calcs.EPSILON_12));
        assertEquals( 0, Calcs.compare(1.1, 1.2, 0.1 + Calcs.EPSILON_12));
        assertEquals( 1, Calcs.compare(1.2, 1.1, 0.1 - Calcs.EPSILON_12));
        assertEquals(-1, Calcs.compare(-1.06, -1.05, 0.01 - Calcs.EPSILON_12));
        assertEquals( 0, Calcs.compare( 121.23,  120.94, 0.29 + Calcs.EPSILON_12));
        assertEquals(-1, Calcs.compare(-121.23, -120.94, 0.29 - Calcs.EPSILON_12));
        assertEquals( 1, Calcs.compare( 121.23,  120.94, 0.29 - Calcs.EPSILON_12));
    }

    @Test
    public void shouldRoundToDelta() {
        assertEquals(0.001, Calcs.roundToDelta(0.001, 0.0), Calcs.EPSILON_MIN);
        assertEquals(0.5, Calcs.roundToDelta(0.75 - Calcs.EPSILON, 0.5), Calcs.EPSILON_MIN);
        assertEquals(1.0, Calcs.roundToDelta(0.75, 0.5), Calcs.EPSILON_MIN);
        assertEquals(-109.2, Calcs.roundToDelta(-109.15 - Calcs.EPSILON, 0.1), Calcs.EPSILON_MIN);
        assertEquals(11000, Calcs.roundToDelta(11409.23, 1000.0));
        assertEquals(2200, Calcs.roundToDelta(2150.333, 100.0));
    }

    @Test
    public void shouldCalculateAbsoluteModuloTwo() {
        assertEquals(0, Calcs.absMod2(0));
        assertEquals(0, Calcs.absMod2(2));
        assertEquals(0, Calcs.absMod2(-2));
        assertEquals(0, Calcs.absMod2(1278));
        assertEquals(0, Calcs.absMod2(-131910));
        assertEquals(1, Calcs.absMod2(1));
        assertEquals(1, Calcs.absMod2(-1));
        assertEquals(1, Calcs.absMod2(1277));
        assertEquals(1, Calcs.absMod2(-131903));
    }

    @Test
    public void shouldCalculateHaversine() {
        assertEquals(1.0 , Calcs.Angle.hav(-3 * Math.PI),   Calcs.EPSILON_12);
        assertEquals(0.25, Calcs.Angle.hav( Math.PI / 3.0), Calcs.EPSILON_12);
        assertEquals(0.25, Calcs.Angle.hav(-Math.PI / 3.0), Calcs.EPSILON_12);
        assertEquals(0.0 , Calcs.Angle.hav(0.0),    Calcs.EPSILON_12);
        assertEquals(0.0 , Calcs.Angle.hav( 2 * Math.PI),   Calcs.EPSILON_12);
    }

    @Test
    public void shouldNormalizeAngleToAbsoluteLongitude() {
        assertEquals(0.0, Calcs.Angle.toNormalLongitude(0.0), Calcs.EPSILON_12);
        assertEquals(0.5 * Math.PI, Calcs.Angle.toNormalLongitude(0.5 * Math.PI), Calcs.EPSILON_12);
        assertEquals(0.1 * Math.PI, Calcs.Angle.toNormalLongitude( 12.1 * Math.PI), Calcs.EPSILON_12);
        assertEquals(1.9 * Math.PI, Calcs.Angle.toNormalLongitude(-12.1 * Math.PI), Calcs.EPSILON_12);

        assertEquals(0.0, Calcs.Angle.toNormalLongitude(0.0, 1.0), Calcs.EPSILON_12);
        assertEquals(90.0, Calcs.Angle.toNormalLongitude(90.0, 360.0), Calcs.EPSILON_12);
        assertEquals(0.1 * 180.0, Calcs.Angle.toNormalLongitude( 12.1 * 180.0, 360.0), Calcs.EPSILON_12);
        assertEquals(0.9, Calcs.Angle.toNormalLongitude(-12.1, 1.0), Calcs.EPSILON_12);
    }

    @Test
    public void shouldNormalizeAngleToSignedLongitude() {
        assertEquals(0.0, Calcs.Angle.toNormalSignedLongitude(0.0), Calcs.EPSILON_12);
        assertEquals(0.5 * Math.PI, Calcs.Angle.toNormalSignedLongitude(0.5 * Math.PI), Calcs.EPSILON_12);
        assertEquals(-0.25 * Math.PI, Calcs.Angle.toNormalSignedLongitude(1.75 * Math.PI), Calcs.EPSILON_12);
        assertEquals(0.75 * Math.PI, Calcs.Angle.toNormalSignedLongitude(-3.25 * Math.PI), Calcs.EPSILON_12);
        assertEquals(Math.PI, Math.abs(Calcs.Angle.toNormalSignedLongitude(11 * Math.PI)), Calcs.EPSILON_12);

        assertEquals(0.0, Calcs.Angle.toNormalSignedLongitude(0.0, 1.0), Calcs.EPSILON_12);
        assertEquals(0.25, Calcs.Angle.toNormalSignedLongitude(0.25, 1.0), Calcs.EPSILON_12);
        assertEquals(-45, Calcs.Angle.toNormalSignedLongitude(315, 360), Calcs.EPSILON_12);
        assertEquals(135, Calcs.Angle.toNormalSignedLongitude(-585, 360), Calcs.EPSILON_12);
        assertEquals(0.5, Math.abs(Calcs.Angle.toNormalSignedLongitude(5.5, 1.0)), Calcs.EPSILON_12);
    }

    @Test
    public void shouldNormalizeAngleToLatitude() {
        double[][] inputAndLatitudePairs = {
            {0.0, 0.0},
            {0.5 * Math.PI, 0.5 * Math.PI}, {-0.5 * Math.PI, -0.5 * Math.PI},
            {1.0 * Math.PI, 0.0}, {-1.0 * Math.PI, 0.0},
            {1.5 * Math.PI, -0.5 * Math.PI}, {-1.5 * Math.PI, 0.5 * Math.PI},
            {2.5 * Math.PI, 0.5 * Math.PI}, {-2.5 * Math.PI, -0.5 * Math.PI},
            {0.25 * Math.PI, 0.25 * Math.PI}, {-0.25 * Math.PI, -0.25 * Math.PI},
            {0.75 * Math.PI, 0.25 * Math.PI}, {-0.75 * Math.PI, -0.25 * Math.PI},
            {1.25 * Math.PI, -0.25 * Math.PI}, {-1.25 * Math.PI, 0.25 * Math.PI},
            {3.3 * Math.PI, -0.3 * Math.PI}, {-5.6 * Math.PI, 0.4 * Math.PI}
        };
        for (double[] inputAndLatitude : inputAndLatitudePairs) {
            double input = inputAndLatitude[0];
            double expectedLatitude = inputAndLatitude[1];
            assertEquals(expectedLatitude, Calcs.Angle.toNormalLatitude(input), Calcs.EPSILON_12);
            assertEquals(Math.toDegrees(expectedLatitude), Calcs.Angle.toNormalLatitude(Math.toDegrees(input), 360), Calcs.EPSILON_12);
            assertEquals(expectedLatitude / Calcs.TURN, Calcs.Angle.toNormalLatitude(input / Calcs.TURN, 1.0), Calcs.EPSILON_12);
        }
    }

    @Test
    public void shouldDecimalizeAngle() {
        assertEquals( 199.0, Calcs.Angle.toSingleDegreesValue(199,  0, 0.0), Calcs.EPSILON_MIN);
        assertEquals( 0.5, Calcs.Angle.toSingleDegreesValue(0,  30, 0.0), Calcs.EPSILON_MIN);
        assertEquals(0.01, Calcs.Angle.toSingleDegreesValue(0, 0, 36), Calcs.EPSILON);
        assertEquals( 199.5, Calcs.Angle.toSingleDegreesValue(199, 30, 0.0), Calcs.EPSILON_MIN);
        assertEquals(-90.51, Calcs.Angle.toSingleDegreesValue(-90, 30, 36));
        assertEquals( 199.008333333, Calcs.Angle.toSingleDegreesValue( 199,  0, 30.0 ), decimalAutoDelta(0.000000001));
        assertEquals( 199.508333333, Calcs.Angle.toSingleDegreesValue( 199, 30, 30.0 ), decimalAutoDelta(0.000000001));
        assertEquals(-199.737830556, Calcs.Angle.toSingleDegreesValue(-199, 44, 16.19), decimalAutoDelta(0.000000001));

        assertEquals(1.0, Calcs.Angle.toSingleArcsecondsValue(0, 0, 1));
        assertEquals(60.0, Calcs.Angle.toSingleArcsecondsValue(0, 1, 0));
        assertEquals( 716400.0,  Calcs.Angle.toSingleArcsecondsValue( 199,  0, 0.0),   Calcs.EPSILON_MIN);
        assertEquals( 718200.0,  Calcs.Angle.toSingleArcsecondsValue( 199, 30, 0.0),   Calcs.EPSILON_MIN);
        assertEquals(-110451.0, Calcs.Angle.toSingleArcsecondsValue(-30, 40, 51));
        assertEquals( 716431.0,  Calcs.Angle.toSingleArcsecondsValue( 199,  0, 31.0 ), Calcs.EPSILON_MIN);
        assertEquals( 718232.0,  Calcs.Angle.toSingleArcsecondsValue( 199, 30, 32.0 ), Calcs.EPSILON_MIN);
        assertEquals(-719056.19, Calcs.Angle.toSingleArcsecondsValue(-199, 44, 16.19), Calcs.EPSILON_MIN);
    }

    @Test
    public void shouldConvertTimeBetweenDaysAndWholeSmallerUnits() {
        assertEquals(0.0,        Calcs.Time.timeToDays( 0,  0,  0), Calcs.EPSILON_12);
        assertEquals(0.5,        Calcs.Time.timeToDays(12,  0,  0), Calcs.EPSILON_12);
        assertEquals(0.50694444, Calcs.Time.timeToDays(12, 10,  0), decimalAutoDelta(0.00000001));
        assertEquals(0.50716435, Calcs.Time.timeToDays(12, 10, 19), decimalAutoDelta(0.00000001));

        assertEquals(0, Calcs.Time.dayFractionToWholeHours(0.0));
        assertEquals(12, Calcs.Time.dayFractionToWholeHours(1.5));
        assertEquals(11, Calcs.Time.dayFractionToWholeHours(30.499));
        assertEquals(5, Calcs.Time.dayFractionToWholeHours(100.2499));
        assertEquals(0, Calcs.Time.dayFractionToWholeMinutes(0.75));
        assertEquals(19, Calcs.Time.dayFractionToWholeMinutes(10000.75 + (19.0/60.0/24.0) + Calcs.EPSILON_12));
        assertEquals(18, Calcs.Time.dayFractionToWholeMinutes(10000.75 + (19.0/60.0/24.0) - Calcs.EPSILON_12));
        assertEquals(0, Calcs.Time.dayFractionToWholeSeconds(0.75));
        assertEquals(57, Calcs.Time.dayFractionToWholeSeconds(10000.75 + (57.0/60.0/60.0/24.0) + Calcs.EPSILON_12));
        assertEquals(56, Calcs.Time.dayFractionToWholeSeconds(10000.75 + (57.0/60.0/60.0/24.0) - Calcs.EPSILON_12));
        assertArrayEquals(new int[] {0, 0, 0}, Calcs.Time.dayFractionToWholeHMinS(0.0));
        assertArrayEquals(new int[] {12, 34, 0}, Calcs.Time.dayFractionToWholeHMinS(10000.5 + (34.0/60.0/24.0) + Calcs.EPSILON_12));
        assertArrayEquals(new int[] {12, 0, 49}, Calcs.Time.dayFractionToWholeHMinS(10000.5 + (50.0/60.0/60.0/24.0) - Calcs.EPSILON_12));
    }

    @Test
    public void shouldCalculateCircularMotionAngleFromTime() {
        assertEquals(0.0, Calcs.CircularMotion.degreesPerTimeSeconds(10000, 0));
        assertEquals(0.0, Calcs.CircularMotion.radiansPerTimeSeconds(20000, 0));
        assertEquals(0.0, Calcs.CircularMotion.degreesPerTimeMiliseconds(30000, 0));
        assertEquals(0.0, Calcs.CircularMotion.arcsecondsPerTimeMiliseconds(40000, 0));

        double length = 365.2421896698 * Calcs.DAY_SECONDS;
        double percent = length * 0.01;
        double deltaDeg = Calcs.Angle.arcsecondsToDegrees(0.1);
        double deltaRad = Math.toRadians(deltaDeg);
        assertEquals(0.01 * 360, Calcs.CircularMotion.degreesPerTimeSeconds(length, (int) Math.round(percent)), deltaDeg);
        assertEquals(0.01 * Calcs.TURN, Calcs.CircularMotion.radiansPerTimeSeconds(length, (int) Math.round(percent)), deltaRad);
        assertEquals(0.01 * 360, Calcs.CircularMotion.degreesPerTimeMiliseconds(length, (int) Math.round(1000.0 * percent)), deltaDeg);
        assertEquals(Calcs.Angle.toArcseconds(0.01 * 360), Calcs.CircularMotion.arcsecondsPerTimeMiliseconds(length, (int) Math.round(1000.0 * percent)), 0.001);
    }

    @Test
    public void shouldCalculateCircularMotionTimeFromAngle() {
        assertEquals(0.0, Calcs.CircularMotion.secondsPerRadians(1000000, 0.0));
        assertEquals(0.0, Calcs.CircularMotion.secondsPerDegrees(2000000, 0.0));

        double permilleOfRound = Calcs.TURN * 0.001;
        double permilleOfRoundDegrees = Math.toDegrees(permilleOfRound);
        double length = 123456789;
        assertEquals(length * 0.001, Calcs.CircularMotion.secondsPerRadians(length, permilleOfRound), decimalAutoDelta(0.001));
        assertEquals(length * 0.001, Calcs.CircularMotion.secondsPerDegrees(length, permilleOfRoundDegrees), decimalAutoDelta(0.001));
    }
}
