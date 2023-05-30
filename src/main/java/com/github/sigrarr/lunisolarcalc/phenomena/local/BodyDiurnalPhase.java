package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.util.Titled;

/**
 * A specific {@link DiurnalPhase diurnal phase}
 * of a specific {@link Body celestial body} (a combination).
 */
public enum BodyDiurnalPhase implements Titled {
    /**
     * {@linkplain DiurnalPhase#RISE Rise} of the {@linkplain Body#MOON Moon}.
     */
    MOON_RISE(Body.MOON, DiurnalPhase.RISE),
    /**
     * {@linkplain DiurnalPhase#TRANSIT Transit} of the {@linkplain Body#MOON Moon}.
     */
    MOON_TRANSIT(Body.MOON, DiurnalPhase.TRANSIT),
    /**
     * {@linkplain DiurnalPhase#SET Set} of the {@linkplain Body#MOON Moon}.
     */
    MOON_SET(Body.MOON, DiurnalPhase.SET),
    /**
     * {@linkplain DiurnalPhase#RISE Rise} of the {@linkplain Body#SUN Sun}.
     */
    SUN_RISE(Body.SUN, DiurnalPhase.RISE),
    /**
     * {@linkplain DiurnalPhase#TRANSIT Transit} of the {@linkplain Body#SUN Sun}.
     */
    SUN_TRANSIT(Body.SUN, DiurnalPhase.TRANSIT),
    /**
     * {@linkplain DiurnalPhase#SET Set} of the {@linkplain Body#SUN Sun}.
     */
    SUN_SET(Body.SUN, DiurnalPhase.SET);

    public final Body body;
    public final DiurnalPhase diurnalPhase;

    private BodyDiurnalPhase(Body body, DiurnalPhase diurnalPhase) {
        this.body = body;
        this.diurnalPhase = diurnalPhase;
    }

    /**
     * Gets an instance representing the given diurnal phase
     * of the given celestial body.
     *
     * @param body              celestial body
     * @param diurnalPhase      diurnal phase
     * @return                  instance (combination)
     */
    public static BodyDiurnalPhase of(Body body, DiurnalPhase diurnalPhase) {
        return values()[3 * body.ordinal() + diurnalPhase.ordinal()];
    }

    @Override
    public String getTitle() {
        return body.getTitle() + "-" + diurnalPhase.getTitle();
    }
}
