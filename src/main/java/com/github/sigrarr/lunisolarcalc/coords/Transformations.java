package com.github.sigrarr.lunisolarcalc.coords;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

/**
 * Transformations between the ecliptical, equatorial and horizontal coordinates.
 *
 * @see "Meeus 1998: Ch. 13 (pp. 91...)"
 */
public abstract class Transformations {
    /**
     * Transforms equatorial coordinates to ecliptical longitude (λ).
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
     * Transforms equatorial coordinates to ecliptical latitude (β).
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
     * Transforms ecliptical coordinates to right ascension (α).
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
     * Transforms ecliptical coordinates to declination (δ).
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
     * Determines the hour angle (H).
     * This is a general method: output will take the charachteristics
     * of input; you can pass the local sidereal time (θ) to obtain
     * the local hour angle (H) or the sidereal time at the Greenwich meridian (θ0)
     * to get the Greenwich hour angle (H0) accordingly.
     *
     * @param siderealTime      sidereal time (θ)
     * @param rightAscension    right ascension (α)
     * @param scaleTurn         1 turn (round angle) in the same scale as the previous arguments
     * @return                  hour angle (H), bearing the characteristics of arguments
     *                          (e.g. Greenwich hour angle H0 if the Greenwich sidereal time θ0 was passed),
     *                          in the same scale: [-1/2 turn, 1/2 turn)
     */
    public static double calculateHourAngle(double siderealTime, double rightAscension, double scaleTurn) {
        return Calcs.Angle.toNormalSignedLongitude(siderealTime - rightAscension, scaleTurn);
    }

    /**
     * Determines the hour angle (H).
     * This is a general method: output will take the charachteristics
     * of input; you can pass the local sidereal time (θ) to obtain
     * the local hour angle (H) or the sidereal time at the Greenwich meridian (θ0)
     * to get the Greenwich hour angle (H0) accordingly.
     *
     * @param siderealTime      sidereal time (θ), in radians
     * @param rightAscension    right ascension (α), in radians
     * @return                  hour angle (H), bearing the characteristics of arguments
     *                          (e.g. Greenwich hour angle H0 if the Greenwich sidereal time θ0 was passed),
     *                          in radians: [-π, π)
     */
    public static double calculateHourAngle(double siderealTime, double rightAscension) {
        return Calcs.Angle.toNormalSignedLongitude(siderealTime - rightAscension);
    }

    /**
     * Determines the local hour angle (H).
     *
     * @param siderealTime0                     sidereal time at the Greenwich meridian (θ0)
     * @param observerPlanetographicLongitude   the observer's planetographic longitude (L)
     *                                          (Eastern - negative, Western - positive)
     * @param rightAscension                    right ascension (α)
     * @param scaleTurn                         1 turn (round angle) in the same scale as the previous arguments
     * @return                                  local hour angle (H): [-1/2 turn, 1/2 turn)
     */
    public static double calculateLocalHourAngle(
        double siderealTime0,
        double observerPlanetographicLongitude,
        double rightAscension,
        double scaleTurn
    ) {
        return Calcs.Angle.toNormalSignedLongitude(siderealTime0 - observerPlanetographicLongitude - rightAscension, scaleTurn);
    }

    /**
     * Determines the local hour angle (H).
     *
     * @param siderealTime0                     sidereal time at the Greenwich meridian (θ0), in radians
     * @param observerPlanetographicLongitude   the observer's planetographic longitude (L), in radians
     *                                          (Eastern - negative, Western - positive)
     * @param rightAscension                    right ascension (α), in radians
     * @return                                  local hour angle (H), in radians: [-π, π)
     */
    public static double calculateLocalHourAngle(
        double siderealTime0,
        double observerPlanetographicLongitude,
        double rightAscension
    ) {
        return Calcs.Angle.toNormalSignedLongitude(siderealTime0 - observerPlanetographicLongitude - rightAscension);
    }

    /**
     * Determines the local hour angle (H).
     *
     * @param hourAngle0                        hour angle at the Greenwich meridian (H0)
     * @param observerPlanetographicLongitude   the observer's planetographic longitude (L)
     *                                          (Eastern - negative, Western - positive)
     * @param scaleTurn                         1 turn (round angle) in the same scale as the previous arguments
     * @return                                  local hour angle (H): [-1/2 turn, 1/2 turn)
     */
    public static double hourAngle0ToLocal(double hourAngle0, double observerPlanetographicLongitude, double scaleTurn) {
        return Calcs.Angle.toNormalSignedLongitude(hourAngle0 - observerPlanetographicLongitude, scaleTurn);
    }

    /**
     * Determines the local hour angle (H).
     *
     * @param hourAngle0                        hour angle at the Greenwich meridian (H0), in radians
     * @param observerPlanetographicLongitude   the observer's planetographic longitude (L), in radians
     *                                          (Eastern - negative, Western - positive)
     * @return                                  local hour angle (H), in radians: [-π, π)
     */
    public static double hourAngle0ToLocal(double hourAngle0, double observerPlanetographicLongitude) {
        return Calcs.Angle.toNormalSignedLongitude(hourAngle0 - observerPlanetographicLongitude);
    }

    /**
     * Determines the altitude (h).
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
     * Determines the azimuth (from the South; A).
     *
     * @param localHourAngle    local hour angle (H), in radians
     * @param declination       declination (δ), in radians
     * @param observerLatitude  the observer's latitude (φ), in radians
     * @return                  azimuth (from the South; A), in radians: [0, 2π)
     */
    public static double calculateAzimuth(double localHourAngle, double declination, double observerLatitude) {
        return Calcs.Angle.toNormalLongitude(Math.atan2(
            Math.sin(localHourAngle),
            Math.cos(localHourAngle) * Math.sin(observerLatitude)
                - Math.tan(declination) * Math.cos(observerLatitude)
        ));
    }
}
