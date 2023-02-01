package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.TestUtils.decimalAutoDelta;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class SunApparentLongitudeCalculatorTest {

    private static final SunGeometricLongitudeCalculator GEOMETRIC_LONGITUDE_CALCULATOR = new SunGeometricLongitudeCalculator();
    private static final EarthNutuationInLongitudeCalculator DELTA_PSI_CALCULATOR = new EarthNutuationInLongitudeCalculator();
    private static final AberrationEarthSunCalculator ABERRATION_CALCULATOR = new AberrationEarthSunCalculator();

    private SunApparentLongitudeCalculator calculator = new SunApparentLongitudeCalculator();

    @Test
    public void shouldCalculateLambda() {
        // Meeus 1998, Example 25.a-b, pp. 165, 169
        TimelinePoint tx = new DynamicalTimelinePoint(2448908.5);

        // Meeus 1998, Example 25.b, p. 169.
        double geometricLongitude = Math.toRadians(199.907347);
        double deltaPsi = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(15.908));
        double aberration = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(-20.539));
        double actualLambda = calculator.calculate(geometricLongitude, deltaPsi, aberration);
        double exampleLambdaArcseconds = Calcs.Angle.toSingleArcsecondsValue(199, 54, 21.818);
        assertEquals(exampleLambdaArcseconds, Calcs.Angle.toArcseconds(Math.toDegrees(actualLambda)), decimalAutoDelta(0.001));

        // Meeus 1998, Example 25.a, pp. 165
        double trueVSOP87Radius = 0.99760853;
        double trueVSOP87GeometricLongitude = Math.toRadians(Calcs.Angle.toSingleDegreesValue(199, 54, 26.18));
        deltaPsi = DELTA_PSI_CALCULATOR.calculate(tx, new EarthNutuationElements(tx));
        aberration = ABERRATION_CALCULATOR.calculate(tx, trueVSOP87Radius);
        actualLambda = calculator.calculate(trueVSOP87GeometricLongitude, deltaPsi, aberration);
        double trueVSOP87LambdaArcseconds = Calcs.Angle.toSingleArcsecondsValue(199, 54, 21.56);
        assertEquals(trueVSOP87LambdaArcseconds, Calcs.Angle.toArcseconds(Math.toDegrees(actualLambda)), decimalAutoDelta(0.01));

        // Meeus 1998, Example 27.b, pp. 180-181
        geometricLongitude = GEOMETRIC_LONGITUDE_CALCULATOR.calculate(Math.toRadians(270.003272));
        deltaPsi = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(-12.965));
        aberration = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(-20.161));
        actualLambda = calculator.calculate(geometricLongitude, deltaPsi, aberration);
        assertEquals(89.994045, Math.toDegrees(actualLambda), decimalAutoDelta(89.994045));
    }
}
