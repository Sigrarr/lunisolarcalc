package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public final class GeoCoords {

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

    protected final double latitude;
    protected final double longitude;

    GeoCoords(double latitude, double longitude) {
        this.latitude = Calcs.Angle.toNormalLatitude(latitude);
        this.longitude = Calcs.Angle.toNormalSignedLongtiude(longitude);
    }

    public static GeoCoords ofConventionalDegrees(
        double latitudeDegrees,
        LatitudeDirection latitudeDirection,
        double longitudeDegrees,
        LongitudeDirection longitudeDirection
    ) {
        return ofConventional(Math.toRadians(latitudeDegrees), latitudeDirection, Math.toRadians(longitudeDegrees), longitudeDirection);
    }

    public static GeoCoords ofConventional(
        double latitude,
        LatitudeDirection latitudeDirection,
        double longitude,
        LongitudeDirection longitudeDirection
    ) {
        return ofPlanetographic(latitudeDirection.sign * latitude, longitudeDirection.counterRotationSign * longitude);
    }

    public static GeoCoords ofPlanetographic(double latitude, double longitude) {
        return new GeoCoords(latitude, longitude);
    }

    public double getPlanetographicLatitude() {
        return latitude;
    }

    public double getPlanetographicLongitude() {
        return longitude;
    }

    public double getConventionalLatitude() {
        return latitude;
    }

    public double getConventionalLongitude() {
        return -longitude;
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
