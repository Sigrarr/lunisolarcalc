package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.SingleOutputComposition;

public class CoordsCalcCompositionsTest {

    private EarthNutuationElements earthNutuationElements = EarthNutuationElements.makeUnevaluatedInstance();
    private MoonCoordinateElements moonCoordinateElements = MoonCoordinateElements.makeUnevaluatedInstance();
    private EarthLongitudeCalculator earthLongitudeCalculator = new EarthLongitudeCalculator();
    private EarthSunRadiusCalculator earthSunRadiusCalculator = new EarthSunRadiusCalculator();
    private EclipticMeanObliquityCalculator eclipticMeanObliquityCalculator = new EclipticMeanObliquityCalculator();
    private EclipticTrueObliquityCalculator eclipticTrueObliquityCalculator = new EclipticTrueObliquityCalculator();
    private SunGeometricLongitudeCalculator sunGeometricLongitudeCalculator = new SunGeometricLongitudeCalculator();
    private EarthNutuationInLongitudeCalculator earthNutuationInLongitudeCalculator = new EarthNutuationInLongitudeCalculator();
    private EarthNutuationInObliquityCalculator earthNutuationInObliquityCalculator = new EarthNutuationInObliquityCalculator();
    private AberrationEarthSunCalculator aberrationEarthSunCalculator = new AberrationEarthSunCalculator();
    private MoonLongitudeCalculator moonLongitudeCalculator = new MoonLongitudeCalculator();
    private EarthLatitudeCalculator earthLatitudeCalculator = new EarthLatitudeCalculator();
    private SiderealMeanTimeCalculator siderealMeanTimeCalculator = new SiderealMeanTimeCalculator();
    private SiderealApparentTimeCalculator siderealApparentTimeCalculator = new SiderealApparentTimeCalculator();
    private SunLatitudeCalculator sunLatitudeCalculator = new SunLatitudeCalculator();
    private SunApparentLongitudeCalculator sunApparentLongitudeCalculator = new SunApparentLongitudeCalculator();
    private SunAberratedLongitudeCalculator sunAberratedLongitudeCalculator = new SunAberratedLongitudeCalculator();
    private SunDeclinationCalculator sunDeclinationCalculator = new SunDeclinationCalculator();
    private SunRightAscensionCalculator sunRightAscensionCalculator = new SunRightAscensionCalculator();
    private SunHourAngleCalculator sunHourAngleCalculator = new SunHourAngleCalculator();
    private MoonLatitudeCalculator moonLatitudeCalculator = new MoonLatitudeCalculator();
    private MoonEarthDistanceCalculator moonEarthDistanceCalculator = new MoonEarthDistanceCalculator();
    private MoonEquatorialHorizontalParallaxCalculator moonEquatorialHorizontalParallaxCalculator = new MoonEquatorialHorizontalParallaxCalculator();
    private MoonApparentLongitudeCalculator moonApparentLongitudeCalculator = new MoonApparentLongitudeCalculator();
    private MoonOverSunApparentLongitudeExcessCalculator moonOverSunApparentLongitudeExcessCalculator = new MoonOverSunApparentLongitudeExcessCalculator();
    private MoonDeclinationCalculator moonDeclinationCalculator = new MoonDeclinationCalculator();
    private MoonRightAscensionCalculator moonRightAscensionCalculator = new MoonRightAscensionCalculator();
    private MoonHourAngleCalculator moonHourAngleCalculator = new MoonHourAngleCalculator();
    private Map<Subject, SingleOutputComposition<Subject, TimelinePoint>> subjectToComposition = Arrays.stream(Subject.values())
        .collect(Collectors.toMap(s -> s, s -> CoordsCalcCompositions.compose(s)));

    private TimelinePoint tx;
    private int checkedSubjectsCount = 0;

    @Test
    public void shouldCompositionsAndCoreCalculatorsGiveEqualResults() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            tx = new DynamicalTimelinePoint(random.nextDouble() * Timeline.JULIAN_PERIOD_END_JD);
            checkedSubjectsCount = 0;
            assertForCurrentRootArgument();
            assertEquals(Subject.values().length, checkedSubjectsCount);
        }
    }

    private void assertForCurrentRootArgument() {
        moonCoordinateElements.calculate(tx);
        earthNutuationElements.calculate(tx);
        double earthLongitude = earthLongitudeCalculator.calculate(tx);
        double earthSunRadius = earthSunRadiusCalculator.calculate(tx);
        double eclipticMeanObliquity = eclipticMeanObliquityCalculator.calculate(tx);
        double sunGeometricLongitude = sunGeometricLongitudeCalculator.calculate(earthLongitude);
        double earthNutuationInLongitude = earthNutuationInLongitudeCalculator.calculate(tx, earthNutuationElements);
        double earthNutuationInObliquity = earthNutuationInObliquityCalculator.calculate(tx, earthNutuationElements);
        double eclipticTrueObliquity = eclipticTrueObliquityCalculator.calculate(eclipticMeanObliquity, earthNutuationInObliquity);
        double siderealMeanTime = siderealMeanTimeCalculator.calculate(tx);
        double siderealApparentTime = siderealApparentTimeCalculator.calculate(siderealMeanTime, earthNutuationInLongitude, eclipticTrueObliquity);
        double aberrationEarthSun = aberrationEarthSunCalculator.calculate(tx, earthSunRadius);
        double moonLongitude = moonLongitudeCalculator.calculate(tx, moonCoordinateElements);
        double earthLatitude = earthLatitudeCalculator.calculate(tx);
        double sunLatitude = sunLatitudeCalculator.calculate(tx, earthLatitude, earthLongitude);
        double sunApparentLongitude = sunApparentLongitudeCalculator.calculate(sunGeometricLongitude, earthNutuationInLongitude, aberrationEarthSun);
        double sunAberratedLongitude = sunAberratedLongitudeCalculator.calculate(sunGeometricLongitude, aberrationEarthSun);
        double sunDeclination = sunDeclinationCalculator.calculate(sunLatitude, sunApparentLongitude, eclipticTrueObliquity);
        double sunRightAscension = sunRightAscensionCalculator.calculate(sunApparentLongitude, sunLatitude, eclipticTrueObliquity);
        double sunHourAngle = sunHourAngleCalculator.calculate(siderealApparentTime, sunRightAscension);
        double moonLatitude = moonLatitudeCalculator.calculate(tx, moonCoordinateElements);
        double moonEarthDistance = moonEarthDistanceCalculator.calculate(tx, moonCoordinateElements);
        double moonEquatorialHorizontalParallax = moonEquatorialHorizontalParallaxCalculator.calculate(moonEarthDistance);
        double moonApparentLongitude = moonApparentLongitudeCalculator.calculate(moonLongitude, earthNutuationInLongitude);
        double moonOverSunApparentLongitudeExcess = moonOverSunApparentLongitudeExcessCalculator.calculate(moonLongitude, sunAberratedLongitude);
        double moonDeclination = moonDeclinationCalculator.calculate(moonLatitude, moonApparentLongitude, eclipticTrueObliquity);
        double moonRightAscension = moonRightAscensionCalculator.calculate(moonApparentLongitude, moonLatitude, eclipticTrueObliquity);
        double moonHourAngle = moonHourAngleCalculator.calculate(siderealApparentTime, moonRightAscension);

        assertForElements(moonCoordinateElements, Subject.MOON_COORDINATE_ELEMENTS);
        assertForElements(earthNutuationElements, Subject.EARTH_NUTUATION_ELEMENTS);
        assertForNumber(earthLongitude, Subject.EARTH_LONGITUDE);
        assertForNumber(earthSunRadius, Subject.EARTH_SUN_RADIUS);
        assertForNumber(eclipticMeanObliquity, Subject.ECLIPTIC_MEAN_OBLIQUITY);
        assertForNumber(sunGeometricLongitude, Subject.SUN_GEOMETRIC_LONGITUDE);
        assertForNumber(earthNutuationInLongitude, Subject.EARTH_NUTUATION_IN_LONGITUDE);
        assertForNumber(earthNutuationInObliquity, Subject.EARTH_NUTUATION_IN_OBLIQUITY);
        assertForNumber(eclipticTrueObliquity, Subject.ECLIPTIC_TRUE_OBLIQUITY);
        assertForNumber(siderealMeanTime, Subject.SIDEREAL_MEAN_TIME);
        assertForNumber(siderealApparentTime, Subject.SIDEREAL_APPARENT_TIME);
        assertForNumber(aberrationEarthSun, Subject.ABERRATION_EARTH_SUN);
        assertForNumber(moonLongitude, Subject.MOON_LONGITUDE);
        assertForNumber(earthLatitude, Subject.EARTH_LATITUDE);
        assertForNumber(sunLatitude, Subject.SUN_LATITUDE);
        assertForNumber(sunApparentLongitude, Subject.SUN_APPARENT_LONGITUDE);
        assertForNumber(sunAberratedLongitude, Subject.SUN_ABERRATED_LONGITUDE);
        assertForNumber(sunDeclination, Subject.SUN_DECLINATION);
        assertForNumber(sunRightAscension, Subject.SUN_RIGHT_ASCENSION);
        assertForNumber(sunHourAngle, Subject.SUN_HOUR_ANGLE);
        assertForNumber(moonLatitude, Subject.MOON_LATITUDE);
        assertForNumber(moonEarthDistance, Subject.MOON_EARTH_DISTANCE);
        assertForNumber(moonEquatorialHorizontalParallax, Subject.MOON_EQUATORIAL_HORIZONTA_PARALLAX);
        assertForNumber(moonApparentLongitude, Subject.MOON_APPARENT_LONGITUDE);
        assertForNumber(moonOverSunApparentLongitudeExcess, Subject.MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS);
        assertForNumber(moonDeclination, Subject.MOON_DECLINATION);
        assertForNumber(moonRightAscension, Subject.MOON_RIGHT_ASCENSION);
        assertForNumber(moonHourAngle, Subject.MOON_HOUR_ANGLE);
    }

    private void assertForElements(DoubleRow elements, Subject subject) {
        DoubleRow byComposition = (DoubleRow) subjectToComposition.get(subject).calculate(tx);
        for (int i = 0; i < elements.getSize(); i++)
            assertEquals(elements.getValue(i), byComposition.getValue(i), Calcs.EPSILON_MIN);
        checkedSubjectsCount++;
    }

    private void assertForNumber(double value, Subject subject) {
        double byComposition = (Double) subjectToComposition.get(subject).calculate(tx);
        assertEquals(value, byComposition, Calcs.EPSILON_MIN);
        checkedSubjectsCount++;
    }
}
