package com.github.sigrarr.lunisolarcalc.subjects;

import java.util.Objects;

/**
 * The observer's position on Earth,
 * which includes geographical coordinates and elevation.
 *
 * In this representation the elevation is an integral number of meters.
 */
public class GeoPosition {
    /**
     * Geographical coordinates.
     */
    public final GeoCoords coords;
    /**
     * Elevation (height above sea level), in meters.
     */
    public final int elevation;

    GeoPosition(GeoCoords coords, int elevation) {
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
    public static GeoPosition of(GeoCoords geoCoords, int elevation) {
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

    @Override
    public int hashCode() {
        return Objects.hash(coords, elevation);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GeoPosition))
            return false;
        GeoPosition gp = (GeoPosition) o;
        return coords.equals(gp.coords) && elevation == gp.elevation;
    }
}
