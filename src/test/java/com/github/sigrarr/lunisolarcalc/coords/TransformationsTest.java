package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class TransformationsTest {

    @Test
    public void shouldCalculateLambda() {
        // Meeus 1998: Example 13.a, p. 95
        double refAlpha = Math.toRadians(360.0 * Calcs.Time.timeToDays(7, 45, 18.946));
        double refDelta = Math.toRadians(Calcs.Angle.toSingleDegreesValue(28, 1, 34.26));
        double refEpsilon = Math.toRadians(23.4392911);
        double actualLambda = Transformations.equatorialToLongitude(refAlpha, refDelta, refEpsilon);
        double expectedRefLambdaDegrees = 113.215630;
        assertEquals(expectedRefLambdaDegrees, Math.toDegrees(actualLambda), 0.01 * Calcs.ARCSECOND_TO_DEGREE);
    }

    @Test
    public void shouldCalculateBeta() {
        // Meeus 1998: Example 13.a, p. 95
        double refDelta = Math.toRadians(Calcs.Angle.toSingleDegreesValue(28, 1, 34.26));
        double refAlpha = Math.toRadians(360.0 * Calcs.Time.timeToDays(7, 45, 18.946));
        double refEpsilon = Math.toRadians(23.4392911);
        double actualBeta = Transformations.equatorialToLatitude(refDelta, refAlpha, refEpsilon);
        double expectedRefBetaDegrees = 6.684170;
        assertEquals(expectedRefBetaDegrees, Math.toDegrees(actualBeta), 0.001 * Calcs.ARCSECOND_TO_DEGREE);
    }

    @Test
    public void shouldCalculateAlpha() {
        // Meeus 1998: Example 13.a, p. 95
        double refLambda = Math.toRadians(113.215630);
        double refBeta = Math.toRadians(6.684170);
        double refEpsilon = Math.toRadians(23.4392911);
        double actualAlpha = Transformations.eclipticalToRightAscension(refLambda, refBeta, refEpsilon);
        double expectedRefAlphaDegrees = 360.0 * Calcs.Time.timeToDays(7, 45, 18.946);
        assertEquals(expectedRefAlphaDegrees, Math.toDegrees(actualAlpha), 0.01 * Calcs.ARCSECOND_TO_DEGREE);
    }

    @Test
    public void shouldCalculateDelta() {
        // Meeus 1998: Example 13.a, p. 95
        double refBeta = Math.toRadians(6.684170);
        double refLambda = Math.toRadians(113.215630);
        double refEpsilon = Math.toRadians(23.4392911);
        double actualDelta = Transformations.eclipticalToDeclination(refBeta, refLambda, refEpsilon);
        double expectedRefDeltaDegrees = Calcs.Angle.toSingleDegreesValue(28, 1, 34.26);
        assertEquals(expectedRefDeltaDegrees, Math.toDegrees(actualDelta), 0.001 * Calcs.ARCSECOND_TO_DEGREE);
    }

    @Test
    public void shouldCalculateH() {
        // Meeus 1998: Example 13.b, p. 95
        double refLongitude = 360.0 * Calcs.Time.timeToDays(5, 8, 15.7);
        double refThetaZero = 360.0 * Calcs.Time.timeToDays(8, 34, 56.853);
        double refImpliedTheta = refThetaZero - refLongitude;
        double refAlpha = 360.0 * Calcs.Time.timeToDays(23, 9, 16.641);
        double actualH = Transformations.calculateHourAngle(refImpliedTheta, refAlpha, 360.0);
        double expectedRefH = 64.352133;
        assertEquals(expectedRefH, actualH, 0.01 * Calcs.ARCSECOND_TO_DEGREE);
    }
}
