package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

public class SpaceByTimeCalcCompositionTest {

    private EarthLongitudeCalculator earthLongitudeCalculator = new EarthLongitudeCalculator();
    private MoonCoordinateElements moonCoordinateElements = new MoonCoordinateElements();
    private EarthNutuationElements earthNutuationElements = new EarthNutuationElements();
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
        .collect(Collectors.toMap(s -> s, s -> SpaceByTimeCalcComposition.compose(s)));

    @Test
    public void shouldCompositionsAndCoreCalculatorsGiveEqualResults() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            TimelinePoint tx = TimelinePoint.ofJulianEphemerisDay(random.nextDouble() * Timeline.JULIAN_PERIOD_END_JD);
            assertForRootArgument(tx);
        }
    }

    private void assertForRootArgument(TimelinePoint tx) {
        double earthLongitude = earthLongitudeCalculator.calculateCoordinate(tx);
        moonCoordinateElements.calculate(tx);
        earthNutuationElements.calculate(tx);
        double earthSunRadius = earthSunRadiusCalculator.calculateCoordinate(tx);
        double sunGeometricLongitude = sunGeometricLongitudeCalculator.calculateGeometricLongitude(earthLongitude);
        double earthNutuationInLongitude = earthNutuationInLongitudeCalculator.calculateNutuation(tx, earthNutuationElements);
        double aberrationEarthSun = aberrationEarthSunCalculator.calculateAberration(tx, earthSunRadius);
        double moonLongitude = moonLongitudeCalculator.calculateCoordinate(tx, moonCoordinateElements);
        double earthLatitude = earthLatitudeCalculator.calculateCoordinate(tx);
        double sunLatitude = sunLatitudeCalculator.calculateLatitude(tx, earthLatitude, earthLongitude);
        double sunApparentLongitude = sunApparentLongitudeCalculator.calculateApparentLongitude(sunGeometricLongitude, earthNutuationInLongitude, aberrationEarthSun);
        double sunAberratedLongitude = sunAberratedLongitudeCalculator.calculateAberratedLongitude(sunGeometricLongitude, aberrationEarthSun);
        double moonLatitude = moonLatitudeCalculator.calculateCoordinate(tx, moonCoordinateElements);
        double moonEarthDistance = moonEarthDistanceCalculator.calculateCoordinate(tx, moonCoordinateElements);
        double moonApparentLongitude = moonApparentLongitudeCalculator.calculateApparentLongitude(moonLongitude, earthNutuationInLongitude);
        double moonOverSunApparentLongitudeExcess = moonOverSunApparentLongitudeExcessCalculator.calculateExcess(moonLongitude, sunAberratedLongitude);

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
