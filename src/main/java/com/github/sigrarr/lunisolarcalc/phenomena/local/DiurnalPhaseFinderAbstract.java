package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;
import java.util.stream.Stream;

import com.github.sigrarr.lunisolarcalc.phenomena.UniversalOccurrence;
import com.github.sigrarr.lunisolarcalc.phenomena.exceptions.DiurnalPhaseSearchTooCloseToPeriodBoundaryException;
import com.github.sigrarr.lunisolarcalc.time.calendar.CalendarPoint;
import com.github.sigrarr.lunisolarcalc.time.exceptions.JulianDayOutOfPeriodException;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

abstract class DiurnalPhaseFinderAbstract {
    /**
     * The {@linkplain #getPrecision() angular precision}, in arcseconds ({@value}).
     */
    public static final double PRECISION_ARCSECONDS = GeoCoords.EQUIV_UNIT_ARCSECONDS;
    /**
     * The {@linkplain #getPrecision() angular precision}, in radians (={@value #PRECISION_ARCSECONDS} arcsecond).
     */
    public static final double PRECISION_RADIANS = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(PRECISION_ARCSECONDS));

    protected final DiurnalPhaseCalcCore core;

    DiurnalPhaseFinderAbstract(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    /**
     * Tries to find an occurrence of the requested diurnal phase of the celestial body
     * for the specified geographical position, matching the transit which occurrs at the given local date
     * (expressed in local solar time proper for the given position).
     *
     * See the class' description for details.
     *
     * @param date          date of a matching transit, in local solar time
     *                      proper for the given geographical coordinates
     *                      (preferably with time set to the noon)
     * @param geoCoords     (the observer's) geographical coordinates
     * @param phase         the diurnal phase to look for
     * @return              optional occurrence
     */
    public Optional<UniversalOccurrence<BodyDiurnalPhase>> find(CalendarPoint date, GeoCoords geoCoords, DiurnalPhase phase) {
        try {
            core.reset(new DiurnalPhaseCalcRequest(date, geoCoords, EnumSet.of(phase)));
            return core.get();
        } catch (JulianDayOutOfPeriodException periodException) {
            throw new DiurnalPhaseSearchTooCloseToPeriodBoundaryException(periodException);
        }
    }

    /**
     * Looks for occurrences of any principal diurnal phases of the celestial body
     * for the specified geographical position, starting with the rise matching the transit
     * which occurrs at the given local date (expressed in local solar time proper for the given position);
     * streams the results.
     *
     * See the class' description for details.
     *
     * @param baseDate      date of the transit whose rise will be the first to look for,
     *                      in local solar time proper for the given geographical coordinates
     *                      (preferably with time set to the noon)
     * @param geoCoords     (the observer's) geographical coordinates
     * @return              unterminated {@link Stream} of optional occurrences
     */
    public Stream<Optional<UniversalOccurrence<BodyDiurnalPhase>>> findMany(CalendarPoint baseDate, GeoCoords geoCoords) {
        return findMany(baseDate, geoCoords, EnumSet.allOf(DiurnalPhase.class));
    }

    /**
     * Looks for occurrences of the requested diurnal phase of the celestial body
     * for the specified geographical position, starting with the one matching the transit
     * which occurrs at the given local date (expressed in local solar time proper for the given position);
     * streams the results.
     *
     * See the class' description for details.
     *
     * @param baseDate      date of the transit matching the first occurrence to look for,
     *                      in local solar time proper for the given geographical coordinates
     *                      (preferably with time set to the noon)
     * @param geoCoords     (the observer's) geographical coordinates
     * @param phase         diurnal phase to look for
     * @return              unterminated {@link Stream} of optional occurrences
     */
    public Stream<Optional<UniversalOccurrence<BodyDiurnalPhase>>> findMany(CalendarPoint baseDate, GeoCoords geoCoords, DiurnalPhase phase) {
        return findMany(baseDate, geoCoords, EnumSet.of(phase));
    }

    /**
     * Looks for occurrences of the requested diurnal phases of the celestial body
     * for the specified geographical position, starting with one from the group of the transit
     * which occurrs at the given local date (expressed in local solar time proper for the given position);
     * streams the results.
     *
     * See the class' description for details.
     *
     * @param baseDate      date of the transit matching the first occurrence to look for,
     *                      in local solar time proper for the given geographical coordinates
     *                      (preferably with time set to the noon)
     * @param geoCoords     (the observer's) geographical coordinates
     * @param phases        diurnal phases to look for
     * @return              unterminated {@link Stream} of optional occurrences
     */
    public Stream<Optional<UniversalOccurrence<BodyDiurnalPhase>>> findMany(CalendarPoint baseDate, GeoCoords geoCoords, Set<DiurnalPhase> phases) {
        try {
            core.reset(new DiurnalPhaseCalcRequest(baseDate, geoCoords, phases));
            return Stream.generate(core);
        } catch (JulianDayOutOfPeriodException periodException) {
            throw new DiurnalPhaseSearchTooCloseToPeriodBoundaryException(periodException);
        }
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
     * @return  the angular precision value, in radians
     * @see     #PRECISION_RADIANS
     */
    public double getPrecision() {
        return PRECISION_RADIANS;
    }
}
