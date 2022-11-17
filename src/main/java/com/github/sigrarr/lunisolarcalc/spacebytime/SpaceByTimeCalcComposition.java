package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.EnumSet;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

public class SpaceByTimeCalcComposition {

    private static final CalculationComposer<Subject, TimelinePoint> composer = new CalculationComposer<Subject, TimelinePoint>(Subject.class) {{
        register(new AberrationEarthSunCalculator());
        register(new EarthLatitudeCalculator());
        register(new EarthLongitudeCalculator());
        register(new EarthNutuationElements());
        register(new EarthNutuationInLongitudeCalculator());
        register(new EarthSunRadiusCalculator());
        register(new MoonApparentLongitudeCalculator());
        register(new MoonCoordinateElements());
        register(new MoonEarthDistanceCalculator());
        register(new MoonLatitudeCalculator());
        register(new MoonLongitudeCalculator());
        register(new MoonOverSunApparentLongitudeExcessCalculator());
        register(new SunAberratedLongitudeCalculator());
        register(new SunApparentLongitudeCalculator());
        register(new SunGeometricLongitudeCalculator());
        register(new SunLatitudeCalculator());
    }};

    public static SingleOutputComposition<Subject, TimelinePoint> compose(Subject subject) {
        return composer.compose(subject);
    }

    public static MultiOutputComposition<Subject, TimelinePoint> compose(EnumSet<Subject> subjects) {
        return composer.compose(subjects);
    }
}
