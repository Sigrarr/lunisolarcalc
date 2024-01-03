package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class TransformationsTest {

    @Test
    public void shouldCalculateEclipticalLongitude() {
        // Meeus 1998: Example 13.a, p. 95
        double refAlpha = Math.toRadians(360.0 * Calcs.Time.timeToDays(7, 45, 18.946));
        double refDelta = Math.toRadians(Calcs.Angle.toSingleDegreesValue(28, 1, 34.26));
        double refEpsilon = Math.toRadians(23.4392911);
        double actualLambda = Transformations.equatorialToLongitude(refAlpha, refDelta, refEpsilon);
        double expectedRefLambdaDegrees = 113.215630;
        assertEquals(expectedRefLambdaDegrees, Math.toDegrees(actualLambda), 0.01 * Calcs.ARCSECOND_TO_DEGREE);
    }

    @Test
    public void shouldCalculateEclipticalLatitude() {
        // Meeus 1998: Example 13.a, p. 95
        double refDelta = Math.toRadians(Calcs.Angle.toSingleDegreesValue(28, 1, 34.26));
        double refAlpha = Math.toRadians(360.0 * Calcs.Time.timeToDays(7, 45, 18.946));
        double refEpsilon = Math.toRadians(23.4392911);
        double actualBeta = Transformations.equatorialToLatitude(refDelta, refAlpha, refEpsilon);
        double expectedRefBetaDegrees = 6.684170;
        assertEquals(expectedRefBetaDegrees, Math.toDegrees(actualBeta), 0.001 * Calcs.ARCSECOND_TO_DEGREE);
    }

    @Test
    public void shouldCalculateRightAscension() {
        // Meeus 1998: Example 13.a, p. 95
        double refLambda = Math.toRadians(113.215630);
        double refBeta = Math.toRadians(6.684170);
        double refEpsilon = Math.toRadians(23.4392911);
        double actualAlpha = Transformations.eclipticalToRightAscension(refLambda, refBeta, refEpsilon);
        double expectedRefAlphaDegrees = 360.0 * Calcs.Time.timeToDays(7, 45, 18.946);
        assertEquals(expectedRefAlphaDegrees, Math.toDegrees(actualAlpha), 0.01 * Calcs.ARCSECOND_TO_DEGREE);
    }

    @Test
    public void shouldCalculateDeclination() {
        // Meeus 1998: Example 13.a, p. 95
        double refBeta = Math.toRadians(6.684170);
        double refLambda = Math.toRadians(113.215630);
        double refEpsilon = Math.toRadians(23.4392911);
        double actualDelta = Transformations.eclipticalToDeclination(refBeta, refLambda, refEpsilon);
        double expectedRefDeltaDegrees = Calcs.Angle.toSingleDegreesValue(28, 1, 34.26);
        assertEquals(expectedRefDeltaDegrees, Math.toDegrees(actualDelta), 0.001 * Calcs.ARCSECOND_TO_DEGREE);
    }

    @Test
    public void shouldCalculateGreenwichAndLocalHourAngle() {
        // Meeus 1998: Example 13.b, p. 95
        double observerLongitude = 360.0 * Calcs.Time.timeToDays(5, 8, 15.7);
        double refThetaZero = 360.0 * Calcs.Time.timeToDays(8, 34, 56.853);
        double refAlpha = 360.0 * Calcs.Time.timeToDays(23, 9, 16.641);
        double expectedRefH = 64.352133;
        double expectedRefImpliedH0 = expectedRefH + observerLongitude;
        double actualH0 = Transformations.calculateHourAngle(refThetaZero, refAlpha, 360.0);
        double actualH = Transformations.calculateLocalHourAngle(expectedRefImpliedH0, observerLongitude, 360.0);
        assertEquals(expectedRefImpliedH0, actualH0, 0.01 * Calcs.ARCSECOND_TO_DEGREE);
        assertEquals(expectedRefH, actualH, 0.01 * Calcs.ARCSECOND_TO_DEGREE);
    }

    @Test
    public void shouldCalculateAltitude() {
        // Meeus 1998: Example 13.b, p. 95
        double refDelta = Math.toRadians(Calcs.Angle.toSingleDegreesValue(-6, 43, 11.61));
        double refHourAngle = Math.toRadians(64.352133);
        double observerLatitude = Math.toRadians(Calcs.Angle.toSingleDegreesValue(38, 55, 17.0));
        double actualAltitude = Transformations.calculateAltitude(refDelta, refHourAngle, observerLatitude);
        double expectedAltitudeDegrees = 15.1249;
        assertEquals(expectedAltitudeDegrees, Math.toDegrees(actualAltitude), decimalAutoDelta(0.0001));
    }

    @Test
    public void shouldCalculateAzimuth() {
        // Meeus 1998: Example 13.b, p. 95
        double refDelta = Math.toRadians(Calcs.Angle.toSingleDegreesValue(-6, 43, 11.61));
        double refHourAngle = Math.toRadians(64.352133);
        double observerLatitude = Math.toRadians(Calcs.Angle.toSingleDegreesValue(38, 55, 17.0));
        double actualAzimuth = Transformations.calculateAzimuth(refHourAngle, refDelta, observerLatitude);
        double expectedAzimuthDegrees = 68.0337;
        assertEquals(expectedAzimuthDegrees, Math.toDegrees(actualAzimuth), decimalAutoDelta(0.0001));
    }
}
