package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.space.HeliocentricCoordinateCalculator.Unit;
import com.github.sigrarr.lunisolarcalc.space.periodicterms.*;
import com.github.sigrarr.lunisolarcalc.time.Timeline;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class SunGeocentricLongitudeCalculator {
    /**
     * Meeus 1998, 25.9, p. 166
     */
    public static final double BASIC_TO_FK5_DELTA = Math.toRadians(Calcs.arcsecondsToDegrees(-0.09033));
    private static final double HELIOCENTRIC_TO_GEOCENTRIC_FK5_ADDEND = Math.PI + BASIC_TO_FK5_DELTA;
    private HeliocentricCoordinateCalculator heliocentricCalculator = new HeliocentricCoordinateCalculator(new PeriodicTermsForEarthLongitude(), Unit.RADIAN);
    private EarthNutuationCalculator deltaPsiCalculator = new EarthNutuationCalculator(new PeriodicTermsForNutuationInLongitude());

    /**
     * Meeus 1998, Ch. 25, Higher accuracy, p. 166 
     */
    public double calculateGeometricLongitude(double tau) {
        return heliocentricCalculator.calculateCoordinate(tau) + HELIOCENTRIC_TO_GEOCENTRIC_FK5_ADDEND;
    } 

    /**
     * Meeus 1998, Ch. 25, Higher accuracy, p. 167 
     */
    public double calculateApparentLongitude(double tau) {
        double value = calculateGeometricLongitude(tau);
        value += deltaPsiCalculator.calculateNutuation(Timeline.millenialTauToCenturialT(tau));
        // TODO add aberration
        return value;
    }
}
