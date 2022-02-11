package com.github.sigrarr.lunisolarcalc.space;

import static org.junit.Assert.assertEquals;

import com.github.sigrarr.lunisolarcalc.space.periodicterms.PeriodicTermsForNutuationInLongitude;
import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.Test;

public class AberrationCalculatorTest {

    private AberrationCalculator calculator = new AberrationCalculator();
    private EarthNutuationCalculator deltaPsiCalculator = new EarthNutuationCalculator(new PeriodicTermsForNutuationInLongitude());
    
    @Test
    public void shouldCalculateAberration() {
        // Meeus 1998, Example 25.b, p. 169
        double tau = Timeline.julianDayToMillenialTau(2448908.5);
        double actualAberration = calculator.calculateAberration(tau);
        double aberrationCalculatedWithSimplerMethodArcseconds = -20.539;
        assertEquals(aberrationCalculatedWithSimplerMethodArcseconds, Calcs.toArcseconds(Math.toDegrees(actualAberration)), 0.01);
        // Meeus 1998, Example 25.a, p. 165
        double trueVSOP87GeometricLongitudeDegrees = 199.0 + Calcs.arcminutesToDegrees(54.0) + Calcs.arcsecondsToDegrees(26.18);
        double trueVSOP87ApparentLongitudeDegrees = 199.0 + Calcs.arcminutesToDegrees(54.0) + Calcs.arcsecondsToDegrees(21.56);
        double deltaPsiDegrees = Math.toDegrees(deltaPsiCalculator.calculateNutuation(Timeline.millenialTauToCenturialT(tau)));
        double implicitAberrationDegrees = trueVSOP87ApparentLongitudeDegrees - trueVSOP87GeometricLongitudeDegrees - deltaPsiDegrees;
        assertEquals(implicitAberrationDegrees, Math.toDegrees(actualAberration), Calcs.arcsecondsToDegrees(0.01));
    }
}
