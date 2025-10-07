package com.github.sigrarr.lunisolarcalc.coords.global;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.*;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the {@linkplain GlobalCoord#ECLIPTIC_MEAN_OBLIQUITY mean obliquity of the ecliptic (ε0)}.
 * Rather quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CoordsCalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 22 (22.3; pp. 147-148)"
 */
public final class EclipticMeanObliquityCalculator implements Provider<GlobalCoord, TimelinePoint> {

    public static final GlobalCoord SUBJECT = GlobalCoord.ECLIPTIC_MEAN_OBLIQUITY;
    private static final double[] U_COEFFICIENTS_ARCSECONDS = {
        toSingleArcsecondsValue(23, 26, 21.448),
        -4680.93,
        -1.55,
        1999.25,
        -51.38,
        -249.67,
        -39.05,
        7.12,
        27.87,
        5.79,
        2.45
    };

    /**
     * Calculates the {@linkplain GlobalCoord#ECLIPTIC_MEAN_OBLIQUITY mean obliquity of the ecliptic (ε0)}, in radians.
     * Rather quick.
     *
     * @param tx        time argument
     * @return          {@linkplain GlobalCoord#ECLIPTIC_MEAN_OBLIQUITY mean obliquity of the ecliptic (ε0)}, in radians
     */
    public double calculate(TimelinePoint tx) {
        double u = (tx.toDynamicalTime().julianDay - Timeline.EPOCH_2000_JD) / (10 * Timeline.JULIAN_MILLENIUM_DAYS);
        double epsilonZeroArcsec = 0.0;
        for (int p = 0; p < U_COEFFICIENTS_ARCSECONDS.length; p++)
            epsilonZeroArcsec += U_COEFFICIENTS_ARCSECONDS[p] * Math.pow(u, p);
        return Math.toRadians(arcsecondsToDegrees(epsilonZeroArcsec));
    }

    @Override
    public GlobalCoord provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<GlobalCoord> requires() {
        return EnumSet.noneOf(GlobalCoord.class);
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<GlobalCoord, Object> precalculatedValues) {
        return calculate(tx);
    }
}
