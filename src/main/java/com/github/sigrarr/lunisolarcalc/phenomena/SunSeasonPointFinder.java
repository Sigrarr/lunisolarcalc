package com.github.sigrarr.lunisolarcalc.phenomena;

import com.github.sigrarr.lunisolarcalc.phenomena.sunseasonpointfinder.*;

public class SunSeasonPointFinder extends SunSeasonPointFinderAbstract {

    public static interface ApparentLongitudeCalculator {
        public double calculateLambda(double julianEphemerisDay);
    }

    private final ApparentLongitudeCalculator lambdaCalculator;
    private final MeanSunSeasonPointApproximator approximator = new MeanSunSeasonPointApproximator();
    private int lastLambdaCalculationCount = -1;

    public SunSeasonPointFinder() {
        this(new SeparateCompositionApparentLongitudeCalculator());
    }    

    public SunSeasonPointFinder(ApparentLongitudeCalculator lambdaCalculator) {
        this.lambdaCalculator = lambdaCalculator;
    }

    /**
     * Meeus 1998, Ch. 27, p. 180 (customized stop condition)
     */
    @Override
    protected double findJulianEphemerisDay(int romanYear, SunSeasonPoint point, double meanPrecisionRadians) {
        double jde = approximator.approximateJulianEphemerisDay(romanYear, point);
        double lambda = lambdaCalculator.calculateLambda(jde);
        lastLambdaCalculationCount = 1;

        while (Math.abs(lambda - point.apparentLongitude) > meanPrecisionRadians) {
            jde += calculateJDECorrection(point, lambda);
            lambda = lambdaCalculator.calculateLambda(jde);
            lastLambdaCalculationCount++;
        }

        return jde;
    }

    /**
     * Meeus 1998, 27.1, p. 180
     */
    protected double calculateJDECorrection(SunSeasonPoint point, double lambda) {
        return 58.0 * Math.sin(point.apparentLongitude - lambda);
    }

    public int getLastLambdaCalculationsCount() {
        if (lastLambdaCalculationCount < 0) {
            throw new IllegalStateException();
        }
        return lastLambdaCalculationCount;
    }
}
