package com.github.sigrarr.lunisolarcalc.subjects;

/**
 * The observer's position on Earth,
 * which includes geographical coordinates and elevation.
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

    /**
     * Obtains an instance from given geographical coordinates and elevation.
     *
     * @param geoCoords     geographical coordinates
     * @param elevation     elevation, in meters
     * @return              instance
     */
    public static GeoPosition of(GeoCoords geoCoords, double elevation) {
        return new GeoPosition(geoCoords, elevation);
    }

    /**
     * Obtains a sea-level instance from given geographical coordinates.
     *
     * @param geoCoords     geographical coordinates
     * @return              instance
     */
    public static GeoPosition of(GeoCoords geoCoords) {
        return new GeoPosition(geoCoords, 0);
    }
}
