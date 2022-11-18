package com.github.sigrarr.lunisolarcalc.phenomena;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.ROUND;

import com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinder.SeparateCompositionStageIndicatingAngleCalculator;
import com.github.sigrarr.lunisolarcalc.spacebytime.Subject;

public class SunSeasonPointFinder extends SunSeasonPointFinderAbstract {

    public SunSeasonPointFinder() {
        this(new SeparateCompositionStageIndicatingAngleCalculator(Subject.SUN_APPARENT_LONGITUDE));
    }

    public SunSeasonPointFinder(StageIndicatingAngleCalculator sunApparentLongitudeCalculator) {
        super(sunApparentLongitudeCalculator);
    }

    /**
     * Meeus 1998, Ch. 27, p. 180 (customized stop condition)
     */
    @Override
    public double findJulianEphemerisDay(int gregorianYear, SunSeasonPoint point) {
        resetFinding();
        double jde = approximator.approximateJulianEphemerisDay(gregorianYear, point);
        double lambda = calculateSunApparentLongitude(jde);

        while (calculateAbsoluteDiff(point, lambda) > getAngularEpsilon()) {
            jde += calculateJdeCorrection(point, lambda);
            lambda = calculateSunApparentLongitude(jde);
        }

        return jde;
    }

    /**
     * Meeus 1998, 27.1, p. 180
     */
    protected double calculateJdeCorrection(SunSeasonPoint point, double lambda) {
        return 58.0 * Math.sin(point.apparentLongitude - lambda);
    }

    private double calculateAbsoluteDiff(SunSeasonPoint point, double lambda) {
        double diff = point.apparentLongitude - lambda;
        if (point.apparentLongitude == 0.0 && diff < -0.75 * ROUND) {
            diff += ROUND;
        }
        return Math.abs(diff);
    }

    protected double calculateSunApparentLongitude(double julianEphemerisDay) {
        return calculateStageIndicatingAngle(julianEphemerisDay);
    }
}
