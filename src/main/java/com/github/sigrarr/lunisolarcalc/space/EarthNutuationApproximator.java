package com.github.sigrarr.lunisolarcalc.space;

import com.github.sigrarr.lunisolarcalc.util.Calcs;

public class EarthNutuationApproximator {

    /**
     * Meeus 1998, Ch. 22, p. 144
     */
    public double approximateNutuation(double centurialT) {
        double basicOmega = Calcs.toLongitudinallyNormalRadians(125.04452 - (1934.136261 * centurialT));
        double l = Calcs.toLongitudinallyNormalRadians(280.4665 + (36000.7698 * centurialT));
        double lPrim = Calcs.toLongitudinallyNormalRadians(218.3165 + (481267.8813 * centurialT));
        double resultArcseconds = (-17.2 * Math.sin(basicOmega))
            - (1.32 * Math.sin(-2.0 * l))
            - (0.23 * Math.sin(2.0 * lPrim))
            + (0.21 * Math.sin(2.0 * basicOmega));
        return Calcs.toLongitudinallyNormalRadians(Calcs.arcsecondsToDegrees(resultArcseconds));
    }
}
