package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.coords.Subject;
import com.github.sigrarr.lunisolarcalc.util.Titled;

public enum Body implements Titled {

    MOON(Subject.MOON_DECLINATION, Subject.MOON_RIGHT_ASCENSION, Subject.MOON_HOUR_ANGLE, "Moon"),
    SUN(Subject.SUN_DECLINATION, Subject.SUN_RIGHT_ASCENSION, Subject.SUN_HOUR_ANGLE, "Sun");

    public final Subject declinationSubject;
    public final Subject rightAscensionSubject;
    public final Subject hourAngleSubject;
    private final String title;

    private Body(Subject declinationSubject, Subject rightAscensionSubject, Subject hourAngleSubject, String title) {
        this.declinationSubject = declinationSubject;
        this.rightAscensionSubject = rightAscensionSubject;
        this.hourAngleSubject = hourAngleSubject;
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
