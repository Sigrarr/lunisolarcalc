package com.github.sigrarr.lunisolarcalc.coords.global;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions;
import com.github.sigrarr.lunisolarcalc.testing.TestUtils;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;

public class MoonSunElongationCalculatorTest {

    private MoonSunElongationCalculator calculator = new MoonSunElongationCalculator();

    @Test
    public void shouldCalculateElongation() {
        // Meeus 1998, Ex. 48.a, p. 347
        double alpha1 = Math.toRadians(134.6885);
        double delta1 = Math.toRadians(13.7684);
        double alpha2 = Math.toRadians(20.6579);
        double delta2 = Math.toRadians(8.6964);
        double actualElongation = calculator.calculate(delta1, alpha1, delta2, alpha2);
        assertEquals(110.7929, Math.toDegrees(actualElongation), TestUtils.decimalAutoDelta(0.0001));

        // Meeus 1998, Ex. 17.a, p. 110 (different bodies, but the formula is general)
        alpha1 = Math.toRadians(213.9154);
        delta1 = Math.toRadians(19.1825);
        alpha2 = Math.toRadians(201.2983);
        delta2 = Math.toRadians(-11.1614);
        actualElongation = calculator.calculate(delta1, alpha1, delta2, alpha2);
        assertEquals(32.7930, Math.toDegrees(actualElongation), TestUtils.decimalAutoDelta(0.0001));
    }

    @Test
    public void shouldGiveSameResultForEclipticalAndEquatorialCoords() {
        for (TimelinePoint tx : new TimelinePoint[] {Timeline.GREGORIAN_CALENDAR_START, Timeline.EPOCH_2000_TT}) {
            Map<GlobalCoord, Object> coords = CoordsCalcCompositions.compose(EnumSet.of(
                GlobalCoord.MOON_LATITUDE,
                GlobalCoord.MOON_APPARENT_LONGITUDE,
                GlobalCoord.SUN_LATITUDE,
                GlobalCoord.SUN_APPARENT_LONGITUDE,
                GlobalCoord.MOON_DECLINATION,
                GlobalCoord.MOON_RIGHT_ASCENSION,
                GlobalCoord.SUN_DECLINATION,
                GlobalCoord.SUN_RIGHT_ASCENSION
            )).calculate(tx);

            double eclipticalResult = calculator.calculate(
                (Double) coords.get(GlobalCoord.MOON_LATITUDE),
                (Double) coords.get(GlobalCoord.MOON_APPARENT_LONGITUDE),
                (Double) coords.get(GlobalCoord.SUN_LATITUDE),
                (Double) coords.get(GlobalCoord.SUN_APPARENT_LONGITUDE)
            );
            double equatorialResult = calculator.calculate(
                (Double) coords.get(GlobalCoord.MOON_DECLINATION),
                (Double) coords.get(GlobalCoord.MOON_RIGHT_ASCENSION),
                (Double) coords.get(GlobalCoord.SUN_DECLINATION),
                (Double) coords.get(GlobalCoord.SUN_RIGHT_ASCENSION)
            );

            assertEquals(equatorialResult, eclipticalResult, Calcs.EPSILON_12);
        }
    }
}
