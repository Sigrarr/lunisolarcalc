package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

public class SpaceByTimeCalcCompositionsTest {

    private EarthNutuationElements earthNutuationElements = EarthNutuationElements.makeUnevaluatedInstance();
    private MoonCoordinateElements moonCoordinateElements = MoonCoordinateElements.makeUnevaluatedInstance();
    private EarthLongitudeCalculator earthLongitudeCalculator = new EarthLongitudeCalculator();
    private EarthSunRadiusCalculator earthSunRadiusCalculator = new EarthSunRadiusCalculator();
    private SunGeometricLongitudeCalculator sunGeometricLongitudeCalculator = new SunGeometricLongitudeCalculator();
    private EarthNutuationInLongitudeCalculator earthNutuationInLongitudeCalculator = new EarthNutuationInLongitudeCalculator();
    private AberrationEarthSunCalculator aberrationEarthSunCalculator = new AberrationEarthSunCalculator();
    private MoonLongitudeCalculator moonLongitudeCalculator = new MoonLongitudeCalculator();
    private EarthLatitudeCalculator earthLatitudeCalculator = new EarthLatitudeCalculator();
    private SunLatitudeCalculator sunLatitudeCalculator = new SunLatitudeCalculator();
    private SunApparentLongitudeCalculator sunApparentLongitudeCalculator = new SunApparentLongitudeCalculator();
    private SunAberratedLongitudeCalculator sunAberratedLongitudeCalculator = new SunAberratedLongitudeCalculator();
    private MoonLatitudeCalculator moonLatitudeCalculator = new MoonLatitudeCalculator();
    private MoonEarthDistanceCalculator moonEarthDistanceCalculator = new MoonEarthDistanceCalculator();
    private MoonApparentLongitudeCalculator moonApparentLongitudeCalculator = new MoonApparentLongitudeCalculator();
    private MoonOverSunApparentLongitudeExcessCalculator moonOverSunApparentLongitudeExcessCalculator = new MoonOverSunApparentLongitudeExcessCalculator();
    private Map<Subject, SingleOutputComposition<Subject, TimelinePoint>> subjectToComposition = Arrays.stream(Subject.values())
        .collect(Collectors.toMap(s -> s, s -> SpaceByTimeCalcCompositions.compose(s)));

    @Test
    public void shouldCompositionsAndCoreCalculatorsGiveEqualResults() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            TimelinePoint tx = new DynamicalTimelinePoint(random.nextDouble() * Timeline.JULIAN_PERIOD_END_JD);
            assertForRootArgument(tx);
        }
    }

    private void assertForRootArgument(TimelinePoint tx) {
        moonCoordinateElements.calculate(tx);
        earthNutuationElements.calculate(tx);
        double earthLongitude = earthLongitudeCalculator.calculate(tx);
        double earthSunRadius = earthSunRadiusCalculator.calculate(tx);
        double sunGeometricLongitude = sunGeometricLongitudeCalculator.calculate(earthLongitude);
        double earthNutuationInLongitude = earthNutuationInLongitudeCalculator.calculate(tx, earthNutuationElements);
        double aberrationEarthSun = aberrationEarthSunCalculator.calculate(tx, earthSunRadius);
        double moonLongitude = moonLongitudeCalculator.calculate(tx, moonCoordinateElements);
        double earthLatitude = earthLatitudeCalculator.calculate(tx);
        double sunLatitude = sunLatitudeCalculator.calculate(tx, earthLatitude, earthLongitude);
        double sunApparentLongitude = sunApparentLongitudeCalculator.calculate(sunGeometricLongitude, earthNutuationInLongitude, aberrationEarthSun);
        double sunAberratedLongitude = sunAberratedLongitudeCalculator.calculate(sunGeometricLongitude, aberrationEarthSun);
        double moonLatitude = moonLatitudeCalculator.calculate(tx, moonCoordinateElements);
        double moonEarthDistance = moonEarthDistanceCalculator.calculate(tx, moonCoordinateElements);
        double moonApparentLongitude = moonApparentLongitudeCalculator.calculate(moonLongitude, earthNutuationInLongitude);
        double moonOverSunApparentLongitudeExcess = moonOverSunApparentLongitudeExcessCalculator.calculate(moonLongitude, sunAberratedLongitude);

        assertEquals(earthLongitude, getCompositionNumericResult(Subject.EARTH_LONGITUDE, tx), Calcs.EPSILON_MIN);
        assertEquals(earthSunRadius, getCompositionNumericResult(Subject.EARTH_SUN_RADIUS, tx), Calcs.EPSILON_MIN);
        assertEquals(sunGeometricLongitude, getCompositionNumericResult(Subject.SUN_GEOMETRIC_LONGITUDE, tx), Calcs.EPSILON_MIN);
        assertEquals(earthNutuationInLongitude, getCompositionNumericResult(Subject.EARTH_NUTUATION_IN_LONGITUDE, tx), Calcs.EPSILON_MIN);
        assertEquals(aberrationEarthSun, getCompositionNumericResult(Subject.ABERRATION_EARTH_SUN, tx), Calcs.EPSILON_MIN);
        assertEquals(moonLongitude, getCompositionNumericResult(Subject.MOON_LONGITUDE, tx), Calcs.EPSILON_MIN);
        assertEquals(earthLatitude, getCompositionNumericResult(Subject.EARTH_LATITUDE, tx), Calcs.EPSILON_MIN);
        assertEquals(sunLatitude, getCompositionNumericResult(Subject.SUN_LATITUDE, tx), Calcs.EPSILON_MIN);
        assertEquals(sunApparentLongitude, getCompositionNumericResult(Subject.SUN_APPARENT_LONGITUDE, tx), Calcs.EPSILON_MIN);
        assertEquals(sunAberratedLongitude, getCompositionNumericResult(Subject.SUN_ABERRATED_LONGITUDE, tx), Calcs.EPSILON_MIN);
        assertEquals(moonLatitude, getCompositionNumericResult(Subject.MOON_LATITUDE, tx), Calcs.EPSILON_MIN);
        assertEquals(moonEarthDistance, getCompositionNumericResult(Subject.MOON_EARTH_DISTANCE, tx), Calcs.EPSILON_MIN);
        assertEquals(moonApparentLongitude, getCompositionNumericResult(Subject.MOON_APPARENT_LONGITUDE, tx), Calcs.EPSILON_MIN);
        assertEquals(moonOverSunApparentLongitudeExcess, getCompositionNumericResult(Subject.MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS, tx), Calcs.EPSILON_MIN);
    }

    private double getCompositionNumericResult(Subject subject, TimelinePoint tx) {
        return (Double) subjectToComposition.get(subject).calculate(tx);
    }
}
