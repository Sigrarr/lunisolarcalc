package com.github.sigrarr.lunisolarcalc.time.tables;

import com.github.sigrarr.lunisolarcalc.time.DeltaTResolver;

/**
 * Morrison & Stephenson 2004, pp. 331-332, esp. Table 1
 */
public class DeltaTTable implements DeltaTResolver {
    
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
    };

    private int searchStartIndex = 0;
    private int searchEndIndex = ROMAN_YEARS.length - 1;

    public int getDeltaT(int requestedRomanYear) {
        for (int i = searchStartIndex; i < searchEndIndex; i++) {
            if (requestedRomanYear >= ROMAN_YEARS[i] && requestedRomanYear <= ROMAN_YEARS[i + 1]) {
                return findDeltaTByLinearInterpolation(requestedRomanYear, i, i + 1);
            }
        }
        throw new IndexOutOfBoundsException("" + requestedRomanYear);
    }

    private int findDeltaTByLinearInterpolation(int requestedRomanYear, int startIndex, int endIndex) {
        double relativePosition = ((double) requestedRomanYear - ROMAN_YEARS[startIndex]) / (ROMAN_YEARS[endIndex] - ROMAN_YEARS[startIndex]);
        return DELTA_T[startIndex] + (int) Math.round(relativePosition * (DELTA_T[endIndex] - DELTA_T[startIndex]));
    }
}
