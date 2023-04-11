package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.phenomena.exceptions.PrecisionAngleTooSmallException;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class DiurnalPhaseFinderAbstractTest {

    private final DiurnalPhaseFinderAbstract[] finders = {new SunDiurnalPhaseFinder(), new MoonDiurnalPhaseFinder()};

    @Test
    public void shouldValidatePrecisionSetting() {
        double barelyOkRadians = DiurnalPhaseFinderAbstract.MIN_PRECISION_RADIANS;
        double wrongRadians = DiurnalPhaseFinderAbstract.MIN_PRECISION_RADIANS - Calcs.EPSILON_12;
        double barelyOkArcseconds = Calcs.Angle.toArcseconds(Math.toDegrees(barelyOkRadians));
        double wrongArcseconds = barelyOkArcseconds - Calcs.EPSILON_12;

        for (DiurnalPhaseFinderAbstract finder : finders) {
            assertDoesNotThrow(() -> finder.setPrecision(barelyOkRadians));
            PrecisionAngleTooSmallException ex = assertThrows(PrecisionAngleTooSmallException.class,
                () -> finder.setPrecision(wrongRadians));
            assertEquals(wrongRadians, ex.getRadians(), Calcs.EPSILON_MIN);

            assertDoesNotThrow(() -> finder.setPrecisionArcseconds(barelyOkArcseconds));
            ex = assertThrows(PrecisionAngleTooSmallException.class,
                () -> finder.setPrecisionArcseconds(wrongArcseconds));
            assertEquals(Math.toRadians(Calcs.Angle.arcsecondsToDegrees(wrongArcseconds)), ex.getRadians(), Calcs.EPSILON_MIN);
        }
    }
}
