package com.github.sigrarr.lunisolarcalc.phenomena.local;

public final class MoonDiurnalPhaseFinder extends DiurnalPhaseFinderAbstract {
    public MoonDiurnalPhaseFinder() {
        super(new MoonDiurnalPhaseCalcCore());
    }
}
