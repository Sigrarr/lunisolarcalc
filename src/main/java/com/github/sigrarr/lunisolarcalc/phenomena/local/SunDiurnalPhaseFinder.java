package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.coords.Subject;

/**
 * A tool for finding occurrences of the Sun's {@linkplain DiurnalPhase principal diurnal phases}.
 *
 * Notes:
 *  •   Phases are processed in groups consisting of a rise, transit and set. \
 *  •   A group is associated with a date by its central phase - the transit (solar noon).
 *      Any method's input date is meant to specify a transit which occurs at that date.
 *      Even if you need to obtain an occurrence of an extreme phase (rise or set),
 *      you have to provide the expected date of its group central transit.
 *      In some cases a rise may be found at the preceding date, or set - at the following. \
 *  •   Input dates should be given in the local solar time proper for the observer's
 *      geographical coordinates. It is more important for extreme longitudes than for moderate ones.
 *      The difference between the local solar time and the official zone-time may often be neglected. \
 *  •   In circumpolar locations one may encounter a polar night or a polar day.
 *      During those periods there are no rises nor sets, every optional occurrence
 *      of an extreme phase will be empty (only transits will be present). \
 *  •   The algorithm takes the atmospheric refraction into account by a simple mean
 *      - assuming the "standard altitude" of −0°50′ (following Meeus), i.e. the geometric altitude
 *      of the Sun-disk's center at the time of its apparent rise or set.
 *      The {@linkplain Subject#ECLIPTIC_TRUE_OBLIQUITY obliquity of the ecliptic}
 *      and {@linkplain Subject#EARTH_NUTUATION_IN_LONGITUDE the Earth's nutuation}
 *      are taken into account. Factors such as the observer's elevation and subtle air parameters
 *      are neglected. \
 *  •   Usually the results should not be off by more than a minute of time, but they have to be taken
 *      with caution in circumpolar scenarios, especially near a beginning or end of a polar day or night;
 *      then error may reach several minutes and one should not exclude the possibility of a false presence
 *      or false absence of rise or set (however, it did not happen even once during testing). \
 *
 * The tool is loosly based on the algorithm by Meeus, but modified and expanded with some original
 * detailed logic to be more reliable.
 *
 * @see "Meeus 1998: Ch. 15, pp. 101-104"
 */
public final class SunDiurnalPhaseFinder extends DiurnalPhaseFinderAbstract {
    public SunDiurnalPhaseFinder() {
        super(new SunDiurnalPhaseCalcCore());
    }
}
