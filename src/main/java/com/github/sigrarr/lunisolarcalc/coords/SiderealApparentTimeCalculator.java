package com.github.sigrarr.lunisolarcalc.coords;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Calculator of the apparent sidereal time at the Greenwich meridian (θ0).
 * Given required parameters, it's in itself quick.
 * Stateless, {@linkplain CalculationComposer composable}, pre-registered in {@link CalcCompositions}.
 *
 * @see "Meeus 1998: Ch. 12 (pp. 87-89)"
 */
public class SiderealApparentTimeCalculator implements Provider<Subject, TimelinePoint> {

    public static final Subject SUBJECT = Subject.SIDEREAL_APPARENT_TIME;

    /**
     * Calculates the apparent sidereal time at the Greenwich meridian (θ0),
     * in degrees: [0, 360°). 15° corresponds to 1 hour.
     *
     * @param meanSiderealTimeDegrees   {@linkplain SiderealMeanTimeCalculator mean sidereal time (θ0)}, in degrees
     * @param earthNutuationInLongitude {@linkplain EarthNutuationInLongitudeCalculator the Earth's nutuation in longitude (Δψ)}, in radians
     * @param eclipticObliquity         {@linkplain EclipticTrueObliquityCalculator true obliquity of the ecliptic (ε)}, in radians
     * @return                          the apparent sidereal time at the Greenwich meridian (θ0),
     *                                  in degrees: [0, 360°)
     */
    public double calculate(double meanSiderealTimeDegrees, double earthNutuationInLongitude, double eclipticObliquity) {
        return Calcs.Angle.normalizeLongitudinally(
            meanSiderealTimeDegrees + calculateNutuationInRightAscensionDegrees(earthNutuationInLongitude, eclipticObliquity),
            360.0
        );
    }

    protected double calculateNutuationInRightAscensionDegrees(double deltaPsi, double epsilon) {
        return Math.toDegrees(deltaPsi * Math.cos(epsilon));
    }

    @Override
    public Subject provides() {
        return SUBJECT;
    }

    @Override
    public EnumSet<Subject> requires() {
        return EnumSet.of(
            Subject.SIDEREAL_MEAN_TIME,
            Subject.EARTH_NUTUATION_IN_LONGITUDE,
            Subject.ECLIPTIC_TRUE_OBLIQUITY
        );
    }

    @Override
    public Double calculate(TimelinePoint tx, Map<Subject, Object> precalculatedValues) {
        return calculate(
            (Double) precalculatedValues.get(Subject.SIDEREAL_MEAN_TIME),
            (Double) precalculatedValues.get(Subject.EARTH_NUTUATION_IN_LONGITUDE),
            (Double) precalculatedValues.get(Subject.ECLIPTIC_TRUE_OBLIQUITY)
        );
    }
}
