package com.github.sigrarr.lunisolarcalc.spacebytime.periodicterms;

/**
 * Periodic terms for Earth's heliocentric latitude (B).
 * Results are in radians.
 *
 * @see com.github.sigrarr.lunisolarcalc.spacebytime.EarthLatitudeCalculator
 * @see "Meeus 1998: App. III, Earth, B0-B1 (p. 420)"
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

    @Override
    protected int getNumberOfSeries() {
        return SERIES_ARRAY.length;
    }

    @Override
    protected double[][] getSeries(int n) {
        return SERIES_ARRAY[n];
    }
}
