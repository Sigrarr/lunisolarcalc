package com.github.sigrarr.lunisolarcalc.coords;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

/**
 * Transformations between the ecliptical and equatorial coordinates.
 *
 * @see "Meeus 1998: Ch. 13 (pp. 91...)"
 */
public abstract class Transformations {
    /**
     * Transforms equatorial coordinates to ecliptical longitude (λ): [0, 2π).
     *
     * @param rightAscension        right ascension (α), in radians
     * @param declination           declination (δ), in radians
     * @param eclipticObliquity     obliquity of the ecliptic (ε), in radians
     * @return                      ecliptical longitude (λ), in radians: [0, 2π)
     */
    public static double equatorialToLongitude(double rightAscension, double declination, double eclipticObliquity) {
        return Calcs.Angle.toNormalLongitude(Math.atan2(
            Math.sin(rightAscension) * Math.cos(eclipticObliquity) + Math.tan(declination) * Math.sin(eclipticObliquity),
            Math.cos(rightAscension)
        ));
    }

    /**
     * Transforms equatorial coordinates to ecliptical latitude (β): [-π/2, π/2].
     *
     * @param declination           declination (δ), in radians
     * @param rightAscension        right ascension (α), in radians
     * @param eclipticObliquity     obliquity of the ecliptic (ε), in radians
     * @return                      ecliptical latitude (β), in radians: [-π/2, π/2]
     */
    public static double equatorialToLatitude(double declination, double rightAscension, double eclipticObliquity) {
        return Math.asin(
            Math.sin(declination) * Math.cos(eclipticObliquity)
            - Math.cos(declination) * Math.sin(eclipticObliquity) * Math.sin(rightAscension)
        );
    }

    /**
     * Transforms ecliptical coordinates to right ascension (α): [0, 2π).
     *
     * @param longitude             ecliptical longitude (λ), in radians
     * @param latitude              ecliptical latitude (β), in radians
     * @param eclipticObliquity     obliquity of the ecliptic (ε), in radians
     * @return                      right ascension (α), in radians: [0, 2π)
     */
    public static double eclipticalToRightAscension(double longitude, double latitude, double eclipticObliquity) {
        return Calcs.Angle.toNormalLongitude(Math.atan2(
            Math.sin(longitude) * Math.cos(eclipticObliquity) - Math.tan(latitude) * Math.sin(eclipticObliquity),
            Math.cos(longitude)
        ));
    }

    /**
     * Transforms ecliptical coordinates to declination (δ): [-π/2, π/2].
     *
     * @param latitude              ecliptical latitude (β), in radians
     * @param longitude             ecliptical longitude (λ), in radians
     * @param eclipticObliquity     obliquity of the ecliptic (ε), in radians
     * @return                      right declination, in radians (δ): [-π/2, π/2].
     */
    public static double eclipticalToDeclination(double latitude, double longitude, double eclipticObliquity) {
        return Math.asin(
            Math.sin(latitude) * Math.cos(eclipticObliquity)
            + Math.cos(latitude) * Math.sin(eclipticObliquity) * Math.sin(longitude)
        );
    }

    /**
     * Determines the hour angle (H): [-1/2 turn, 1/2 turn).
     *
     * @param siderealTime      sidereal time (θ)
     * @param rightAscension    right ascension (α)
     * @param scaleTurn         1 turn (round angle) in the same scale as the previous arguments
     * @return                  hour angle (H), in the same scale: [-1/2 turn, 1/2 turn)
     */
    public static double calculateHourAngle(double siderealTime, double rightAscension, double scaleTurn) {
        return Calcs.Angle.toNormalSignedLongitude(siderealTime - rightAscension, scaleTurn);
    }

    /**
     * Determines the hour angle (H): [-π, π).
     *
     * @param siderealTime      sidereal time (θ), in radians
     * @param rightAscension    right ascension (α), in radians
     * @return                  hour angle (H), in radians: [-π, π)
     */
    public static double calculateHourAngle(double siderealTime, double rightAscension) {
        return Calcs.Angle.toNormalSignedLongitude(siderealTime - rightAscension);
    }
}
