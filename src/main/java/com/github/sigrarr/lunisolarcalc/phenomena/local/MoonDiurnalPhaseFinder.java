package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.coords.Subject;

/**
 * A tool for finding occurrences of the Moon's {@linkplain DiurnalPhase principal diurnal phases}.
 *
 * Notes:
 *  •   Phases are processed in groups consisting of a rise, transit and set. \
 *  •   A group is associated with a date by its central phase - the transit.
 *      Any method's input date is meant to specify a transit which occurs at that date.
 *      Even if you need to obtain an occurrence of an extreme phase (rise or set),
 *      you have to provide the expected date of its group central transit.
 *      A rise will often be found at the preceding date, and set - at the following. \
 *  •   Input dates should be given in the local solar time proper for the observer's
 *      geographical coordinates. \
 *  •   Near a Full Moon transits occur close to midnight; since the mean time between
 *      subsequent transits is a bit longer than one day, it happens on a regular basis
 *      that there is a date without the Moon's transit. It means that for such date
 *      the entire rise-transit-set group is missing. Keep it in mind while handling the results:
 *      a transitless group will not be omitted but reflected by empty optional occurrences. \
 *  •   In circumpolar locations one may encounter a period longer than one day when the Moon is constantly
 *      over, or under, the horizon; there are no rises nor sets and every optional occurrence
 *      of an extreme phase will be empty. \
 *  •   The algorithm takes the atmospheric refraction into account by a simple mean
 *      - assuming the "standard altitude" formula:
 *      h0 = 0.7275 {@linkplain Subject#MOON_EQUATORIAL_HORIZONTAL_PARALLAX π} − 0°34′ (following Meeus),
 *      where h0 is the geometric altitude of the Moon-disk's center at the time of its apparent rise or set.
 *      The {@linkplain Subject#ECLIPTIC_TRUE_OBLIQUITY obliquity of the ecliptic}
 *      and {@linkplain Subject#EARTH_NUTUATION_IN_LONGITUDE the Earth's nutuation}
 *      are taken into account. Factors such as the observer's elevation and subtle air parameters
 *      are neglected. \
 *  •   Usually the results should not be off by more than a minute of time, but they have to be taken
 *      with caution near a Full Moon and in circumpolar scenarios, especially near a border of a period
 *      when the Moon does not cross the horizon; in such circumstances error may be greater and
 *      one should not exclude the possibility of a false presence or false absence of a phase
 *      (however, it did not happen during testing). \
 *
 * The tool is loosly based on the algorithm by Meeus, but modified and expanded with some original
 * detailed logic to be more reliable.
 *
 * @see "Meeus 1998: Ch. 15, pp. 101-104"
 */
public final class MoonDiurnalPhaseFinder extends DiurnalPhaseFinderAbstract {
    public MoonDiurnalPhaseFinder() {
        super(new MoonDiurnalPhaseCalcCore());
    }
}
