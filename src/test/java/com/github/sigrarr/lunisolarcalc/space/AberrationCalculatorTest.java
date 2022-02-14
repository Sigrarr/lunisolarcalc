package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;
import static com.github.sigrarr.lunisolarcalc.util.MeanValueApproximations.SunEarthRelativeMotion.arcsecondsPerTimeMiliseconds;
import static com.github.sigrarr.lunisolarcalc.util.MeanValueApproximations.SunEarthRelativeMotion.degreesPerTimeMiliseconds;

import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.Test;

public class AberrationCalculatorTest {

    private AberrationEarthSunCalculator calculator = new AberrationEarthSunCalculator();
    private EarthNutuationCalculator deltaPsiCalculator = new EarthNutuationInLongitudeCalculator();
    
    @Test
    public void shouldCalculateAberration() {
        // Meeus 1998, Example 25.a-b, pp. 165, 169
        double jd = 2448908.5;
        double cT = Timeline.julianDayToCenturialT(jd);
        double tau = Timeline.julianDayToMillenialTau(jd);
        double actualAberration = calculator.calculateAberration(tau);

        // Meeus 1998, Example 25.b, p. 169
        double aberrationCalculatedWithSimplerMethodArcseconds = -20.539;
        assertEquals(aberrationCalculatedWithSimplerMethodArcseconds, Calcs.toArcseconds(Math.toDegrees(actualAberration)), arcsecondsPerTimeMiliseconds(215));

        // Meeus 1998, Example 25.a, p. 165
        double trueVSOP87GeometricLongitudeDegrees = 199.0 + Calcs.arcminutesToDegrees(54.0) + Calcs.arcsecondsToDegrees(26.18);
        double trueVSOP87ApparentLongitudeDegrees = 199.0 + Calcs.arcminutesToDegrees(54.0) + Calcs.arcsecondsToDegrees(21.56);
        double deltaPsiDegrees = Math.toDegrees(deltaPsiCalculator.calculateNutuation(cT));
        double implicitAberrationDegrees = trueVSOP87ApparentLongitudeDegrees - trueVSOP87GeometricLongitudeDegrees - deltaPsiDegrees;
        assertEquals(implicitAberrationDegrees, Math.toDegrees(actualAberration), degreesPerTimeMiliseconds(115));

        // Meeus 1998, Example 27.b, pp. 180-181
        tau = Timeline.julianDayToMillenialTau(2437837.38589);
        actualAberration = calculator.calculateAberration(tau);
        assertEquals(-20.161, Calcs.toArcseconds(Math.toDegrees(actualAberration)), arcsecondsPerTimeMiliseconds(215));
    }
}
