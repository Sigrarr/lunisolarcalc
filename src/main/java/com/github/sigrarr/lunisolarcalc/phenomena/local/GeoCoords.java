package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.Objects;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class GeoCoords {

    public static enum LatitudeDirection {
        N(+1), S(-1);
        final int sign;
        private LatitudeDirection(int sign) {
            this.sign = sign;
        }
    };

    public static enum LongitudeDirection {
        W(+1), E(-1);
        final int counterRotationSign;
        private LongitudeDirection(int counterRotationSign) {
            this.counterRotationSign = counterRotationSign;
        }
    }

    public static final double EQUIV_UNIT_RADIANS = Math.toRadians(Calcs.ARCSECOND_TO_DEGREE);

    protected final double latitude;
    protected final double longitude;

    GeoCoords(double latitude, double longitude) {
        this.latitude = Calcs.Angle.toNormalLatitude(latitude);
        this.longitude = Calcs.Angle.toNormalSignedLongtiude(longitude);
    }

    public static GeoCoords ofDegreesWithDirections(
        double latitudeDegrees,
        LatitudeDirection latitudeDirection,
        double longitudeDegrees,
        LongitudeDirection longitudeDirection
    ) {
        return ofAnglesWithDirections(Math.toRadians(latitudeDegrees), latitudeDirection, Math.toRadians(longitudeDegrees), longitudeDirection);
    }

    public static GeoCoords ofAnglesWithDirections(
        double latitude,
        LatitudeDirection latitudeDirection,
        double longitude,
        LongitudeDirection longitudeDirection
    ) {
        return ofPlanetographic(latitudeDirection.sign * latitude, longitudeDirection.counterRotationSign * longitude);
    }

    public static GeoCoords ofConventional(double latitude, double longitude) {
        return ofPlanetographic(latitude, -longitude);
    }

    public static GeoCoords ofPlanetographic(double latitude, double longitude) {
        return new GeoCoords(latitude, longitude);
    }

    public final double getPlanetographicLatitude() {
        return latitude;
    }

    public final double getPlanetographicLongitude() {
        return longitude;
    }

    public final double getConventionalLatitude() {
        return latitude;
    }

    public final double getConventionalLongitude() {
        return -longitude;
    }

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
