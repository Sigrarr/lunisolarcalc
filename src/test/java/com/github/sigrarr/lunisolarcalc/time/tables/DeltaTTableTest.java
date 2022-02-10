package com.github.sigrarr.lunisolarcalc.time.tables;

import static org.junit.Assert.assertEquals;

import java.util.*;
import org.junit.Test;

public class DeltaTTableTest {
    /**
     * Morrison & Stephenson 2004, pp. 331-332, esp. Table 1
     */
    private static final Map<Integer, Integer> ROMAN_YEAR_TO_DELTA_T = new HashMap<Integer, Integer>() {{
        put(-700, 21000);
        put(-600, 19040);
        put(-500, 17190);
        put(-400, 15530);
        put(-300, 14080);
        put(-200, 12790);
        put(-100, 11640);
        put(0, 10580);
        put(100, 9600);
        put(200, 8640);
        put(300, 7680);
        put(400, 6700);
        put(500, 5710);
        put(600, 4740);
        put(700, 3810);
        put(800, 2960);
        put(900, 2200);
        put(1000, 1570);
        put(1100, 1090);
        put(1200, 740);
        put(1300, 490);
        put(1400, 320);
        put(1500, 200);
        put(1600, 120);
    }};

    private DeltaTTable deltaTTable = new DeltaTTable();

    @Test
    public void shouldFindValueFromTable() {
        ROMAN_YEAR_TO_DELTA_T.forEach((y, delta) -> assertEquals(delta.intValue(), deltaTTable.getDeltaT(y.intValue())));
    }

    @Test
    public void shouldInterpolateLinearlyBetweenTablePoints() {
        assertEquals((21000 + 19040) / 2, deltaTTable.getDeltaT(-650));
        assertEquals(2960 + ((2200 - 2960) / 5), deltaTTable.getDeltaT(820));
    }
}
