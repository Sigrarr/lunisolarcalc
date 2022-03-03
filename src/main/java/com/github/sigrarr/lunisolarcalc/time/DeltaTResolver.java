package com.github.sigrarr.lunisolarcalc.time;

import com.github.sigrarr.lunisolarcalc.time.Time.DeltaTProvider;

/**
 * Morrison & Stephenson 2004, pp. 331-332, 335, esp. Table 1
 */
public class DeltaTResolver implements DeltaTProvider {
    
    private final static int[] ROMAN_YEARS = {
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
    private int tableEndIndex = ROMAN_YEARS.length - 1;

    @Override
    public int getDeltaT(int requestedRomanYear) {
        for (int i = tableStartIndex; i < tableEndIndex; i++) {
            if (requestedRomanYear >= ROMAN_YEARS[i] && requestedRomanYear <= ROMAN_YEARS[i + 1]) {
                return interpolateDeltaTLinearly(requestedRomanYear, i, i + 1);
            }
        }
        return extrapolateDeltaTParabolically(requestedRomanYear);
    }

    private int interpolateDeltaTLinearly(int requestedRomanYear, int startIndex, int endIndex) {
        double relativePosition = ((double) requestedRomanYear - ROMAN_YEARS[startIndex]) / (ROMAN_YEARS[endIndex] - ROMAN_YEARS[startIndex]);
        return DELTA_T[startIndex] + (int) Math.round(relativePosition * (DELTA_T[endIndex] - DELTA_T[startIndex]));
    }

    private int extrapolateDeltaTParabolically(int requestedRomanYear) {
        return (int) Math.round(
            -20.0 + (32.0 * Math.pow(0.01 * (requestedRomanYear - 1820), 2.0))
        );
    }
}
