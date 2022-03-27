package com.github.sigrarr.lunisolarcalc.time.time;

import com.github.sigrarr.lunisolarcalc.time.Time.DeltaTResolver;

/**
 * Morrison & Stephenson 2004, pp. 331-332, 335, esp. Table 1
 */
public class DeltaTResolverUsingTableForMinus700ToPlus2000 implements DeltaTResolver {
    
    private final static int[] GREGORIAN_YEARS = {
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
    };
    private final static int[] DELTA_T = {
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
    };

    private int tableStartIndex = 0;
    private int tableEndIndex = GREGORIAN_YEARS.length - 1;

    @Override
    public int resolveDeltaT(int requestedGregorianYear) {
        for (int i = tableStartIndex; i < tableEndIndex; i++) {
            if (requestedGregorianYear >= GREGORIAN_YEARS[i] && requestedGregorianYear <= GREGORIAN_YEARS[i + 1]) {
                return interpolateDeltaTLinearly(requestedGregorianYear, i, i + 1);
            }
        }
        return extrapolateDeltaTParabolically(requestedGregorianYear);
    }

    private int interpolateDeltaTLinearly(int requestedGregorianYear, int startIndex, int endIndex) {
        double relativePosition = ((double) requestedGregorianYear - GREGORIAN_YEARS[startIndex]) / (GREGORIAN_YEARS[endIndex] - GREGORIAN_YEARS[startIndex]);
        return DELTA_T[startIndex] + (int) Math.round(relativePosition * (DELTA_T[endIndex] - DELTA_T[startIndex]));
    }

    private int extrapolateDeltaTParabolically(int requestedGregorianYear) {
        return (int) Math.round(
            -20.0 + (32.0 * Math.pow(0.01 * (requestedGregorianYear - 1820), 2.0))
        );
    }
}
