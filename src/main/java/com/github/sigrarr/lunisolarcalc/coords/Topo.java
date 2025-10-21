package com.github.sigrarr.lunisolarcalc.coords;

import com.github.sigrarr.lunisolarcalc.util.*;

/**
 * Formulae concerning the Earth's globe and topocentric astronomical coordinates.
 *
 * @see "Meeus 1998: Ch. 11, 40 (pp. 81..., 279...)"
 */
public class Topo {
    /**
     * The Earth's falttening (F).
     */
    public static final double FLATTENING = 1.0/298.257;
    /**
     * The Earth's equatorial radius (A), in kilometers.
     */
    public static final double EQUATORIAL_RADIUS_KM = 0.001 * ConstantsAndUnits.EARTH_EQUATORIAL_RADIUS_METERS;
    /**
     * b/a: the ratio of the Earth's polar radius to the Earth's equatorial radius.
     */
    private static final double B_TO_A = 1.0 - FLATTENING;
    /**
     * The Earth's polar radius (B), in kilometers.
     */
    public static final double POLAR_RADIUS_KM = EQUATORIAL_RADIUS_KM * B_TO_A;

    /**
     * Caltulates the observer's geocentric latitude (φ′).
     * Accurate at sea level.
     *
     * @param geographicalLatitude  geographical latitude (φ), in radians
     * @return                      the sea-level observer's geocentric latitude (φ′),
     *                              in radians: [-π/2, π/2]
     */
    public static double geographicalToGeocentricLatitude(double geographicalLatitude) {
        return Calcs.Angle.toNormalLatitude(
            Math.atan(
                B_TO_A * B_TO_A * Math.tan(geographicalLatitude)
            )
        );
    }

    /**
     * Calculates the observer's geocentric radius (ρ)
     * as a fraction of the Earth's equatorial radius, for sea level.
     *
     * @param geographicalLatitude  geographical latitude (φ), in radians
     * @return                      the sea-level observer's geocentric radius (ρ)
     *                              as a fraction of the Earth's equatorial radius
     */
    public static double calculateGeocentricRadius(double geographicalLatitude) {
        return 0.9983271
            + 0.0016764 * Math.cos(2*geographicalLatitude)
            - 0.0000035 * Math.cos(4*geographicalLatitude);
    }

    /**
     * Calculates the parallax in right ascension (Δα),
     * i.e. the difference between the topocentric and geocentric right ascension (α′ − α).
     *
     * @param declination                       declination (δ), in radians
     * @param localHourAngle                    local hour angle (H), in radians
     * @param equatorialHorizontalParallaxSine  sine of the equatorial horizontal parallax (sin(π))
     * @param rhoCosPhiPrime                    {@linkplain #calculateRhoCosPhiPrime(double, double) ρ*cos(φ′)}
     * @return                                  parallax in right ascension (Δα = α′ − α), in radians
     */
    public static double calculateParallaxInRightAscension(
        double declination,
        double localHourAngle,
        double equatorialHorizontalParallaxSine,
        double rhoCosPhiPrime
    ) {
        return Math.atan2(
            - rhoCosPhiPrime * equatorialHorizontalParallaxSine * Math.sin(localHourAngle),
            Math.cos(declination) - rhoCosPhiPrime * equatorialHorizontalParallaxSine  * Math.cos(localHourAngle)
        );
    }

    /**
     * Calculates the topocentric right ascension (α′).
     *
     * @param rightAscension            geocentric right ascension (α), in radians
     * @param parallaxInRightAscension  parallax in right ascension (Δα), in radians
     * @return                          topocentric right ascension (α′), in radians: [0, 2π)
     */
    public static double calculateTopocentricRightAscension(double rightAscension, double parallaxInRightAscension) {
        return Calcs.Angle.toNormalLongitude(rightAscension + parallaxInRightAscension);
    }

    /**
     * Calculates the topocentric declination (δ′).
     *
     * @param declination                       geocentric declination (δ), in radians
     * @param parallaxInRightAscension          parallax in right ascension (Δα), in radians
     * @param localHourAngle                    local hour angle (H), in radians
     * @param equatorialHorizontalParallaxSine  sine of the equatorial horizontal parallax (sin(π))
     * @param rhoSinPhiPrime                    {@linkplain #calculateRhoSinPhiPrime(double, double) ρ*sin(φ′)}
     * @param rhoCosPhiPrime                    {@linkplain #calculateRhoCosPhiPrime(double, double) ρ*cos(φ′)}
     * @return                                  topocentric declination (δ′), in radians: [-π/2, π/2]
     */
    public static double calculateTopocentricDeclination(
        double declination,
        double parallaxInRightAscension,
        double localHourAngle,
        double equatorialHorizontalParallaxSine,
        double rhoSinPhiPrime,
        double rhoCosPhiPrime
    ) {
        return Calcs.Angle.toNormalLatitude(
            Math.atan2(
                (Math.sin(declination) - rhoSinPhiPrime * equatorialHorizontalParallaxSine)
                    * Math.cos(parallaxInRightAscension),
                Math.cos(declination) - rhoCosPhiPrime * equatorialHorizontalParallaxSine * Math.cos(localHourAngle)
            )
        );
    }

    /**
     * Calculates the topocentric local hour angle (H′).
     *
     * Note that if you have already got the topocentric right ascension
     * (α′ = α + Δα), you may simply pass it to the methods:
     * {@link Transformations#calculateLocalHourAngle(double, double, double) Transformations.calculateLocalHourAngle},
     * {@link Transformations#calculateHourAngle(double, double) Transformations.calculateHourAngle}
     * - and the result obtained this way will be topocentric.
     *
     * @param localHourAngle                    geocentric local hour angle (H), in radians
     * @param declination                       declination (δ), in radians
     * @param equatorialHorizontalParallaxSine  sine of the equatorial horizontal parallax (sin(π))
     * @param rhoCosPhiPrime                    {@linkplain #calculateRhoCosPhiPrime(double, double) ρ*cos(φ′)}
     * @return                                  topocentric hour angle (H′), in radians
     */
    public static double calculateTopocentricLocalHourAngle(
        double localHourAngle,
        double declination,
        double equatorialHorizontalParallaxSine,
        double rhoCosPhiPrime
    ) {
        double dCos = Math.cos(declination);
        return Calcs.Angle.toNormalSignedLongitude(
            Math.atan2(
                dCos * Math.sin(localHourAngle),
                dCos * Math.cos(localHourAngle) - rhoCosPhiPrime * equatorialHorizontalParallaxSine
            )
        );
    }

    /**
     * Approximates the topocentric altitude (h′).
     *
     * @param altitude                          geocentric altitude (h), in radians
     * @param equatorialHorizontalParallaxSine  sine of the equatorial horizontal parallax (sin(π))
     * @param geocentricRadius                  ρ geocentric radius as a fraction
     *                                          of the Earth's equatorial radius
     * @return                                  approximation of the topocentric altitude (h′),
     *                                          in radians: [-π/2, π/2]
     */
    public static double approximateTopocentricAltitude(
        double altitude,
        double equatorialHorizontalParallaxSine,
        double geocentricRadius
    ) {
        double parallax = geocentricRadius * equatorialHorizontalParallaxSine * Math.cos(altitude);
        return Calcs.Angle.toNormalLatitude(altitude - parallax);
    }

    /**
     * Calculates the ρ*sin(φ′) quantity, where ρ is the observer's
     * geocentric radius as a fraction of the Earth's equatorial radius
     * and φ′ is the geocentric latitude.
     *
     * @param geographicalLatitude    geographical latitude (φ), in radians
     * @param elevation               the observer's elevation above sea level (H), in meters
     * @return                        ρ*sin(φ′), where ρ is the observer's geocentric radius
     *                                as a fraction of the Earth's equatorial radius
     */
    public static double calculateRhoSinPhiPrime(double geographicalLatitude, double elevation) {
        return B_TO_A * Math.sin(u(geographicalLatitude))
            + elevation/ConstantsAndUnits.EARTH_EQUATORIAL_RADIUS_METERS
                * Math.sin(geographicalLatitude);
    }

    /**
     * Calculates the ρ*cos(φ′) quantity, where ρ is the observer's
     * geocentric radius as a fraction of the Earth's equatorial radius
     * and φ′ is the geocentric latitude.
     *
     * @param geographicalLatitude    geographical latitude (φ), in radians
     * @param elevation               the observer's elevation above sea level (H), in meters
     * @return                        ρ*cos(φ′), where ρ is the observer's geocentric radius
     *                                as a fraction of the Earth's equatorial radius
     */
    public static double calculateRhoCosPhiPrime(double geographicalLatitude, double elevation) {
        return Math.cos(u(geographicalLatitude))
            + elevation/ConstantsAndUnits.EARTH_EQUATORIAL_RADIUS_METERS
                * Math.cos(geographicalLatitude);
    }

    private static double u(double geographicalLatitude) {
        return Math.atan(
            B_TO_A * Math.tan(geographicalLatitude)
        );
    }
}
