package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.decimalAutoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class SunApparentLongitudeCalculatorTest {

    private static final SunGeometricLongitudeCalculator GEOMETRIC_LONGITUDE_CALCULATOR = new SunGeometricLongitudeCalculator();
    private static final EarthNutuationCalculator DELTA_PSI_CALCULATOR = new EarthNutuationInLongitudeCalculator();
    private static final AberrationEarthSunCalculator ABERRATION_CALCULATOR = new AberrationEarthSunCalculator();

    private SunApparentLongitudeCalculator calculator = new SunApparentLongitudeCalculator();

    @Test
    public void shouldCalculateLambda() {
        // Meeus 1998, Example 25.a-b, pp. 165, 169
        TimelinePoint tx = new TimelinePoint(2448908.5);

        // Meeus 1998, Example 25.b, p. 169.
        double geometricLongitude = Math.toRadians(199.907347);
        double deltaPsi = Math.toRadians(Calcs.arcsecondsToDegrees(15.908));
        double aberration = Math.toRadians(Calcs.arcsecondsToDegrees(-20.539));
        double actualLambda = calculator.calculateApparentLongitude(geometricLongitude, deltaPsi, aberration);
        double exampleLambdaArcseconds = Calcs.toSingleArcsecondsValue(199, 54, 21.818);
        assertEquals(exampleLambdaArcseconds, Calcs.toArcseconds(Math.toDegrees(actualLambda)), decimalAutoDelta(0.001));

        // Meeus 1998, Example 25.a, pp. 165
        double trueVSOP87Radius = 0.99760853;
        double trueVSOP87GeometricLongitude = Math.toRadians(Calcs.toSingleDegreesValue(199, 54, 26.18));
        deltaPsi = DELTA_PSI_CALCULATOR.calculateNutuation(tx, new EarthNutuationElements(tx));
        aberration = ABERRATION_CALCULATOR.calculateAberration(tx, trueVSOP87Radius);
        actualLambda = calculator.calculateApparentLongitude(trueVSOP87GeometricLongitude, deltaPsi, aberration);
        double trueVSOP87LambdaArcseconds = Calcs.toSingleArcsecondsValue(199, 54, 21.56);
        assertEquals(trueVSOP87LambdaArcseconds, Calcs.toArcseconds(Math.toDegrees(actualLambda)), decimalAutoDelta(0.01));

        // Meeus 1998, Example 27.b, pp. 180-181
        geometricLongitude = GEOMETRIC_LONGITUDE_CALCULATOR.calculateGeometricLongitude(Math.toRadians(270.003272));
        deltaPsi = Math.toRadians(Calcs.arcsecondsToDegrees(-12.965));
        aberration = Math.toRadians(Calcs.arcsecondsToDegrees(-20.161));
        actualLambda = calculator.calculateApparentLongitude(geometricLongitude, deltaPsi, aberration);
        assertEquals(89.994045, Math.toDegrees(actualLambda), decimalAutoDelta(89.994045));
    }
}
