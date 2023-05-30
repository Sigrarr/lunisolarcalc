package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.coords.Subject;
import com.github.sigrarr.lunisolarcalc.util.Titled;

/**
 * A celestial body supported by this package.
 */
public enum Body implements Titled {

    MOON("Moon", Subject.MOON_DECLINATION, Subject.MOON_RIGHT_ASCENSION, Subject.MOON_HOUR_ANGLE),
    SUN("Sun", Subject.SUN_DECLINATION, Subject.SUN_RIGHT_ASCENSION, Subject.SUN_HOUR_ANGLE);

    protected final Subject declinationSubject;
    protected final Subject rightAscensionSubject;
    protected final Subject hourAngleSubject;
    private final String title;

    private Body(String title, Subject declinationSubject, Subject rightAscensionSubject, Subject hourAngleSubject) {
        this.title = title;
        this.declinationSubject = declinationSubject;
        this.rightAscensionSubject = rightAscensionSubject;
        this.hourAngleSubject = hourAngleSubject;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
