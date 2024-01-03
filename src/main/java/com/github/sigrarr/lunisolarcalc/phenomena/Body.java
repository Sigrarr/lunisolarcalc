package com.github.sigrarr.lunisolarcalc.phenomena;

import com.github.sigrarr.lunisolarcalc.coords.Subject;
import com.github.sigrarr.lunisolarcalc.util.Titled;

/**
 * A celestial body supported by this package.
 */
public enum Body implements Titled {

    MOON(
        "Moon",
        Subject.MOON_LATITUDE,
        Subject.MOON_APPARENT_LONGITUDE,
        Subject.MOON_DECLINATION,
        Subject.MOON_RIGHT_ASCENSION,
        Subject.MOON_HOUR_ANGLE
    ),
    SUN(
        "Sun",
        Subject.SUN_LATITUDE,
        Subject.SUN_APPARENT_LONGITUDE,
        Subject.SUN_DECLINATION,
        Subject.SUN_RIGHT_ASCENSION,
        Subject.SUN_HOUR_ANGLE
    );

    public final Subject latitudeSubject;
    public final Subject apparentLongitudeSubject;
    public final Subject declinationSubject;
    public final Subject rightAscensionSubject;
    public final Subject hourAngleSubject;
    private final String title;

    private Body(
        String title,
        Subject latitudeSubject,
        Subject apparentLongitudeSubject,
        Subject declinationSubject,
        Subject rightAscensionSubject,
        Subject hourAngleSubject
    ) {
        this.title = title;
        this.apparentLongitudeSubject = apparentLongitudeSubject;
        this.latitudeSubject = latitudeSubject;
        this.declinationSubject = declinationSubject;
        this.rightAscensionSubject = rightAscensionSubject;
        this.hourAngleSubject = hourAngleSubject;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
