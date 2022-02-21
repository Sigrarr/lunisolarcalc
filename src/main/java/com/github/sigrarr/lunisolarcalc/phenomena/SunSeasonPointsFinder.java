package com.github.sigrarr.lunisolarcalc.phenomena;

import com.github.sigrarr.lunisolarcalc.phenomena.sunseasonpointsfinder.*;
import com.github.sigrarr.lunisolarcalc.phenomena.tables.MeanSunSeasonPointApproximationTable;
import com.github.sigrarr.lunisolarcalc.util.MeanValueApproximations.SunEarthRelativeMotion;;

public class SunSeasonPointsFinder {

    public static interface ApparentLongitudeCalculator {
        public double calculateLambda(double julianEphemerisDay);
    }

    public static final int DEFAULT_MEAN_PRECISION_SECONDS = 15;
    private final ApparentLongitudeCalculator lambdaCalculator;
    private final MeanSunSeasonPointApproximationTable approximationTable = new MeanSunSeasonPointApproximationTable();
    private int lastLambdaCalculationIterations = -1;

    public SunSeasonPointsFinder() {
        this(new SeparateCompositionApparentLongitudeCalculator());
    }    

    public SunSeasonPointsFinder(ApparentLongitudeCalculator lambdaCalculator) {
        this.lambdaCalculator = lambdaCalculator;
    }

    public double findJulianEphemerisDay(int romanYear, SunSeasonPoint point) {
        return findJulianEphemerisDay(romanYear, point, DEFAULT_MEAN_PRECISION_SECONDS);
    }

    /**
     * Meeus 1998, Ch. 27, p. 180 (customized stop condition)
     */
    public double findJulianEphemerisDay(int romanYear, SunSeasonPoint point, int meanPrecisionSeconds) {
        double meanPrecisionDegrees = SunEarthRelativeMotion.degreesPerTimeMiliseconds(1000 * meanPrecisionSeconds);
        double correctionSinArgumentMinuend = 0.5 * Math.PI * point.ordinal();

        double jde = approximationTable.evaluate(romanYear, point);
        double lambda = lambdaCalculator.calculateLambda(jde);

        lastLambdaCalculationIterations = 1;
        while (Math.toDegrees(Math.abs(lambda - point.apparentLongitude)) > meanPrecisionDegrees) {
            jde += calculateJDECorrection(correctionSinArgumentMinuend, lambda);
            lambda = lambdaCalculator.calculateLambda(jde);
            lastLambdaCalculationIterations++;
        }

        return jde;
    }

    /**
     * Meeus 1998, 27.1, p. 180
     */
    private double calculateJDECorrection(double correctionSinArgumentMinuend, double lambda) {
        return 58.0 * Math.sin(correctionSinArgumentMinuend - lambda);
    }

    protected int getLastLambdaCalculationsCount() {
        if (lastLambdaCalculationIterations < 0) {
            throw new IllegalStateException();
        }
        return lastLambdaCalculationIterations;
    }
}
