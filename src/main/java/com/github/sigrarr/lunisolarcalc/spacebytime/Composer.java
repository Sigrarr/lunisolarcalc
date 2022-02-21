package com.github.sigrarr.lunisolarcalc.spacebytime;

import java.util.*;

import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

public final class Composer extends CalculationComposer<Subject, Double, Double> {

    protected static final List<Provider<Subject, Double, Double>> PROVIDERS = new ArrayList<Provider<Subject, Double, Double>>(16) {{
        add(new AberrationEarthSunCalculator());
        add(new EarthLatitudeCalculator());
        add(new EarthLongitudeCalculator());
        add(new EarthNutuationElements());
        add(new EarthNutuationInLongitudeCalculator());
        add(new EarthSunRadiusCalculator());
        add(new MoonApparentLongitudeCalculator());
        add(new MoonCoordinateElements());
        add(new MoonEarthDistanceCalculator());
        add(new MoonLatitudeCalculator());
        add(new MoonLongitudeCalculator());
        add(new MoonOverSunApparentLongitudeExcessCalculator());
        add(new SunAberratedLongitudeCalculator());
        add(new SunApparentLongitudeCalculator());
        add(new SunGeometricLongitudeCalculator());
        add(new SunLatitudeCalculator());
    }};
    private static final Composer INSTANCE = new Composer(PROVIDERS);

    public static Composer get() {
        return INSTANCE;
    }

    private Composer(List<Provider<Subject, Double, Double>> providers) {
        super(Subject.class, Double.class);
        for (Provider<Subject, Double, Double> provider : providers) {
            register(provider);
        }
    }
}
