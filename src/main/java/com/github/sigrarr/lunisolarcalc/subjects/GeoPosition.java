package com.github.sigrarr.lunisolarcalc.subjects;

/**
 * The observer's position on Earth.
 */
public class GeoPosition {
    /**
     * Geographical coordinates.
     */
    public final GeoCoords coords;
    /**
     * Elevation (height above sea level), in meters.
     */
    public final double elevation;

    GeoPosition(GeoCoords coords, double elevation) {
        this.coords = coords;
        this.elevation = elevation;
    }

    public static GeoPosition of(GeoCoords geoCoords, double elevation) {
        return new GeoPosition(geoCoords, elevation);
    }

    public static GeoPosition of(GeoCoords geoCoords) {
        return new GeoPosition(geoCoords, 0);
    }

    public static GeoPosition ofPlanetographic(double latitude, double longitude, double elevation) {
        return new GeoPosition(new GeoCoords(latitude, longitude), elevation);
    }

    public static GeoPosition ofConventional(double latitude, double longitude, double elevation) {
        return new GeoPosition(GeoCoords.ofConventional(latitude, longitude), elevation);
    }

    public static GeoPosition ofConventionalDegrees(double latitudeDegrees, double longitudeDegrees, double elevation) {
        return new GeoPosition(GeoCoords.ofConventionalDegrees(latitudeDegrees, longitudeDegrees), elevation);
    }

    public static GeoPosition ofPlanetographic(double latitude, double longitude) {
        return new GeoPosition(new GeoCoords(latitude, longitude), 0);
    }

    public static GeoPosition ofConventional(double latitude, double longitude) {
        return new GeoPosition(GeoCoords.ofConventional(latitude, longitude), 0);
    }

    public static GeoPosition ofConventionalDegrees(double latitudeDegrees, double longitudeDegrees) {
        return new GeoPosition(GeoCoords.ofConventionalDegrees(latitudeDegrees, longitudeDegrees), 0);
    }
}
