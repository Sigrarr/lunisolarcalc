package com.github.sigrarr.lunisolarcalc.spacebytime;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

public class Composer extends CalculationComposer<Subject, TimelinePoint> {

    private static final Composer INSTANCE = new Composer() {{
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

    public static Composer get() {
        return INSTANCE;
    }

    private Composer() {
        super(Subject.class);
    }
}
