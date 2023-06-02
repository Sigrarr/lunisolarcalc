package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.Objects;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

/**
 * Angular geographical coordinates, ie. latitude and longitude.
 *
 * Can be construct and expressed with either "planetographic" (default, following Meeus)
 * or "conventional" angles which differ by the longitude's sign
 * (planetographically, Eastern longitudes are negative).
 *
 * @see "Meeus 1998: Ch. 13, p. 93"
 */
public class GeoCoords {

    public static enum LatitudeDirection {
        /**
         * North
         */
        N(+1),
        /**
         * South
         */
        S(-1);
        final int sign;
        private LatitudeDirection(int sign) {
            this.sign = sign;
        }
    };

    public static enum LongitudeDirection {
        /**
         * West
         */
        W(+1),
        /**
         * East
         */
        E(-1);
        final int counterRotationSign;
        private LongitudeDirection(int counterRotationSign) {
            this.counterRotationSign = counterRotationSign;
        }
    }

    /**
     * The equivalence unit - the frame size indicating the planes' division
     * such that all angles belonging to the same frame are considered equal
     * in the terms of {@linkplain #equals(Object) equivalence checks} ({@value} arcsecond).
     */
    public static final double EQUIV_UNIT_ARCSECONDS = 1.0;
    /**
     * {@link #EQUIV_UNIT_ARCSECONDS The equivalence unit} expressed in radians (={@value #EQUIV_UNIT_ARCSECONDS} arcsecond).
     */
    public static final double EQUIV_UNIT_RADIANS = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(EQUIV_UNIT_ARCSECONDS));

    protected final double latitude;
    protected final double longitude;

    GeoCoords(double latitude, double longitude) {
        this.latitude = Calcs.Angle.toNormalLatitude(latitude);
        this.longitude = Calcs.Angle.toNormalSignedLongitude(longitude);
    }

    /**
     * Creates an instance with latitude's and longitude's absolute values
     * (expressed in degrees) with explicit geographical directions
     * (so there is no distinction between planetographic and conventional coordinates).
     *
     * @param latitudeDegrees       latitude's absolute value, in degrees
     * @param latitudeDirection     latitude's direction (N|S)
     * @param longitudeDegrees      longitude's absolute value, in degrees
     * @param longitudeDirection    longitude's direction (W|E)
     * @return                      instance
     */
    public static GeoCoords ofDegreesWithDirections(
        double latitudeDegrees,
        LatitudeDirection latitudeDirection,
        double longitudeDegrees,
        LongitudeDirection longitudeDirection
    ) {
        return ofAnglesWithDirections(Math.toRadians(latitudeDegrees), latitudeDirection, Math.toRadians(longitudeDegrees), longitudeDirection);
    }

    /**
     * Creates an instance with latitude's and longitude's absolute values
     * with explicit geographical directions
     * (so there is no distinction between planetographic and conventional coordinates).
     *
     * @param latitude              latitude's absolute value, in radians
     * @param latitudeDirection     latitude's direction (N|S)
     * @param longitude             longitude's absolute value, in radians
     * @param longitudeDirection    longitude's direction (W|E)
     * @return                      instance
     */
    public static GeoCoords ofAnglesWithDirections(
        double latitude,
        LatitudeDirection latitudeDirection,
        double longitude,
        LongitudeDirection longitudeDirection
    ) {
        return ofPlanetographic(latitudeDirection.sign * latitude, longitudeDirection.counterRotationSign * longitude);
    }

    /**
     * Creates an instance with latitude's and longitude's planetographic values
     * ({@linkplain LongitudeDirection#E Eastern} - negative,
     * {@linkplain LongitudeDirection#W Western} - positive).
     *
     * @param latitude      planetographic latitude, in radians
     * @param longitude     planetographic longitude, in radians
     *                      ({@linkplain LongitudeDirection#E Eastern} - negative,
     *                      {@linkplain LongitudeDirection#W Western} - positive)
     * @return              instance
     */
    public static GeoCoords ofPlanetographic(double latitude, double longitude) {
        return new GeoCoords(latitude, longitude);
    }

    /**
     * Creates an instance with latitude's and longitude's conventional values
     * ({@linkplain LongitudeDirection#E Eastern} - positive,
     * {@linkplain LongitudeDirection#W Western} - negative).
     *
     * @param latitude      conventional latitude, in radians
     * @param longitude     conventional longitude, in radians
     *                      ({@linkplain LongitudeDirection#E Eastern} - positive,
     *                      {@linkplain LongitudeDirection#W Western} - negative)
     * @return              instance
     */
    public static GeoCoords ofConventional(double latitude, double longitude) {
        return ofPlanetographic(latitude, -longitude);
    }

    /**
     * Gets the latitude.
     *
     * @return  latitude, in radians: [-π/2, π/2]
     *          ({@linkplain LatitudeDirection#N Northern} - positive,
     *          {@linkplain LatitudeDirection#S Southern} - negative)
     */
    public final double getLatitude() {
        return latitude;
    }

    /**
     * Gets the planetographic longitude
     * ({@linkplain LongitudeDirection#E Eastern} - negative,
     * {@linkplain LongitudeDirection#W Western} - positive).
     *
     * @return  planetographic longitude, in radians: [-π, π)
     *          ({@linkplain LongitudeDirection#E Eastern} - negative,
     *          {@linkplain LongitudeDirection#W Western} - positive)
     */
    public final double getPlanetographicLongitude() {
        return longitude;
    }

    /**
     * Gets the conventional longitude
     * ({@linkplain LongitudeDirection#E Eastern} - positive,
     * {@linkplain LongitudeDirection#W Western} - negative).
     *
     * @return  conventional longitude, in radians: [-π, π)
     *          ({@linkplain LongitudeDirection#E Eastern} - positive,
     *          {@linkplain LongitudeDirection#W Western} - negative)
     */
    public final double getConventionalLongitude() {
        return -longitude;
    }

    /**
     * Equivalence check applying the {@linkplain #EQUIV_UNIT_ARCSECONDS equivalence unit}:
     * checks whether the other pair of geographical coordintes is an equivalent of this,
     * i.e. whether they have got equal latitudes and longitudes
     * (belonging to the same equivalence unit).
     *
     * @param o     other object (pair of geographical coordinates), to check its equivalence with this
     * @return      {@code true} - if both objects (pairs of geographical coordinates) have got equal
     *              latitudes and longitudes (where "equal" means belonging to the same
     *              {@linkplain #EQUIV_UNIT_ARCSECONDS equivalence unit});
     *              {@code false } - otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GeoCoords))
            return false;
        GeoCoords gc = (GeoCoords) o;
        return Double.compare(
            Calcs.roundToDelta(latitude, EQUIV_UNIT_RADIANS),
            Calcs.roundToDelta(gc.latitude, EQUIV_UNIT_RADIANS)
        ) == 0
        && Double.compare(
            Calcs.roundToDelta(longitude, EQUIV_UNIT_RADIANS),
            Calcs.roundToDelta(gc.longitude, EQUIV_UNIT_RADIANS)
        ) == 0;
    }

    /**
     * Equivalence check applying the {@linkplain #EQUIV_UNIT_ARCSECONDS equivalence unit}:
     * generates a {@linkplain Object#hashCode() hash code} identifying the combination
     * of this instance's latitude's and longitude's equivalence units.
     *
     * @return  {@linkplain Object#hashCode() hash code} identifying the combination
     *          of this instance's latitude's and longitude's
     *          {@linkplain #EQUIV_UNIT_ARCSECONDS equivalence units}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            Calcs.roundToDelta(latitude, EQUIV_UNIT_RADIANS),
            Calcs.roundToDelta(longitude, EQUIV_UNIT_RADIANS)
        );
    }

    @Override
    public String toString() {
        return String.format("(%s) ϕ=%f λ=%f",
            GeoCoords.class.getSimpleName(),
            latitude,
            longitude
        );
    }
}
