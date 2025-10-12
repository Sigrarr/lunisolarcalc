package com.github.sigrarr.lunisolarcalc.coords;

import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.ConstantsAndUnits;

/**
 * @see "Meeus 1998: Ch. 11 (pp. 81...)"
 */
public class Topo {
    /**
     * The Earth's falttening.
     */
    public static final double F = 1.0/298.257;
    /**
     * The Earth's equatorial radius, in kilometers.
     */
    public static final double A = 0.001 * ConstantsAndUnits.EARTH_EQUATORIAL_RADIUS_METERS;
    /**
     * b/a: the ratio of the Earth's polar radius to the Earth's equatorial radius.
     */
    public static final double B_TO_A = 1.0 - F;
    /**
     * The Earth's polar radius, in kilometers.
     */
    public static final double B = A * B_TO_A;

    /**
     * Caltulates the observer's geocentric latitude (φ′), in radians: [-π/2, π/2].
     * Applicable to the observer at sea level.
     *
     * @param geographicalLatitude  geographical latitude (φ), in radians
     * @return                      geocentric latitude (φ′), in radians: [-π/2, π/2]
     */
    public static double geographicalToGeocentricLatitudeAtSeaLevel(double geographicalLatitude) {
        return Calcs.Angle.toNormalLatitude(
            Math.atan(
                B_TO_A * B_TO_A * Math.tan(geographicalLatitude)
            )
        );
    }

    /**
     * Calculates the intermediate argument u,
     * used in calculations of several topocentric quantities.
     *
     * @param geographicalLatitude  geographical latitude (φ), in radians
     * @return                      intermediate argument u, in radians
     */
    public static double calculateU(double geographicalLatitude) {
        return Math.atan(
            B_TO_A * Math.tan(geographicalLatitude)
        );
    }

    /**
     * Calculates the ratio of the observer's geocentric radius
     * to the Earth equatorial radius (ρ). Apllcable for sea level.
     *
     * @param geographicalLatitude  geographical latitude (φ), in radians
     * @return                      ratio of the sea level observer's geocentric radius
     *                              to the Earth equatorial radius (ρ)
     */
    public static double calculateRadiusRatioAtSeaLevel(double geographicalLatitude) {
        return 0.9983271
            + 0.0016764 * Math.cos(2*geographicalLatitude)
            - 0.0000035 * Math.cos(4*geographicalLatitude);
    }
}
