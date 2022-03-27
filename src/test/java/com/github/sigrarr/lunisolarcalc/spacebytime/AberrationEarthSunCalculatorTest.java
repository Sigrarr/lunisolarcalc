package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.MeanMotionApproximate.TROPICAL_YEAR;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.jupiter.api.Test;

public class AberrationEarthSunCalculatorTest {

    private static final EarthNutuationCalculator DELTA_PSI_CALCULATOR = new EarthNutuationInLongitudeCalculator();

    private AberrationEarthSunCalculator calculator = new AberrationEarthSunCalculator();
    
    @Test
    public void shouldCalculateAberration() {
        // Meeus 1998, Example 25.a-b, pp. 165, 169
        TimelinePoint tx = new TimelinePoint(2448908.5);

        // Meeus 1998, Example 25.b, p. 169
        double radius = 0.99760775;
        double actualAberration = calculator.calculateAberration(tx, radius);
        double aberrationCalculatedWithSimplerMethodArcseconds = -20.539;
        assertEquals(aberrationCalculatedWithSimplerMethodArcseconds, Calcs.toArcseconds(Math.toDegrees(actualAberration)), TROPICAL_YEAR.arcsecondsPerTimeMiliseconds(215));

        // Meeus 1998, Example 25.a, p. 165
        double trueVSOP87Radius = 0.99760853;
        actualAberration = calculator.calculateAberration(tx, trueVSOP87Radius);
        double trueVSOP87GeometricLongitudeDegrees = Calcs.toSingleDegreesValue(199, 54, 26.18);
        double trueVSOP87ApparentLongitudeDegrees = Calcs.toSingleDegreesValue(199, 54, 21.56);
        double deltaPsiDegrees = Math.toDegrees(DELTA_PSI_CALCULATOR.calculateNutuation(tx, new EarthNutuationElements(tx)));
        double implicitAberrationDegrees = trueVSOP87ApparentLongitudeDegrees - trueVSOP87GeometricLongitudeDegrees - deltaPsiDegrees;
        assertEquals(implicitAberrationDegrees, Math.toDegrees(actualAberration), TROPICAL_YEAR.degreesPerTimeMiliseconds(75));

        // Meeus 1998, Example 27.b, pp. 180-181
        tx = new TimelinePoint(2437837.38589);
        radius = 1.0163018;
        actualAberration = calculator.calculateAberration(tx, radius);
        assertEquals(-20.161, Calcs.toArcseconds(Math.toDegrees(actualAberration)), TROPICAL_YEAR.arcsecondsPerTimeMiliseconds(150));
    }
}
