package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.util.Titled;

public enum BodyDiurnalPhase implements Titled {

    MOON_RISING(Body.MOON, DiurnalPhase.RISING),
    MOON_TRANSIT(Body.MOON, DiurnalPhase.TRANSIT),
    MOON_SETTING(Body.MOON, DiurnalPhase.SETTING),
    SUN_RISING(Body.SUN, DiurnalPhase.RISING),
    SUN_TRANSIT(Body.SUN, DiurnalPhase.TRANSIT),
    SUN_SETTING(Body.SUN, DiurnalPhase.SETTING);

    public final Body body;
    public final DiurnalPhase diurnalPhase;

    private BodyDiurnalPhase(Body body, DiurnalPhase diurnalPhase) {
        this.body = body;
        this.diurnalPhase = diurnalPhase;
    }

    public static BodyDiurnalPhase of(Body body, DiurnalPhase diurnalPhase) {
        return values()[3 * body.ordinal() + diurnalPhase.ordinal()];
    }

    @Override
    public String getTitle() {
        return body.getTitle() + " " + diurnalPhase.getTitle();
    }
}
