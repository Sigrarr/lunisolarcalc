package com.github.sigrarr.lunisolarcalc.space.periodicterms;

public class PeriodicTermsForEarthRadius extends PeriodicTermsForHeliocentricCoordinate {
    protected static final double[][][] SERIES_ARRAY = {
        {

        },
        {

        },
        {

        },
        {

        },
        {

        }
    };

    public int getSeriesCount() {
        return SERIES_ARRAY.length;
    }

    protected double[][] getSeries(int n) {
        return SERIES_ARRAY[n];        
    }    
}
