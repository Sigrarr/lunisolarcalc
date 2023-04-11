package com.github.sigrarr.lunisolarcalc.phenomena.local;

public final class SunDiurnalPhaseFinder extends DiurnalPhaseFinderAbstract {
    public SunDiurnalPhaseFinder() {
        super(new SunDiurnalPhaseCalcCore());
    }
}
