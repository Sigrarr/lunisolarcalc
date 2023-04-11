package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;
import java.util.stream.Stream;

import com.github.sigrarr.lunisolarcalc.phenomena.UniversalOccurrence;
import com.github.sigrarr.lunisolarcalc.phenomena.exceptions.PrecisionAngleTooSmallException;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

abstract class DiurnalPhaseFinderAbstract {

    /**
     * Minimal allowed value of {@linkplain #getPrecision() angular precision}, in radians (0.001 arcsecond).
     */
    public static final double MIN_PRECISION_RADIANS = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(0.001));
    /**
     * The default value of {@linkplain #getPrecision() angular precision}, in radians (1 arcsecond).
     */
    public static final double DEFAULT_PRECISION_RADIANS = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(1.0));

    protected final DiurnalPhaseCalcCore core;
    protected double precisionRadians = DEFAULT_PRECISION_RADIANS;

    DiurnalPhaseFinderAbstract(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    public Optional<UniversalOccurrence<BodyDiurnalPhase>> find(CalendarPoint date, GeoCoords geoCoords, DiurnalPhase phase) {
        core.reset(new DiurnalPhaseCalcRequest(date, geoCoords, EnumSet.of(phase), precisionRadians));
        return core.get();
    }

    public Stream<Optional<UniversalOccurrence<BodyDiurnalPhase>>> findMany(CalendarPoint baseDate, GeoCoords geoCoords) {
        return findMany(baseDate, geoCoords, EnumSet.allOf(DiurnalPhase.class));
    }

    public Stream<Optional<UniversalOccurrence<BodyDiurnalPhase>>> findMany(CalendarPoint baseDate, GeoCoords geoCoords, DiurnalPhase phase) {
        return findMany(baseDate, geoCoords, EnumSet.of(phase));
    }

    public Stream<Optional<UniversalOccurrence<BodyDiurnalPhase>>> findMany(CalendarPoint baseDate, GeoCoords geoCoords, Set<DiurnalPhase> phases) {
        core.reset(new DiurnalPhaseCalcRequest(baseDate, geoCoords, phases, precisionRadians));
        return Stream.generate(core);
    }

    /**
     * Gets the angular precision, in radians.
     *
     * This setting determines the results' proximity
     * to the best values achievable by this object's core calculators.
     *
     * While searching, a time argument of a newly calculated value of phase-indicating angle
     * is accepted as a result iff it differs by less than 'angular precision'
     * from the value indicating the phase which is currently under search;
     * otherwise a time argument is corrected, then the phase-indicating angle is recalculated.
     *
     * In the case of transit of the astronomical body, the phase-indicating angle means
     * its local hour angle; in the cases of rise and set it is its altitude.
     *
     * A smaller value implies better precision,
     * but also a higher mean number of core calculations needed to obtain a result.
     *
     * @return  the angular precision value, in radians
     * @see     #DEFAULT_PRECISION_RADIANS
     */
    public double getPrecision() {
        return precisionRadians;
    }

    /**
     * Sets the {@linkplain #getPrecision() angular precision}.
     *
     * @param radians   new value of angular precision, in radians, not lesser than {@value #MIN_PRECISION_RADIANS}
     * @see             #DEFAULT_PRECISION_RADIANS
     */
    public void setPrecision(double radians) {
        validatePrecisionRadians(radians);
        precisionRadians = radians;
    }

    /**
     * Sets the {@linkplain #getPrecision() angular precision}.
     *
     * @param arcseconds    new value of angular precision, in arcseconds, not lesser than {@value #MIN_PRECISION_RADIANS}
     * @see                 #DEFAULT_PRECISION_RADIANS
     */
    public void setPrecisionArcseconds(double arcseconds) {
        setPrecision(Math.toRadians(Calcs.Angle.arcsecondsToDegrees(arcseconds)));
    }

    protected final void validatePrecisionRadians(double radians) {
        if (radians < MIN_PRECISION_RADIANS)
            throw new PrecisionAngleTooSmallException(radians, MIN_PRECISION_RADIANS);
    }
}
