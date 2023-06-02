package com.github.sigrarr.lunisolarcalc.time.timescaledelta;

import static com.github.sigrarr.lunisolarcalc.time.timescaledelta.Util.*;

import com.github.sigrarr.lunisolarcalc.time.*;

/**
 * A tool for approximating ΔT = {@linkplain TimeScale TT - UT},
 * adapting the method by {@literal Morrison & Stephenson (2004)}.
 * It is fit for years from -700 to +2000, but can be applied outside this interval
 * with reasonable accuracy (in greater extent to the past than to the future).
 * It does not provide values of high accuracy for contemporary times.
 *
 * Remember that ΔT bears serious uncertainty, regarding both the future and the distant past.
 * This tool does not round yielded values to any arbitrary precision;
 * it does not reflect the results' accuracy, but only the design goal
 * that ΔT should change in time continuously, which prevents contradictional behaviour
 * (sudden changes of discrete values of ΔT would break time order in some cases,
 * i.e. cause situations in which {@literal UT1 < UT2 but TT(UT1) ≥ TT(UT2)}).
 *
 * The referenced work assigns ΔT values to certain years; this tool
 * takes those points as the years' beginnings in Universal Time
 * and performs linear interpolation between them.
 * {@literal Morrison & Stephenson} give also a parabolic formula to be used for extrapolation
 * of ΔT before -700 and after +2000; it is applied by this resolver too,
 * but with a slight modification: values for (start of) -710 and +2001 are precalculated
 * and assigned as if they belonged to the base points, so ΔT for intervals
 * [-710-01-01 00:00, -700-01-01 00:00] and [+2000-01-01 00:00, +2001-01-01 00:00]
 * will be resolved linearly in order to smoothen the transition between the scopes.
 *
 * @see "Morrison & Stephenson 2004, esp.: Table 1 (p. 332); p 335"
 */
public final class BasisMinus700ToPlus2000Resolver implements TimeScaleDelta.Resolver {

    static final double EPOCH_1820_JD = yearStartToJulianDay(1820);
    static final double DELTA_T_QUADRATIC_COEF_SECONDS_PER_CENTURY_SQUARED = 32.0;
    static final double DELTA_T_FREE_COEF_SECONDS = -20.0;

    @Override
    public double resolveDeltaTSeconds(double julianDay, TimeScale argumentTimeScale) {
        double[] jdAxisPoints = Table.getJdAxisPoints(argumentTimeScale);
        int floorIndex = Table.tryFindFloorIndex(julianDay, jdAxisPoints);
        if (floorIndex != Table.OUT) {
            return interpolateLinearly(julianDay, jdAxisPoints, floorIndex);
        }

        double centurialVector = julianDayToCenturialVector(julianDay);
        if (argumentTimeScale == TimeScale.DYNAMICAL) {
            centurialVector = convertToUniversalTimeParabolically(centurialVector);
        }
        return extrapolateParabolically(centurialVector);
    }

    static double interpolateLinearly(double julianDay, double[] jdAxisPoints, int floorIndex) {
        int ceilIndex = floorIndex + 1;
        double relativePosition = (julianDay - jdAxisPoints[floorIndex]) / (jdAxisPoints[ceilIndex] - jdAxisPoints[floorIndex]);
        return Table.DELTA_T[floorIndex] + relativePosition * (Table.DELTA_T[ceilIndex] - Table.DELTA_T[floorIndex]);
    }

    static double extrapolateParabolically(double centurialVector) {
        return DELTA_T_QUADRATIC_COEF_SECONDS_PER_CENTURY_SQUARED * centurialVector * centurialVector
            + DELTA_T_FREE_COEF_SECONDS;
    }

    static double convertToUniversalTimeParabolically(double centurialVectorTT) {
        // UT + ΔT = TT  =>  UT + aUT^2 + c = TT  =>  aUT^2 + UT + c - TT = 0
        double qA = DELTA_T_QUADRATIC_COEF_SECONDS_PER_CENTURY_SQUARED*SECOND_TO_CENTURY;
        double qC = DELTA_T_FREE_COEF_SECONDS*SECOND_TO_CENTURY - centurialVectorTT;
        double qDRoot = Math.sqrt(1.0 - 4.0 * qA * qC);
        double centurialVectorUT = (-2.0 * qC) / (1.0 + qDRoot);

        return centurialVectorUT;
    }
}
