package com.github.sigrarr.lunisolarcalc.spacebytime;

import static org.junit.Assert.assertEquals;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.Calcs;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

public class ComposerTest {

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
    private Map<Subject, SingleOutputComposition<Subject, Double>> subjectToComposition = Arrays.stream(Subject.values())
        .collect(Collectors.toMap(s -> s, s -> Composer.get().compose(s)));

    @Test
    public void shouldCompositionsAndCoreCalculatorsGiveEqualResults() {
        double jdLimit = Timeline.romanCalendarToJulianDay(new RomanCalendarPoint(2000, 12, 31));
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            double cT = Timeline.julianDayToCenturialT(random.nextDouble() * jdLimit);
            assertForRootArgument(cT);
        }
    }

    private void assertForRootArgument(double cT) {
        double tau = Timeline.centurialTToMillenialTau(cT);
        double earthLongitude = earthLongitudeCalculator.calculateCoordinate(tau);
        moonCoordinateElements.calculate(cT);
        earthNutuationElements.calculate(cT);
        double earthSunRadius = earthSunRadiusCalculator.calculateCoordinate(tau);
        double sunGeometricLongitude = sunGeometricLongitudeCalculator.calculateGeometricLongitude(earthLongitude);
        double earthNutuationInLongitude = earthNutuationInLongitudeCalculator.calculateNutuation(cT, earthNutuationElements);
        double aberrationEarthSun = aberrationEarthSunCalculator.calculateAberration(tau, earthSunRadius);
        double moonLongitude = moonLongitudeCalculator.calculateCoordinate(cT, moonCoordinateElements);
        double earthLatitude = earthLatitudeCalculator.calculateCoordinate(tau);
        double sunLatitude = sunLatitudeCalculator.calculateLatitude(cT, earthLatitude, earthLongitude);
        double sunApparentLongitude = sunApparentLongitudeCalculator.calculateApparentLongitude(sunGeometricLongitude, earthNutuationInLongitude, aberrationEarthSun);
        double sunAberratedLongitude = sunAberratedLongitudeCalculator.calculateAberratedLongitude(sunGeometricLongitude, aberrationEarthSun);
        double moonLatitude = moonLatitudeCalculator.calculateCoordinate(cT, moonCoordinateElements);
        double moonEarthDistance = moonEarthDistanceCalculator.calculateCoordinate(cT, moonCoordinateElements);
        double moonApparentLongitude = moonApparentLongitudeCalculator.calculateApparentLongitude(moonLongitude, earthNutuationInLongitude);
        double moonOverSunApparentLongitudeExcess = moonOverSunApparentLongitudeExcessCalculator.calculateExcess(moonLongitude, sunAberratedLongitude);

        assertEquals(earthLongitude, getCompositionNumericResult(Subject.EARTH_LONGITUDE, cT), Calcs.EPSILON_MIN);
        assertEquals(earthSunRadius, getCompositionNumericResult(Subject.EARTH_SUN_RADIUS, cT), Calcs.EPSILON_MIN);
        assertEquals(sunGeometricLongitude, getCompositionNumericResult(Subject.SUN_GEOMETRIC_LONGITUDE, cT), Calcs.EPSILON_MIN);
        assertEquals(earthNutuationInLongitude, getCompositionNumericResult(Subject.EARTH_NUTUATION_IN_LONGITUDE, cT), Calcs.EPSILON_MIN);
        assertEquals(aberrationEarthSun, getCompositionNumericResult(Subject.ABERRATION_EARTH_SUN, cT), Calcs.EPSILON_MIN);
        assertEquals(moonLongitude, getCompositionNumericResult(Subject.MOON_LONGITUDE, cT), Calcs.EPSILON_MIN);
        assertEquals(earthLatitude, getCompositionNumericResult(Subject.EARTH_LATITUDE, cT), Calcs.EPSILON_MIN);
        assertEquals(sunLatitude, getCompositionNumericResult(Subject.SUN_LATITUDE, cT), Calcs.EPSILON_MIN);
        assertEquals(sunApparentLongitude, getCompositionNumericResult(Subject.SUN_APPARENT_LONGITUDE, cT), Calcs.EPSILON_MIN);
        assertEquals(sunAberratedLongitude, getCompositionNumericResult(Subject.SUN_ABERRATED_LONGITUDE, cT), Calcs.EPSILON_MIN);
        assertEquals(moonLatitude, getCompositionNumericResult(Subject.MOON_LATITUDE, cT), Calcs.EPSILON_MIN);
        assertEquals(moonEarthDistance, getCompositionNumericResult(Subject.MOON_EARTH_DISTANCE, cT), Calcs.EPSILON_MIN);
        assertEquals(moonApparentLongitude, getCompositionNumericResult(Subject.MOON_APPARENT_LONGITUDE, cT), Calcs.EPSILON_MIN);
        assertEquals(moonOverSunApparentLongitudeExcess, getCompositionNumericResult(Subject.MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS, cT), Calcs.EPSILON_MIN);
    }

    private double getCompositionNumericResult(Subject subject, double cT) {
        return (Double) subjectToComposition.get(subject).calculate(cT);
    }
}
