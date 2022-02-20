package com.github.sigrarr.lunisolarcalc.phenomena.tables;

import static org.junit.Assert.assertEquals;

import com.github.sigrarr.lunisolarcalc.phenomena.SunSeasonPoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

import org.junit.Test;

public class MeanSunSeasonPointApproximationTableTest {

    private MeanSunSeasonPointApproximationTable approximationTable = new MeanSunSeasonPointApproximationTable();

    @Test
    public void shouldEvaluate() {
        // Meeus 1998, Example 27.a, p. 180
        double actualApproximationJDE = approximationTable.evaluate(1962, SunSeasonPoint.JUNE_SOLSTICE);
        assertEquals(2437837.38589, actualApproximationJDE, Calcs.autoDelta(0.00001));
    }
}
