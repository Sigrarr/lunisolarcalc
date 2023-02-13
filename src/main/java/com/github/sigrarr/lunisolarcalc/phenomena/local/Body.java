package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.coords.Subject;
import com.github.sigrarr.lunisolarcalc.util.Titled;

public enum Body implements Titled {

    MOON(Subject.MOON_RIGHT_ASCENSION, Subject.MOON_DECLINATION, "Moon"),
    SUN(Subject.SUN_RIGHT_ASCENSION, Subject.SUN_DECLINATION, "Sun");

    public final Subject rightAscensionSubject;
    public final Subject declinationSubject;
    private final String title;

    private Body(Subject rightAscensionSubject, Subject declinationSubject, String title) {
        this.title = title;
        this.rightAscensionSubject = rightAscensionSubject;
        this.declinationSubject = declinationSubject;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
