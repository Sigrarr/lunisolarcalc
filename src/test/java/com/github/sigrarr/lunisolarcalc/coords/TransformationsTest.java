package com.github.sigrarr.lunisolarcalc.coords;

import static com.github.sigrarr.lunisolarcalc.testing.TestUtils.decimalAutoDelta;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class TransformationsTest {

    // Meeus 1998: Example 13.a, p. 95
    static class ExampleA {
        static double latitude = Math.toRadians(6.684170);
        static double longitude = Math.toRadians(113.215630);
        static double rightAscension = Math.toRadians(360.0 * Calcs.Time.timeToDays(7, 45, 18.946));
        static double declination = Math.toRadians(Calcs.Angle.toSingleDegreesValue(28, 1, 34.26));
        static double eclipticObliquity = Math.toRadians(23.4392911);
    }

    // Meeus 1998: Example 13.b, p. 95
    static class ExampleB {
        static double geoLatitude = Calcs.Angle.toSingleDegreesValue(38, 55, 17.0);
        static double geoLongitude = 360.0 * Calcs.Time.timeToDays(5, 8, 15.7);
        static double rightAscension = 360.0 * Calcs.Time.timeToDays(23, 9, 16.641);
        static double declination = Calcs.Angle.toSingleDegreesValue(-6, 43, 11.61);
        static double siderealTime0 = 360.0 * Calcs.Time.timeToDays(8, 34, 56.853);
        static double localHourAngle = 64.352133;
        static double hourAngle0 = localHourAngle + geoLongitude;
        static double altitude = 15.1249;
        static double azimuth = 68.0337;
    }

    @Test
    public void shouldCalculateEclipticalLongitude() {
        double actualLambda = Transformations.equatorialToLongitude(ExampleA.rightAscension, ExampleA.declination, ExampleA.eclipticObliquity);
        assertEquals(ExampleA.longitude, actualLambda, Math.toRadians(0.01 * Calcs.ARCSECOND_TO_DEGREE));
    }

    @Test
    public void shouldCalculateEclipticalLatitude() {
        double actualBeta = Transformations.equatorialToLatitude(ExampleA.declination, ExampleA.rightAscension, ExampleA.eclipticObliquity);
        assertEquals(ExampleA.latitude, actualBeta, Math.toRadians(0.001 * Calcs.ARCSECOND_TO_DEGREE));
    }

    @Test
    public void shouldCalculateRightAscension() {
        double actualAlpha = Transformations.eclipticalToRightAscension(ExampleA.longitude, ExampleA.latitude, ExampleA.eclipticObliquity);
        assertEquals(ExampleA.rightAscension, actualAlpha, Math.toRadians(0.01 * Calcs.ARCSECOND_TO_DEGREE));
    }

    @Test
    public void shouldCalculateDeclination() {
        double actualDelta = Transformations.eclipticalToDeclination(ExampleA.latitude, ExampleA.longitude, ExampleA.eclipticObliquity);
        assertEquals(ExampleA.declination, actualDelta, Math.toRadians(0.001 * Calcs.ARCSECOND_TO_DEGREE));
    }

    @Test
    public void shouldCalculateHourAngle() {
        double actualH0 = Transformations.calculateHourAngle(ExampleB.siderealTime0, ExampleB.rightAscension, 360.0);
        double actualLHa = Transformations.calculateLocalHourAngle(ExampleB.siderealTime0, ExampleB.geoLongitude, ExampleB.rightAscension, 360.0);
        double actualLHb = Transformations.hourAngle0ToLocal(ExampleB.hourAngle0, ExampleB.geoLongitude, 360.0);
        assertEquals(ExampleB.hourAngle0, actualH0, 0.01 * Calcs.ARCSECOND_TO_DEGREE);
        assertEquals(ExampleB.localHourAngle, actualLHa, 0.01 * Calcs.ARCSECOND_TO_DEGREE);
        assertEquals(ExampleB.localHourAngle, actualLHb, 0.01 * Calcs.ARCSECOND_TO_DEGREE);
    }

    @Test
    public void shouldCalculateAltitude() {
        double actualAltitude = Transformations.calculateAltitude(
            Math.toRadians(ExampleB.declination),
            Math.toRadians(ExampleB.localHourAngle),
            Math.toRadians(ExampleB.geoLatitude)
        );
        assertEquals(ExampleB.altitude, Math.toDegrees(actualAltitude), decimalAutoDelta(0.0001));
    }

    @Test
    public void shouldCalculateAzimuth() {
        double actualAzimuth = Transformations.calculateAzimuth(
            Math.toRadians(ExampleB.localHourAngle),
            Math.toRadians(ExampleB.declination),
            Math.toRadians(ExampleB.geoLatitude)
        );
        assertEquals(ExampleB.azimuth, Math.toDegrees(actualAzimuth), decimalAutoDelta(0.0001));
    }
}
