package com.github.sigrarr.lunisolarcalc.coords.periodicterms;

/**
 * Periodic terms for the Earth's radius vector (R).
 * Results are in AU.
 *
 * @see com.github.sigrarr.lunisolarcalc.coords.EarthSunRadiusCalculator
 * @see "Meeus 1998: App. III, Earth, R0-R4 (pp. 420-421)"
 */
public final class EarthSunRadiusPeriodicTerms extends HeliocentricCoordinatePeriodicTerms {
    protected static final double[][][] SERIES_ARRAY = {
        {
            { 100013989.0 , 0.0 , 0.0 },
            { 1670700.0 , 3.0984635 , 6283.07585 },
            { 13956.0 , 3.05525 , 12566.1517 },
            { 3084.0 , 5.1985 , 77713.7715 },
            { 1628.0 , 1.1739 , 5753.3849 },
            { 1576.0 , 2.8469 , 7860.4194 },
            { 925.0 , 5.453 , 11506.77 },
            { 542.0 , 4.564 , 3930.21 },
            { 472.0 , 3.661 , 5884.927 },
            { 346.0 , 0.964 , 5507.553 },
            { 329.0 , 5.9 , 5223.694 },
            { 307.0 , 0.299 , 5573.143 },
            { 243.0 , 4.273 , 11790.629 },
            { 212.0 , 5.847 , 1577.344 },
            { 186.0 , 5.022 , 10977.079 },
            { 175.0 , 3.012 , 18849.228 },
            { 110.0 , 5.055 , 5486.778 },
            { 98.0 , 0.89 , 6069.78 },
            { 86.0 , 5.69 , 15720.84 },
            { 86.0 , 1.27 , 161000.69 },
            { 65.0 , 0.27 , 17260.15 },
            { 63.0 , 0.92 , 529.69 },
            { 57.0 , 2.01 , 83996.85 },
            { 56.0 , 5.24 , 71430.7 },
            { 49.0 , 3.25 , 2544.31 },
            { 47.0 , 2.58 , 775.52 },
            { 45.0 , 5.54 , 9437.76 },
            { 43.0 , 6.01 , 6275.96 },
            { 39.0 , 5.36 , 4694 },
            { 38.0 , 2.39 , 8827.39 },
            { 37.0 , 0.83 , 19651.05 },
            { 37.0 , 4.9 , 12139.55 },
            { 36.0 , 1.67 , 12036.46 },
            { 35.0 , 1.84 , 2942.46 },
            { 33.0 , 0.24 , 7084.9 },
            { 32.0 , 0.18 , 5088.63 },
            { 32.0 , 1.78 , 398.15 },
            { 28.0 , 1.21 , 6286.6 },
            { 28.0 , 1.9 , 6279.55 },
            { 26.0 , 4.59 , 10447.39 },
        },
        {
            { 103019.0 , 1.10749 , 6283.07585 },
            { 1721.0 , 1.0644 , 12566.1517 },
            { 702.0 , 3.142 , 0.0 },
            { 32.0 , 1.02 , 18849.23 },
            { 31.0 , 2.84 , 5507.55 },
            { 25.0 , 1.32 , 5223.69 },
            { 18.0 , 1.42 , 1577.34 },
            { 10.0 , 5.91 , 10977.08 },
            { 9.0 , 1.42 , 6275.96 },
            { 9.0 , 0.27 , 5486.78 },
        },
        {
            { 4359.0 , 5.7846 , 6283.0758 },
            { 124.0 , 5.579 , 12566.152 },
            { 12.0 , 3.14 , 0.0 },
            { 9.0 , 3.63 , 77713.77 },
            { 6.0 , 1.87 , 5573.14 },
            { 3.0 , 5.47 , 18849.23 },
        },
        {
            { 145.0 , 4.273 , 6283.076 },
            { 7.0 , 3.92 , 12566.15 },
        },
        {
            { 4.0 , 2.56 , 6283.08 },
        }
    };

    @Override
    public int getNumberOfSeries() {
        return SERIES_ARRAY.length;
    }

    @Override
    protected double[][] getSeries(int n) {
        return SERIES_ARRAY[n];
    }
}
