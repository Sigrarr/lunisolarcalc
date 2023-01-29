package com.github.sigrarr.lunisolarcalc.time.timescaledelta;

import static com.github.sigrarr.lunisolarcalc.time.timescaledelta.BasisMinus700ToPlus2000Resolver.extrapolateParabolically;
import static com.github.sigrarr.lunisolarcalc.time.timescaledelta.Util.*;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.github.sigrarr.lunisolarcalc.time.TimeScale;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

/**
 * @see " Morrison & Stephenson 2004, Table 1
 */
class Table {

    final static int[] CALENDAR_YEARS = {
        // Smoothing:
        -710,
        // Table 1:
        -700,
        -600,
        -500,
        -400,
        -300,
        -200,
        -100,
           0,
         100,
         200,
         300,
         400,
         500,
         600,
         700,
         800,
         900,
        1000,
        1100,
        1200,
        1300,
        1400,
        1500,
        1600,
        1700,
        1710,
        1720,
        1730,
        1740,
        1750,
        1760,
        1770,
        1780,
        1790,
        1800,
        1810,
        1820,
        1830,
        1840,
        1850,
        1860,
        1870,
        1880,
        1890,
        1900,
        1910,
        1920,
        1930,
        1940,
        1950,
        1960,
        1970,
        1980,
        1990,
        2000,
        // Smoothing:
        2001,
    };

    final static double[] DELTA_T = {
        // Smoothing:
        extrapolateParabolically(yearStartToCenturialVector(CALENDAR_YEARS[0])),
        // Table 1:
        21000,
        19040,
        17190,
        15530,
        14080,
        12790,
        11640,
        10580,
         9600,
         8640,
         7680,
         6700,
         5710,
         4740,
         3810,
         2960,
         2200,
         1570,
         1090,
          740,
          490,
          320,
          200,
          120,
            9,
           10,
           11,
           11,
           12,
           13,
           15,
           16,
           17,
           17,
           14,
           13,
           12,
            8,
            6,
            7,
            8,
            2,
           -5,
           -6,
           -3,
           10,
           21,
           24,
           24,
           29,
           33,
           40,
           51,
           57,
           65,
        // Smoothing:
        extrapolateParabolically(yearStartToCenturialVector(CALENDAR_YEARS[CALENDAR_YEARS.length-1])),
    };

    final static double[] KEY_JD = Arrays.stream(CALENDAR_YEARS)
        .sorted()
        .mapToDouble(Util::yearStartToJulianDay)
        .toArray();
    final static double[] KEY_JDE = IntStream.range(0, KEY_JD.length)
        .mapToDouble(i -> KEY_JD[i] + (Calcs.SECOND_TO_DAY * DELTA_T[i]))
        .toArray();

    final static int OUT = -1;

    static double[] getJdAxisPoints(TimeScale timeScale) {
        return timeScale == TimeScale.UNIVERSAL ? KEY_JD : KEY_JDE;
    }

    static int tryFindFloorIndex(double jd, double[] jdAxisPoints) {
        for (int i = 0; i < jdAxisPoints.length - 1; i++)
            if (Calcs.compare(jd, jdAxisPoints[i]) >= 0 && Calcs.compare(jd, jdAxisPoints[i+1]) <= 0)
                return i;
        return OUT;
    }
}
