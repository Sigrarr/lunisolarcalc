package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.time.Timeline;

public final class SunApparentLongitudeCalculator {

    private SunGeometricLongitudeCalculator geometricCalculator = new SunGeometricLongitudeCalculator();
    private EarthNutuationCalculator deltaPsiCalculator = new EarthNutuationInLongitudeCalculator();
    private AberrationEarthSunCalculator aberrationCalculator = new AberrationEarthSunCalculator();

    /**
     * Meeus 1998, Ch. 25, Higher accuracy, p. 167 
     */
    public double calculateApparentLongitude(double tau) {
        double value = geometricCalculator.calculateGeometricLongitude(tau);
        value += deltaPsiCalculator.calculateNutuation(Timeline.millenialTauToCenturialT(tau));
        value += aberrationCalculator.calculateAberration(tau);
        return value;
    }
}
