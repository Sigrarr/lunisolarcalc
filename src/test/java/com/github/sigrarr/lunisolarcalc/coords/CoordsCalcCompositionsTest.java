package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.Body;
import com.github.sigrarr.lunisolarcalc.coords.global.*;
import com.github.sigrarr.lunisolarcalc.coords.local.*;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.tutil.ExampleLocation;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.CalcComposition;

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
    private MoonSunElongationCalculator moonSunElongationCalculator = new MoonSunElongationCalculator();
    private AltitudeCalculator moonAltitudeCalculator = new AltitudeCalculator(Body.MOON, ExampleLocation.WROCLAW);
    private AzimuthCalculator moonAzimuthCalculator = new AzimuthCalculator(Body.MOON, ExampleLocation.WROCLAW);
    private AltitudeCalculator sunAltitudeCalculator = new AltitudeCalculator(Body.SUN, ExampleLocation.WROCLAW);
    private AzimuthCalculator sunAzimuthCalculator = new AzimuthCalculator(Body.SUN, ExampleLocation.WROCLAW);
    private Map<Key, CalcComposition<Key, TimelinePoint>> keyToComposition = Stream.concat(
        Arrays.stream(GlobalCoord.values()).map(Key.QuantityIndetifier::key),
        Arrays.stream(LocalCoord.values()).map(Key.QuantityIndetifier::key)
    ).collect(Collectors.toMap(k -> k, k -> CoordsCalcCompositions.compose(k, ExampleLocation.WROCLAW)));

    private TimelinePoint tx;
    private int checkedSubjectsCount = 0;

    @Test
    public void shouldCompositionsAndCoreCalculatorsGiveEqualResults() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            tx = new DynamicalTimelinePoint(random.nextDouble() * Timeline.JULIAN_PERIOD_END_JD);
            checkedSubjectsCount = 0;
            assertForCurrentRootArgument();
            assumeTrue(GlobalCoord.values().length + LocalCoord.values().length == checkedSubjectsCount);
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
        double moonSunElongation = moonSunElongationCalculator.calculate(moonLatitude, moonApparentLongitude, sunLatitude, sunApparentLongitude);
        double moonAltitude = moonAltitudeCalculator.calculate(moonDeclination, moonHourAngle);
        double moonAzimuth = moonAzimuthCalculator.calculate(moonHourAngle, moonDeclination);
        double sunAltitude = sunAltitudeCalculator.calculate(sunDeclination, sunHourAngle);
        double sunAzimuth = sunAzimuthCalculator.calculate(sunHourAngle, sunDeclination);

        assertForElements(moonCoordinateElements, GlobalCoord.MOON_COORDINATE_ELEMENTS);
        assertForElements(earthNutuationElements, GlobalCoord.EARTH_NUTUATION_ELEMENTS);
        assertForNumber(earthLongitude, GlobalCoord.EARTH_LONGITUDE);
        assertForNumber(earthSunRadius, GlobalCoord.EARTH_SUN_RADIUS);
        assertForNumber(eclipticMeanObliquity, GlobalCoord.ECLIPTIC_MEAN_OBLIQUITY);
        assertForNumber(sunGeometricLongitude, GlobalCoord.SUN_GEOMETRIC_LONGITUDE);
        assertForNumber(earthNutuationInLongitude, GlobalCoord.EARTH_NUTUATION_IN_LONGITUDE);
        assertForNumber(earthNutuationInObliquity, GlobalCoord.EARTH_NUTUATION_IN_OBLIQUITY);
        assertForNumber(eclipticTrueObliquity, GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY);
        assertForNumber(siderealMeanTime, GlobalCoord.SIDEREAL_MEAN_TIME);
        assertForNumber(siderealApparentTime, GlobalCoord.SIDEREAL_APPARENT_TIME);
        assertForNumber(aberrationEarthSun, GlobalCoord.ABERRATION_EARTH_SUN);
        assertForNumber(moonLongitude, GlobalCoord.MOON_LONGITUDE);
        assertForNumber(earthLatitude, GlobalCoord.EARTH_LATITUDE);
        assertForNumber(sunLatitude, GlobalCoord.SUN_LATITUDE);
        assertForNumber(sunApparentLongitude, GlobalCoord.SUN_APPARENT_LONGITUDE);
        assertForNumber(sunAberratedLongitude, GlobalCoord.SUN_ABERRATED_LONGITUDE);
        assertForNumber(sunDeclination, GlobalCoord.SUN_DECLINATION);
        assertForNumber(sunRightAscension, GlobalCoord.SUN_RIGHT_ASCENSION);
        assertForNumber(sunHourAngle, GlobalCoord.SUN_HOUR_ANGLE);
        assertForNumber(moonLatitude, GlobalCoord.MOON_LATITUDE);
        assertForNumber(moonEarthDistance, GlobalCoord.MOON_EARTH_DISTANCE);
        assertForNumber(moonEquatorialHorizontalParallax, GlobalCoord.MOON_EQUATORIAL_HORIZONTAL_PARALLAX);
        assertForNumber(moonApparentLongitude, GlobalCoord.MOON_APPARENT_LONGITUDE);
        assertForNumber(moonOverSunApparentLongitudeExcess, GlobalCoord.MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS);
        assertForNumber(moonDeclination, GlobalCoord.MOON_DECLINATION);
        assertForNumber(moonRightAscension, GlobalCoord.MOON_RIGHT_ASCENSION);
        assertForNumber(moonHourAngle, GlobalCoord.MOON_HOUR_ANGLE);
        assertForNumber(moonSunElongation, GlobalCoord.MOON_SUN_ELONGATION);
        assertForNumber(moonAltitude, LocalCoord.MOON_ALTITUDE);
        assertForNumber(moonAzimuth, LocalCoord.MOON_AZIMUTH);
        assertForNumber(sunAltitude, LocalCoord.SUN_ALTITUDE);
        assertForNumber(sunAzimuth, LocalCoord.SUN_AZIMUTH);
    }

    private void assertForElements(DoubleRow elements, GlobalCoord subject) {
        DoubleRow byComposition = (DoubleRow) keyToComposition.get(subject.key()).calculate(tx);
        for (int i = 0; i < elements.getSize(); i++)
            assertEquals(elements.getValue(i), byComposition.getValue(i), Calcs.EPSILON_MIN);
        checkedSubjectsCount++;
    }

    private void assertForNumber(double value, Key.QuantityIndetifier coord) {
        double byComposition = (Double) keyToComposition.get(coord.key()).calculate(tx);
        assertEquals(value, byComposition, Calcs.EPSILON_MIN);
        checkedSubjectsCount++;
    }
}
