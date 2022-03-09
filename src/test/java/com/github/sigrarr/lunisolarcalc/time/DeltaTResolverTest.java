package com.github.sigrarr.lunisolarcalc.time;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import org.junit.jupiter.api.Test;

public class DeltaTResolverTest {
    /**
     * Morrison & Stephenson 2004, Table 1, p. 332
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
        put(1700,   9);
        put(1710,  10);
        put(1720,  11);
        put(1730,  11);
        put(1740,  12);
        put(1750,  13);
        put(1760,  15);
        put(1770,  16);
        put(1780,  17);
        put(1790,  17);
        put(1800,  14);
        put(1810,  13);
        put(1820,  12);
        put(1830,   8);
        put(1840,   6);
        put(1850,   7);
        put(1860,   8);
        put(1870,   2);
        put(1880,  -5);
        put(1890,  -6);
        put(1900,  -3);
        put(1910,  10);
        put(1920,  21);
        put(1930,  24);
        put(1940,  24);
        put(1950,  29);
        put(1960,  33);
        put(1970,  40);
        put(1980,  51);
        put(1990,  57);
        put(2000,  65);
    }};

    private DeltaTResolver resolver = new DeltaTResolver();

    @Test
    public void shouldFindValueFromTable() {
        ROMAN_YEAR_TO_DELTA_T.forEach((y, delta) -> assertEquals(delta.intValue(), resolver.getDeltaT(y.intValue())));
    }

    @Test
    public void shouldInterpolateLinearlyBetweenTablePoints() {
        assertEquals((21000 + 19040) / 2, resolver.getDeltaT(-650));
        assertEquals(2960 + ((2200 - 2960) / 5), resolver.getDeltaT(820));
    }

    @Test
    public void shouldExtrapolateParabolicallyOutsideTable() {
        // Morrison & Stephenson 2004, Table 1, p. 332, entries with asterisks: rounded to 100s, -20s omitted (cf. p. 335)
        assertEquals(25400, resolver.getDeltaT(-1000), (100/2) + 20);
        assertEquals(23700, resolver.getDeltaT( -900), (100/2) + 20);
        assertEquals(22000, resolver.getDeltaT( -800), (100/2) + 20);
    }
}
