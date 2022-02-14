package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class SunGeometricLongitudeCalculator {
    /**
     * Meeus 1998, 25.9, p. 166
     */
    public static final double BASIC_TO_FK5_DELTA = Math.toRadians(Calcs.arcsecondsToDegrees(-0.09033));
    private static final double HELIOCENTRIC_TO_GEOCENTRIC_FK5_ADDEND = Math.PI + BASIC_TO_FK5_DELTA;

    /**
     * Meeus 1998, Ch. 25, Higher accuracy, p. 166 
     */
    public double calculateGeometricLongitude(double heliocentricLongitude) {
        return Calcs.normalizeLongitudinally(heliocentricLongitude + HELIOCENTRIC_TO_GEOCENTRIC_FK5_ADDEND);
    } 
}
