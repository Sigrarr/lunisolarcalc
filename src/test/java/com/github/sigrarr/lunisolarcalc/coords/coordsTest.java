package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sigrarr.lunisolarcalc.coords.global.GlobalCoord.*;
import static com.github.sigrarr.lunisolarcalc.coords.local.LocalCoord.*;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.toSingleDegreesValue;
import static com.github.sigrarr.lunisolarcalc.util.Calcs.Time.timeToDays;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.coords.global.GlobalCoord;
import com.github.sigrarr.lunisolarcalc.subjects.GeoPosition;
import com.github.sigrarr.lunisolarcalc.testing.ExampleLocation;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.MultiCalcComposition;

/**
 * Special class for testing results produced in composition
 * by multiple classes of this package.
 *
 * In most cases we don't need an exact match with reference data.
 */
public class coordsTest {

    // Referenced data is to be converted to radians from...
    static final double DEG = Calcs.TURN/360;
    static final double TURN = Calcs.TURN;
    // Precision requirement is arbitrary or implied by referenced data?
    static final PrecisionOrigin A = PrecisionOrigin.ARBITRARY;
    static final PrecisionOrigin D = PrecisionOrigin.DATA;

    static final List<Example<GlobalCoord>> GLOBAL_EXAMPLES = Arrays.asList(
        // https://aa.usno.navy.mil/calculated/positions/geocentric?ID=AA&task=6&body=10&date=1800-01-01&time=12%3A00%3A00.000&intv_mag=1.00&intv_unit=4&reps=1&submit=Get+Data
        new Example<>(1, UniversalTimelinePoint.ofCalendaricParameters(1800, 1, 1.5), Arrays.asList(
            v(
                SUN_RIGHT_ASCENSION,
                timeToDays(18, 47, 35.63),
                TURN, 0.1 * Calcs.SECOND_TO_DAY, A
            ),
            v(
                SUN_DECLINATION,
                toSingleDegreesValue(-23, 0, 57.85),
                DEG, Calcs.ARCSECOND_TO_DEGREE, A
            )
        )),
        // https://aa.usno.navy.mil/calculated/positions/geocentric?ID=AA&task=6&body=11&date=1900-06-21&time=21%3A39%3A00.000&intv_mag=0.1&intv_unit=4&reps=1&submit=Get+Data
        new Example<>(2, UniversalTimelinePoint.ofCalendaricParameters(1900,  6, 21, 21, 39, 00), Arrays.asList(
            v(
                MOON_EARTH_DISTANCE,
                371520.516,
                1.0, 0.5, A
            ),
            v(
                MOON_RIGHT_ASCENSION,
                timeToDays(1, 25, 22.043),
                TURN, 0.1 * Calcs.SECOND_TO_DAY, A
            ),
            v(
                MOON_DECLINATION,
                toSingleDegreesValue(+13,  4, 13.63),
                DEG, Calcs.ARCSECOND_TO_DEGREE, A
            )
        ))
    );

    static final List<LocalExample> LOCAL_EXAMPLES = Arrays.asList(
        // https://aa.usno.navy.mil/calculated/altaz?body=10&date=1900-04-01&intv_mag=1&lat=55.6000&lon=11.9667&label=Lejre&tz=0.00&tz_sign=1&submit=Get+Data
        new LocalExample(3, GeoPosition.of(ExampleLocation.LEJRE), UniversalTimelinePoint.ofCalendaricParameters(1900,  4,  1, 17, 50, 0), Arrays.asList(
            v(
                SUN_ALTITUDE.key(),
                -1.0,
                DEG, 0.05, D
            ),
            v(
                SUN_AZIMUTH.key(),
                279.6 - 180,
                DEG, 0.05, D
            )
        )),
        // https://aa.usno.navy.mil/calculated/altaz?body=11&date=1900-03-20&intv_mag=1&lat=55.6000&lon=11.9667&label=Lejre&tz=0.00&tz_sign=1&submit=Get+Data
        new LocalExample(4, GeoPosition.of(ExampleLocation.LEJRE), UniversalTimelinePoint.ofCalendaricParameters(1900,  3, 20,  6,  6, 0), Arrays.asList(
            v(
                MOON_ALTITUDE.key(),
                -1.0,
                DEG, 1.0, A
            ),
            v(
                MOON_AZIMUTH.key(),
                236.8 - 180,
                DEG, 0.05, D
            )
        )),
        // https://aa.usno.navy.mil/calculated/ssconf?date=1800-01-01&time=18%3A00%3A00.000&intv_mag=0.1&intv_unit=4&reps=1&lat=51.1167&lon=17.0333&label=Wroc%C5%82aw&height=123&submit=Get+Data
        new LocalExample(5, ExampleLocation.WROCLAW, UniversalTimelinePoint.ofCalendaricParameters(1800,  1, 1.75), Arrays.asList(
            v(
                SUN_TOPOCENTRIC_RIGHT_ASCENSION.key(),
                timeToDays(18, 48, 42),
                TURN, 3 * Calcs.SECOND_TO_DAY, D
            ),
            v(
                SUN_TOPOCENTRIC_DECLINATION.key(),
                toSingleDegreesValue(-23, 0, 0),
                DEG, 0.5 * Calcs.ARCMINUTE_TO_DEGREE, D
            ),
            v(
                SUN_TOPOCENTRIC_ALTITUDE.key(),
                90 - 118,
                DEG, 0.5, D
            ),
            v(
                SUN_TOPOCENTRIC_AZIMUTH.key(),
                267 - 180,
                DEG, 0.5, D
            ),
            v(
                MOON_TOPOCENTRIC_RIGHT_ASCENSION.key(),
                timeToDays(23, 55, 30),
                TURN, 3 * Calcs.SECOND_TO_DAY, D
            ),
            v(
                MOON_TOPOCENTRIC_DECLINATION.key(),
                toSingleDegreesValue(-4, 24, 0),
                DEG, 0.5 * Calcs.ARCMINUTE_TO_DEGREE, D
            ),
            v(
                MOON_TOPOCENTRIC_ALTITUDE.key(),
                90 - 61,
                DEG, 0.5, D
            ),
            v(
                MOON_TOPOCENTRIC_AZIMUTH.key(),
                214 - 180,
                DEG, 0.5, D
            )
        ))
    );

    @Test
    public void shouldCalculateGlobalCoords() {
        for(Example<GlobalCoord> example : GLOBAL_EXAMPLES) {
            MultiCalcComposition<GlobalCoord, TimelinePoint> calc = CoordsCalcCompositions.compose(example.keys);
            Map<GlobalCoord, Object> actual = calc.calculate(example.tx);
            example.entries.forEach(v -> assertEquals(v.normalValue(), (Double) actual.get(v.key), v.normalDelta(), msg(example, v, actual)));
        }
    }

    @Test
    public void shouldCalculateLocalCoords() {
        for (LocalExample example : LOCAL_EXAMPLES) {
            MultiCalcComposition<Key, TimelinePoint> calc = CoordsCalcCompositions.compose(example.keys, example.gp);
            Map<Key, Object> actual = calc.calculate(example.tx);
            example.entries.forEach(v -> assertEquals(v.normalValue(), (Double) actual.get(v.key), v.normalDelta(), msg(example, v, actual)));
        }
    }

    static class Example<K> {
        int number;
        TimelinePoint tx;
        Collection<V<K>> entries;
        Set<K> keys = new HashSet<>();
        Example(int number, TimelinePoint tx, Collection<V<K>> entries) {
            this.number = number;
            this.tx = tx;
            this.entries = entries;
            entries.forEach(e -> keys.add(e.key));
        }
    }

    static class LocalExample extends Example<Key> {
        GeoPosition gp;
        LocalExample(int number, GeoPosition gp, TimelinePoint tx, Collection<V<Key>> entries) {
            super(number, tx, entries);
            this.gp = gp;
        }
    }

    static class V<K> {
        K key;
        double value;
        double toNormalFactor = 1.0;
        double delta;
        PrecisionOrigin precisionOrigin;
        V(K key, double value, double toNormalFactor, double delta, PrecisionOrigin precisionOrigin) {
            this.key = key;
            this.value = value;
            this.toNormalFactor = toNormalFactor;
            this.delta = delta;
            this.precisionOrigin = precisionOrigin;
        }
        double normalValue() {
            return toNormalFactor * value;
        }
        double normalDelta() {
            return toNormalFactor * delta;
        }
    }

    static enum PrecisionOrigin {
        DATA, ARBITRARY;
    }

    static <K> V<K> v(K key, double value, double toNormalFactor, double delta, PrecisionOrigin precisionOrigin) {
        return new V<>(key, value, toNormalFactor, delta, precisionOrigin);
    }

    static <K> String msg(Example<K> example, V<K> v, Map<K, Object> actual) {
        return String.format("Ex.#%d : %s\t(exp.) %.9f vs (act.) %.9f\tPrec.: %s",
            example.number,
            v.key.toString(),
            v.value,
            ((Double) actual.get(v.key)) / v.toNormalFactor,
            v.precisionOrigin.name()
        );
    }
}
