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
     * @return                      declination (δ), in radians: [-π/2, π/2].
     */
    public static double eclipticalToDeclination(double latitude, double longitude, double eclipticObliquity) {
        return Math.asin(
            Math.sin(latitude) * Math.cos(eclipticObliquity)
            + Math.cos(latitude) * Math.sin(eclipticObliquity) * Math.sin(longitude)
        );
    }

    /**
     * Determines the hour angle (H0): [-1/2 turn, 1/2 turn).
     *
     * @param siderealTime      sidereal time (θ0)
     * @param rightAscension    right ascension (α)
     * @param scaleTurn         1 turn (round angle) in the same scale as the previous arguments
     * @return                  hour angle (H0), in the same scale: [-1/2 turn, 1/2 turn)
     */
    public static double calculateHourAngle(double siderealTime, double rightAscension, double scaleTurn) {
        return Calcs.Angle.toNormalSignedLongitude(siderealTime - rightAscension, scaleTurn);
    }

    /**
     * Determines the hour angle (H0): [-π, π).
     *
     * @param siderealTime      sidereal time (θ0), in radians
     * @param rightAscension    right ascension (α), in radians
     * @return                  hour angle (H0), in radians: [-π, π)
     */
    public static double calculateHourAngle(double siderealTime, double rightAscension) {
        return Calcs.Angle.toNormalSignedLongitude(siderealTime - rightAscension);
    }

    /**
     * Determines the local hour angle (H): [-1/2 turn, 1/2 turn).
     *
     * @param hourAngle0                        hour angle at the Greenwich meridian (H0)
     * @param observerPlanetographicLongitude   the observer's planetographic longitude (L;
     *                                          {@linkplain com.github.sigrarr.lunisolarcalc.phenomena.local.GeoCoords.LongitudeDirection#E Eastern}
     *                                          - negative,
     *                                          {@linkplain com.github.sigrarr.lunisolarcalc.phenomena.local.GeoCoords.LongitudeDirection#W Western}
     *                                          - positive)
     * @param scaleTurn                         1 turn (round angle) in the same scale as the previous arguments
     * @return                                  local hour angle (H): [-1/2 turn, 1/2 turn)
     */
    public static double calculateLocalHourAngle(double hourAngle0, double observerPlanetographicLongitude, double scaleTurn) {
        return Calcs.Angle.toNormalSignedLongitude(hourAngle0 - observerPlanetographicLongitude, scaleTurn);
    }

    /**
     * Determines the local hour angle (H): [-π, π).
     *
     * @param hourAngle0                        hour angle at the Greenwich meridian (H0), in radians
     * @param observerPlanetographicLongitude   the observer's planetographic longitude (L), in radians
     *                                          ({@linkplain com.github.sigrarr.lunisolarcalc.phenomena.local.GeoCoords.LongitudeDirection#E Eastern}
     *                                          - negative,
     *                                          {@linkplain com.github.sigrarr.lunisolarcalc.phenomena.local.GeoCoords.LongitudeDirection#W Western}
     *                                          - positive)
     * @return                                  local hour angle (H), in radians: [-π, π)
     */
    public static double calculateLocalHourAngle(double hourAngle0, double observerPlanetographicLongitude) {
        return Calcs.Angle.toNormalSignedLongitude(hourAngle0 - observerPlanetographicLongitude);
    }

    /**
     * Determines the altitude (h): [-π/2, π/2].
     *
     * @param declination       declination (δ), in radians
     * @param localHourAngle    local hour angle (H), in radians
     * @param observerLatitude  the observer's latitude (φ), in radians
     * @return                  altitude (h), in radians: [-π/2, π/2]
     */
    public static double calculateAltitude(double declination, double localHourAngle, double observerLatitude) {
        return Calcs.Angle.toNormalLatitude(Math.asin(
            Math.sin(observerLatitude) * Math.sin(declination)
                + Math.cos(observerLatitude) * Math.cos(declination) * Math.cos(localHourAngle)
        ));
    }

    /**
     * Determines the azimuth (from the South; A): [0, 2π).
     *
     * @param localHourAngle    local hour angle (H), in radians
     * @param declination       declination (δ), in radians
     * @param observerLatitude  the observer's latitude (φ), in radians
     * @return                  azimuth (from the South; A): [0, 2π)
     */
    public static double calculateAzimuth(double localHourAngle, double declination, double observerLatitude) {
        return Calcs.Angle.toNormalLongitude(Math.atan2(
            Math.sin(localHourAngle),
            Math.cos(localHourAngle) * Math.sin(observerLatitude)
                - Math.tan(declination) * Math.cos(observerLatitude)
        ));
    }
}
