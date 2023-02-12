package com.github.sigrarr.lunisolarcalc.coords;

import java.util.EnumSet;

import com.github.sigrarr.lunisolarcalc.time.TimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

/**
 * Shortcut for {@linkplain CalculationComposer composing calculations} of quantities supported by this package.
 * Use it to conveniently compose a calculation for a given {@link Subject} or set of Subjects,
 * instead of managing calculators manually. It may help in resolving dependencies and avoinding redundancy.
 */
public abstract class CoordsCalcCompositions {

    private static final CalculationComposer<Subject, TimelinePoint> composer = new CalculationComposer<Subject, TimelinePoint>(Subject.class) {{
        register(new AberrationEarthSunCalculator());
        register(new EarthLatitudeCalculator());
        register(new EarthLongitudeCalculator());
        register(EarthNutuationElements.makeUnevaluatedInstance());
        register(new EarthNutuationInLongitudeCalculator());
        register(new EarthNutuationInObliquityCalculator());
        register(new EarthSunRadiusCalculator());
        register(new EclipticMeanObliquityCalculator());
        register(new EclipticTrueObliquityCalculator());
        register(new MoonApparentLongitudeCalculator());
        register(MoonCoordinateElements.makeUnevaluatedInstance());
        register(new MoonDeclinationCalculator());
        register(new MoonEarthDistanceCalculator());
        register(new MoonHourAngleCalculator());
        register(new MoonLatitudeCalculator());
        register(new MoonLongitudeCalculator());
        register(new MoonOverSunApparentLongitudeExcessCalculator());
        register(new MoonRightAscensionCalculator());
        register(new SiderealMeanTimeCalculator());
        register(new SiderealApparentTimeCalculator());
        register(new SunAberratedLongitudeCalculator());
        register(new SunApparentLongitudeCalculator());
        register(new SunDeclinationCalculator());
        register(new SunGeometricLongitudeCalculator());
        register(new SunHourAngleCalculator());
        register(new SunLatitudeCalculator());
        register(new SunRightAscensionCalculator());
    }};

    /**
     * Composes a calculation which yields a value of a {@linkplain Subject requested quantity}
     * for a {@linkplain TimelinePoint time argument} given as a root input.
     *
     * @param subject   the quantity you want to calculate
     * @return          a composed calculation, which will yield a value of the requested quantity
     *                  for an input {@link TimelinePoint}
     */
    public static SingleOutputComposition<Subject, TimelinePoint> compose(Subject subject) {
        return composer.compose(subject);
    }

    /**
     * Composes a calculation which yields a collection of values of {@linkplain Subject requested quantities}
     * for a {@linkplain TimelinePoint time argument} given as a root input.
     *
     * @param subjects  the quantities you want to calculate
     * @return          a composed calculation, which will yield a collection of values
     *                  of the requested quantities for input {@link TimelinePoint}
     */
    public static MultiOutputComposition<Subject, TimelinePoint> compose(EnumSet<Subject> subjects) {
        return composer.compose(subjects);
    }
}
