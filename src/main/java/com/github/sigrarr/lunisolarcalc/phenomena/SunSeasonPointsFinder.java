package com.github.sigrarr.lunisolarcalc.phenomena;

import com.github.sigrarr.lunisolarcalc.phenomena.sunseasonpointsfinder.*;

public class SunSeasonPointsFinder extends SunSeasonPointsFinderAbstract {

    public static interface ApparentLongitudeCalculator {
        public double calculateLambda(double julianEphemerisDay);
    }

    private final ApparentLongitudeCalculator lambdaCalculator;
    private final MeanSunSeasonPointApproximator approximator = new MeanSunSeasonPointApproximator();
    private int lastLambdaCalculationIterations = -1;

    public SunSeasonPointsFinder() {
        this(new SeparateCompositionApparentLongitudeCalculator());
    }    

    public SunSeasonPointsFinder(ApparentLongitudeCalculator lambdaCalculator) {
        this.lambdaCalculator = lambdaCalculator;
    }

    /**
     * Meeus 1998, Ch. 27, p. 180 (customized stop condition)
     */
    @Override
    protected double findJulianEphemerisDay(int romanYear, SunSeasonPoint point, double meanPrecisionDegrees) {
        double jde = approximator.approximateJulianEphemerisDay(romanYear, point);
        double lambda = lambdaCalculator.calculateLambda(jde);

        lastLambdaCalculationIterations = 1;
        while (Math.toDegrees(Math.abs(lambda - point.apparentLongitude)) > meanPrecisionDegrees) {
            jde += calculateJDECorrection(point, lambda);
            lambda = lambdaCalculator.calculateLambda(jde);
            lastLambdaCalculationIterations++;
        }

        return jde;
    }

    /**
     * Meeus 1998, 27.1, p. 180
     */
    protected double calculateJDECorrection(SunSeasonPoint point, double lambda) {
        return 58.0 * Math.sin(point.apparentLongitude - lambda);
    }

    protected int getLastLambdaCalculationsCount() {
        if (lastLambdaCalculationIterations < 0) {
            throw new IllegalStateException();
        }
        return lastLambdaCalculationIterations;
    }
}
