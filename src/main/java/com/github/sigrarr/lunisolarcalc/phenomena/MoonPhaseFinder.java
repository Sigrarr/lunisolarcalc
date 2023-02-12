package com.github.sigrarr.lunisolarcalc.phenomena;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.TURN;

import com.github.sigrarr.lunisolarcalc.phenomena.cyclicphenomenonfinders.*;
import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.util.*;

/**
 * A tool for finding occurrences of principal phases of the Moon, i.e. distinguished stages of the lunation cycle
 * - whose stage-indicating angle is excess of the Moon's apparent longitude over the Sun's apparent longitude.
 *
 * Internally, works 'by definition' - driven by a core calculator of stage-indicating angle:
 * starts with an initial {@linkplain MoonPhaseApproximator time approximation} - t, then (re)calculates the excess for t
 * and corrects t until the value of excess is close enough to the specific for the phase under search.
 *
 * Uses an original method for time correction.
 * By default utilizes a {@link MoonOverSunApparentLongitudeExcessCalculator} composed with {@link CoordsCalcCompositions}.
 * You can {@linkplain #MoonPhaseFinder(StageIndicatingAngleCalculator) use another excess calculator}
 * and set custom value of angular delta for comparing values of excess.
 *
 * @see "Seidelmann 1992: Ch. 9 by B.D. Yallop & C.Y. Hohenkerk, 9.213 (p. 478)"
 * @see "Meeus 1998: Ch. 49, p. 349"
 */
public final class MoonPhaseFinder extends MoonPhaseFinderAbstract {

    private DoubleStepPair excess = new DoubleStepPair();
    private DoubleStepPair jde = new DoubleStepPair();
    private double diff;

    /**
     * Constructs an instance which will use the default calculator of excess
     * of the Moon's apparent longitude over the Sun's apparent longitude,
     * prepared with {@link CoordsCalcCompositions}.
     */
    public MoonPhaseFinder() {
        this(new OwnCompositionStageIndicatingAngleCalculator(Subject.MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS));
    }

    /**
     * Constructs an instance with a custom calculator of excess
     * of the Moon's apparent longitude over the Sun's apparent longitude.
     * Results' accuracy will obviously depend on the passed calculator.
     *
     * @param moonOverSunLambdaExcessCalculator     calculator of excess of the Moon's apparent longitude
     *                                              over the Sun's apparent longitude
     */
    public MoonPhaseFinder(StageIndicatingAngleCalculator moonOverSunLambdaExcessCalculator) {
        super(moonOverSunLambdaExcessCalculator);
    }

    @Override
    protected double findJulianEphemerisDay(double approximateJde, MoonPhase phase) {
        resetFinding();
        jde.push(approximateJde);
        excess.push(calculateMoonOverSunLambdaExcess());
        setDiffAndExcessProjectingOnContinuousLine(phase);

        while (Math.abs(diff) > getAngularDelta()) {
            jde.push(jde.getCurrent() + calculateJdeCorrection());
            excess.push(calculateMoonOverSunLambdaExcess());
            setDiffAndExcessProjectingOnContinuousLine(phase);
        }

        return jde.getCurrent();
    }

    private double calculateMoonOverSunLambdaExcess() {
        return calculateStageIndicatingAngle(jde.getCurrent());
    }

    private double calculateJdeCorrection() {
        return diff * (excess.hasTwoValues() ? estimateSlopeInverseFromRecentEvaluations() : MeanCycle.LUNATION.epochalLengthDays / TURN);
    }

    private double estimateSlopeInverseFromRecentEvaluations() {
        return (jde.getCurrent() - jde.getPrevious()) / (excess.getCurrent() - excess.getPrevious());
    }

    private void setDiffAndExcessProjectingOnContinuousLine(MoonPhase phase) {
        diff = phase.moonOverSunApparentLongitudeExcess - excess.getCurrent();
        if (phase == MoonPhase.NEW_MOON && diff < -0.75 * TURN) {
            projectExcessFromNearTurnAndDiffFromNearNegativeTurnToNearZero();
        }
    }

    private void projectExcessFromNearTurnAndDiffFromNearNegativeTurnToNearZero() {
        excess.setCurrent(excess.getCurrent() - TURN);
        diff += TURN;
    }

    @Override
    protected void resetFinding() {
        super.resetFinding();
        jde.clear();
        excess.clear();
    }
}
