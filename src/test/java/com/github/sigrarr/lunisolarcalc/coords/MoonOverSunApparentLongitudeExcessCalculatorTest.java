package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.sigrarr.lunisolarcalc.phenomena.global.MoonPhase;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.jupiter.api.Test;

public class MoonOverSunApparentLongitudeExcessCalculatorTest {
    /**
     * cf. https://spaceplace.nasa.gov/review/moon-phases/moon-phases.en.png
     */
    private static final double[][] SUN_LAMBDA_DEGREES_MOON_LAMBDA_DEGREES_EXCESS_RADIANS = {
        {   0.0 ,   0.0 , MoonPhase.NEW_MOON.moonOverSunApparentLongitudeExcess },
        {   0.0 ,  90.0 , MoonPhase.FIRST_QUARTER.moonOverSunApparentLongitudeExcess },
        {   0.0 , 180.0 , MoonPhase.FULL_MOON.moonOverSunApparentLongitudeExcess },
        {   0.0 , 270.0 , MoonPhase.THIRD_QUARTER.moonOverSunApparentLongitudeExcess },

        {  90.0 ,  90.0 , MoonPhase.NEW_MOON.moonOverSunApparentLongitudeExcess },
        {  90.0 , 180.0 , MoonPhase.FIRST_QUARTER.moonOverSunApparentLongitudeExcess },
        {  90.0 , 270.0 , MoonPhase.FULL_MOON.moonOverSunApparentLongitudeExcess },
        {  90.0 ,   0.0 , MoonPhase.THIRD_QUARTER.moonOverSunApparentLongitudeExcess },

        { 180.0 , 180.0 , MoonPhase.NEW_MOON.moonOverSunApparentLongitudeExcess },
        { 180.0 , 270.0 , MoonPhase.FIRST_QUARTER.moonOverSunApparentLongitudeExcess },
        { 180.0 ,   0.0 , MoonPhase.FULL_MOON.moonOverSunApparentLongitudeExcess },
        { 180.0 ,  90.0 , MoonPhase.THIRD_QUARTER.moonOverSunApparentLongitudeExcess },
    };

    private MoonOverSunApparentLongitudeExcessCalculator calculator = new MoonOverSunApparentLongitudeExcessCalculator();

    @Test
    public void shouldCalculateExcess() {
        for (double[] row : SUN_LAMBDA_DEGREES_MOON_LAMBDA_DEGREES_EXCESS_RADIANS) {
            double sunAberratedL = Math.toRadians(row[0]);
            double moonL = Math.toRadians(row[1]);
            double phaseExcess = row[2];
            assertEquals(phaseExcess, calculator.calculate(moonL, sunAberratedL), Calcs.EPSILON_MIN);
        }
    }
}
