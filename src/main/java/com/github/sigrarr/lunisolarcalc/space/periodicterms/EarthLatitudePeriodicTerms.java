package com.github.sigrarr.lunisolarcalc.space.periodicterms;

/**
 * Meeus 1998, App. III, Earth, B0-B1, p. 420
 */
public final class EarthLatitudePeriodicTerms extends HeliocentricCoordinatePeriodicTerms {
    protected static final double[][][] SERIES_ARRAY = {
        {
            { 280.0 , 3.199 , 84334.662 },
            { 102.0 , 5.422 , 5507.553 },
            { 80.0 , 3.88 , 5223.69 },
            { 44.0 , 3.7 , 2352.87 },
            { 32.0, 4.0 , 1577.34 },
        },
        {
            { 9.0 , 3.9 , 5507.55 },
            { 6.0 , 1.73 , 5223.69 },
        }
    };

    public int getSeriesCount() {
        return SERIES_ARRAY.length;
    }

    protected double[][] getSeries(int n) {
        return SERIES_ARRAY[n];        
    }
}
