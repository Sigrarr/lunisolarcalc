package com.github.sigrarr.lunisolarcalc.phenomena;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.TURN;

import com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinders.*;
import com.github.sigrarr.lunisolarcalc.coords.*;

/**
 * A tool for finding occurrences of Equinoxes/Solstices, i.e. distinguished stages of the tropical year cycle
 * - whose stage-indicating angle is the Sun's apparent longitude (λ).
 *
 * Internally, works 'by definition' - driven by a core calculator of λ:
 * starts with an initial {@linkplain SunSeasonPointApproximator time approximation} - t,
 * then (re)calculates λ(t) and corrects t until the value λ(t) is close enough to the specific for the stage under search.
 *
 * Uses Meeus' method for time correction.
 * By default utilizes a {@link SunApparentLongitudeCalculator} composed with {@link CalcCompositions}.
 * You can {@linkplain #SunSeasonPointFinder(StageIndicatingAngleCalculator) use another λ calculator}
 * and set custom value of angular delta for comparing values of λ.
 *
 * @see "Meeus 1998: Ch. 27 ("Of course, higher accuracy...", p. 180)"
 */
public final class SunSeasonPointFinder extends SunSeasonPointFinderAbstract {
    /**
     * Constructs an instance which will use the default calculator of the Sun's apparent longitude (λ),
     * prepared with {@link CalcCompositions}.
     */
    public SunSeasonPointFinder() {
        this(new OwnCompositionStageIndicatingAngleCalculator(Subject.SUN_APPARENT_LONGITUDE));
    }

    /**
     * Constructs an instance with a custom calculator of the Sun's apparent longitude (λ).
     * Results' accuracy will obviously depend on the passed calculator.
     *
     * @param sunApparentLongitudeCalculator    calculator of the Sun's apparent longitude (λ)
     */
    public SunSeasonPointFinder(StageIndicatingAngleCalculator sunApparentLongitudeCalculator) {
        super(sunApparentLongitudeCalculator);
    }

    @Override
    public double findJulianEphemerisDay(int calendarYear, SunSeasonPoint point) {
        resetFinding();
        double jde = approximator.approximateJulianEphemerisDay(calendarYear, point);
        double lambda = calculateStageIndicatingAngle(jde);

        while (calculateAbsoluteDiff(point, lambda) > getAngularDelta()) {
            jde += calculateJdeCorrection(point, lambda);
            lambda = calculateStageIndicatingAngle(jde);
        }

        return jde;
    }

    private double calculateJdeCorrection(SunSeasonPoint point, double lambda) {
        return 58.0 * Math.sin(point.apparentLongitude - lambda);
    }

    private double calculateAbsoluteDiff(SunSeasonPoint point, double lambda) {
        double diff = point.apparentLongitude - lambda;
        if (point.apparentLongitude == 0.0 && diff < -0.75 * TURN) {
            diff += TURN;
        }
        return Math.abs(diff);
    }
}
