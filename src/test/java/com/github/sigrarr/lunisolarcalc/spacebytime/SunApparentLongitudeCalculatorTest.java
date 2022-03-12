package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;
import static com.github.sigrarr.lunisolarcalc.util.MeanMotionApproximate.TROPICAL_YEAR;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class SunApparentLongitudeCalculatorTest {

    private static final SunGeometricLongitudeCalculator GEOMETRIC_LONGITUDE_CALCULATOR = new SunGeometricLongitudeCalculator();
    private static final EarthNutuationCalculator DELTA_PSI_CALCULATOR = new EarthNutuationInLongitudeCalculator();
    private static final AberrationEarthSunCalculator ABERRATION_CALCULATOR = new AberrationEarthSunCalculator();

    private SunApparentLongitudeCalculator calculator = new SunApparentLongitudeCalculator();

    @Test
    public void shouldCalculateLambda() {
        // Meeus 1998, Example 25.a-b, pp. 165, 169
        double tau = Timeline.julianDayToMillenialTau(2448908.5);
        double cT = Timeline.julianDayToCenturialT(2448908.5);

        // Meeus 1998, Example 25.b, p. 169.
        double geometricLongitude = Math.toRadians(199.907347);
        double deltaPsi = Math.toRadians(Calcs.arcsecondsToDegrees(15.908));
        double aberration = Math.toRadians(Calcs.arcsecondsToDegrees(-20.539));
        double actualLambda = calculator.calculateApparentLongitude(geometricLongitude, deltaPsi, aberration);
        double exampleLambdaDegrees = Calcs.toSingleDegreesValue(199, 54, 21.818);
        assertEquals(exampleLambdaDegrees, Math.toDegrees(actualLambda), TROPICAL_YEAR.degreesPerTimeMiliseconds(5));

        // Meeus 1998, Example 25.a, pp. 165
        double trueVSOP87Radius = 0.99760853;
        double trueVSOP87GeometricLongitude = Math.toRadians(Calcs.toSingleDegreesValue(199, 54, 26.18));
        deltaPsi = DELTA_PSI_CALCULATOR.calculateNutuation(cT, new EarthNutuationElements(cT));
        aberration = ABERRATION_CALCULATOR.calculateAberration(tau, trueVSOP87Radius);
        actualLambda = calculator.calculateApparentLongitude(trueVSOP87GeometricLongitude, deltaPsi, aberration);
        double trueVSOP87LambdaDegrees = Calcs.toSingleDegreesValue(199, 54, 21.56);
        assertEquals(trueVSOP87LambdaDegrees, Math.toDegrees(actualLambda), TROPICAL_YEAR.degreesPerTimeMiliseconds(115));

        // Meeus 1998, Example 27.b, pp. 180-181
        geometricLongitude = GEOMETRIC_LONGITUDE_CALCULATOR.calculateGeometricLongitude(Math.toRadians(270.003272));
        deltaPsi = Math.toRadians(Calcs.arcsecondsToDegrees(-12.965));
        aberration = Math.toRadians(Calcs.arcsecondsToDegrees(-20.161));
        actualLambda = calculator.calculateApparentLongitude(geometricLongitude, deltaPsi, aberration);
        assertEquals(89.994045, Math.toDegrees(actualLambda), decimalAutoDelta(89.994045));
    }
}
