package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.coords.global.*;
import com.github.sigrarr.lunisolarcalc.coords.local.*;
import com.github.sigrarr.lunisolarcalc.subjects.*;
import com.github.sigrarr.lunisolarcalc.testing.ExampleLocation;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.CalcComposition;

public class CoordsCalcCompositionsTest {

    private EarthNutuationElements earthNutuationElements = EarthNutuationElements.makeUnevaluatedInstance();
    private MoonCoordinateElements moonCoordinateElements = MoonCoordinateElements.makeUnevaluatedInstance();
    private EarthLongitudeCalculator earthLongitudeCalculator = new EarthLongitudeCalculator();
    private EarthSunRadiusCalculator earthSunRadiusCalculator = new EarthSunRadiusCalculator();
    private SunParallaxSineCalculator sunParallaxSineCalculator = new SunParallaxSineCalculator();
    private ParallaxCalculator sunParallaxCalculator = new ParallaxCalculator(Body.SUN);
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
    private DeclinationCalculator sunDeclinationCalculator = new DeclinationCalculator(Body.SUN);
    private RightAscensionCalculator sunRightAscensionCalculator = new RightAscensionCalculator(Body.SUN);
    private HourAngleCalculator sunHourAngleCalculator = new HourAngleCalculator(Body.SUN);
    private MoonLatitudeCalculator moonLatitudeCalculator = new MoonLatitudeCalculator();
    private MoonEarthDistanceCalculator moonEarthDistanceCalculator = new MoonEarthDistanceCalculator();
    private MoonParallaxSineCalculator moonParallaxSineCalculator = new MoonParallaxSineCalculator();
    private ParallaxCalculator moonParallaxCalculator = new ParallaxCalculator(Body.MOON);
    private MoonApparentLongitudeCalculator moonApparentLongitudeCalculator = new MoonApparentLongitudeCalculator();
    private MoonOverSunApparentLongitudeExcessCalculator moonOverSunApparentLongitudeExcessCalculator = new MoonOverSunApparentLongitudeExcessCalculator();
    private DeclinationCalculator moonDeclinationCalculator = new DeclinationCalculator(Body.MOON);
    private RightAscensionCalculator moonRightAscensionCalculator = new RightAscensionCalculator(Body.MOON);
    private HourAngleCalculator moonHourAngleCalculator = new HourAngleCalculator(Body.MOON);
    private MoonSunElongationCalculator moonSunElongationCalculator = new MoonSunElongationCalculator();
    private LocalHourAngleCalculator moonLocalHourAngleCalculator = new LocalHourAngleCalculator(Body.MOON, ExampleLocation.WROCLAW);
    private AltitudeCalculator moonAltitudeCalculator = new AltitudeCalculator(Body.MOON, ExampleLocation.WROCLAW);
    private AzimuthCalculator moonAzimuthCalculator = new AzimuthCalculator(Body.MOON, ExampleLocation.WROCLAW);
    private ParallaxInRightAscensionCalculator moonParallaxInRightAscensionCalculator = new ParallaxInRightAscensionCalculator(Body.MOON);
    private TopocentricDeclinationCalculator moonTopocentricDeclinationCalculator = new TopocentricDeclinationCalculator(Body.MOON);
    private TopocentricRightAscensionCalculator moonTopocentricRightAscensionCalculator = new TopocentricRightAscensionCalculator(Body.MOON);
    private TopocentricLocalHourAngleCalculator moonTopocentricLocalHourAngleCalculator = new TopocentricLocalHourAngleCalculator(Body.MOON);
    private TopocentricAltitudeCalculator moonTopocentricAltitudeCalculator = new TopocentricAltitudeCalculator(Body.MOON, ExampleLocation.WROCLAW);
    private TopocentricAzimuthCalculator moonTopocentricAzimuthCalculator = new TopocentricAzimuthCalculator(Body.MOON, ExampleLocation.WROCLAW);
    private LocalHourAngleCalculator sunLocalHourAngleCalculator = new LocalHourAngleCalculator(Body.SUN, ExampleLocation.WROCLAW);
    private AltitudeCalculator sunAltitudeCalculator = new AltitudeCalculator(Body.SUN, ExampleLocation.WROCLAW);
    private AzimuthCalculator sunAzimuthCalculator = new AzimuthCalculator(Body.SUN, ExampleLocation.WROCLAW);
    private ParallaxInRightAscensionCalculator sunParallaxInRightAscensionCalculator = new ParallaxInRightAscensionCalculator(Body.SUN);
    private TopocentricDeclinationCalculator sunTopocentricDeclinationCalculator = new TopocentricDeclinationCalculator(Body.SUN);
    private TopocentricRightAscensionCalculator sunTopocentricRightAscensionCalculator = new TopocentricRightAscensionCalculator(Body.SUN);
    private TopocentricLocalHourAngleCalculator sunTopocentricLocalHourAngleCalculator = new TopocentricLocalHourAngleCalculator(Body.SUN);
    private TopocentricAltitudeCalculator sunTopocentricAltitudeCalculator = new TopocentricAltitudeCalculator(Body.SUN, ExampleLocation.WROCLAW);
    private TopocentricAzimuthCalculator sunTopocentricAzimuthCalculator = new TopocentricAzimuthCalculator(Body.SUN, ExampleLocation.WROCLAW);
    private RhoSinPhiPrimeCalculator rhoSinPhiPrimeCalculator = new RhoSinPhiPrimeCalculator(GeoPosition.of(ExampleLocation.WROCLAW));
    private RhoCosPhiPrimeCalculator rhoCosPhiPrimeCalculator = new RhoCosPhiPrimeCalculator(GeoPosition.of(ExampleLocation.WROCLAW));
    private Map<Key, CalcComposition<Key, TimelinePoint>> keyToComposition = Stream.concat(
        Arrays.stream(GlobalCoord.values()).map(Key.QuantityIndetifier::key),
        Arrays.stream(LocalCoord.values()).map(Key.QuantityIndetifier::key)
    ).collect(Collectors.toMap(k -> k, k -> CoordsCalcCompositions.compose(k, GeoPosition.of(ExampleLocation.WROCLAW))));

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
        double sunParallaxSine = sunParallaxSineCalculator.calculate(earthSunRadius);
        double sunParallax = sunParallaxCalculator.calculate(sunParallaxSine);
        double eclipticMeanObliquity = eclipticMeanObliquityCalculator.calculate(tx);
        double sunGeometricLongitude = sunGeometricLongitudeCalculator.calculate(earthLongitude);
        double earthNutuationInLongitude = earthNutuationInLongitudeCalculator.calculate(tx, earthNutuationElements);
        double earthNutuationInObliquity = earthNutuationInObliquityCalculator.calculate(tx, earthNutuationElements);
        double eclipticTrueObliquity = eclipticTrueObliquityCalculator.calculate(eclipticMeanObliquity, earthNutuationInObliquity);
        double siderealMeanTime0 = siderealMeanTimeCalculator.calculate(tx);
        double siderealApparentTime0 = siderealApparentTimeCalculator.calculate(siderealMeanTime0, earthNutuationInLongitude, eclipticTrueObliquity);
        double aberrationEarthSun = aberrationEarthSunCalculator.calculate(tx, earthSunRadius);
        double moonLongitude = moonLongitudeCalculator.calculate(tx, moonCoordinateElements);
        double earthLatitude = earthLatitudeCalculator.calculate(tx);
        double sunLatitude = sunLatitudeCalculator.calculate(tx, earthLatitude, earthLongitude);
        double sunApparentLongitude = sunApparentLongitudeCalculator.calculate(sunGeometricLongitude, earthNutuationInLongitude, aberrationEarthSun);
        double sunAberratedLongitude = sunAberratedLongitudeCalculator.calculate(sunGeometricLongitude, aberrationEarthSun);
        double sunDeclination = sunDeclinationCalculator.calculate(sunLatitude, sunApparentLongitude, eclipticTrueObliquity);
        double sunRightAscension = sunRightAscensionCalculator.calculate(sunApparentLongitude, sunLatitude, eclipticTrueObliquity);
        double sunHourAngle0 = sunHourAngleCalculator.calculate(siderealApparentTime0, sunRightAscension);
        double moonLatitude = moonLatitudeCalculator.calculate(tx, moonCoordinateElements);
        double moonEarthDistance = moonEarthDistanceCalculator.calculate(tx, moonCoordinateElements);
        double moonParallaxSine = moonParallaxSineCalculator.calculate(moonEarthDistance);
        double moonParallax = moonParallaxCalculator.calculate(moonParallaxSine);
        double moonApparentLongitude = moonApparentLongitudeCalculator.calculate(moonLongitude, earthNutuationInLongitude);
        double moonOverSunApparentLongitudeExcess = moonOverSunApparentLongitudeExcessCalculator.calculate(moonLongitude, sunAberratedLongitude);
        double moonDeclination = moonDeclinationCalculator.calculate(moonLatitude, moonApparentLongitude, eclipticTrueObliquity);
        double moonRightAscension = moonRightAscensionCalculator.calculate(moonApparentLongitude, moonLatitude, eclipticTrueObliquity);
        double moonHourAngle0 = moonHourAngleCalculator.calculate(siderealApparentTime0, moonRightAscension);
        double moonSunElongation = moonSunElongationCalculator.calculate(moonLatitude, moonApparentLongitude, sunLatitude, sunApparentLongitude);
        double rhoSinPhiPrime = rhoSinPhiPrimeCalculator.calculate();
        double rhoCosPhiPrime = rhoCosPhiPrimeCalculator.calculate();
        double moonLocalHourAngle = moonLocalHourAngleCalculator.calculate(siderealApparentTime0, moonRightAscension);
        double moonAltitude = moonAltitudeCalculator.calculate(moonDeclination, moonLocalHourAngle);
        double moonAzimuth = moonAzimuthCalculator.calculate(moonLocalHourAngle, moonDeclination);
        double moonParallaxInRightAscension = moonParallaxInRightAscensionCalculator.calculate(moonDeclination, moonLocalHourAngle, moonParallaxSine, rhoCosPhiPrime);
        double moonTopocentricDeclination = moonTopocentricDeclinationCalculator.calculate(moonDeclination, moonParallaxInRightAscension, moonLocalHourAngle, moonParallaxSine, rhoSinPhiPrime, rhoCosPhiPrime);
        double moonTopocentricRightAscension = moonTopocentricRightAscensionCalculator.calculate(moonRightAscension, moonParallaxInRightAscension);
        double moonTopocentricLocalHourAngle = moonTopocentricLocalHourAngleCalculator.calculate(moonLocalHourAngle, moonDeclination, moonParallaxSine, rhoCosPhiPrime);
        double moonTopocentricAltitude = moonTopocentricAltitudeCalculator.calculate(moonTopocentricDeclination, moonTopocentricLocalHourAngle);
        double moonTopocentricAzimuth = moonTopocentricAzimuthCalculator.calculate(moonTopocentricLocalHourAngle, moonTopocentricDeclination);
        double sunLocalHourAngle = sunLocalHourAngleCalculator.calculate(siderealApparentTime0, sunRightAscension);
        double sunAltitude = sunAltitudeCalculator.calculate(sunDeclination, sunLocalHourAngle);
        double sunAzimuth = sunAzimuthCalculator.calculate(sunLocalHourAngle, sunDeclination);
        double sunParallaxInRightAscension = sunParallaxInRightAscensionCalculator.calculate(sunDeclination, sunLocalHourAngle, sunParallaxSine, rhoCosPhiPrime);
        double sunTopocentricDeclination = sunTopocentricDeclinationCalculator.calculate(sunDeclination, sunParallaxInRightAscension, sunLocalHourAngle, sunParallaxSine, rhoSinPhiPrime, rhoCosPhiPrime);
        double sunTopocentricRightAscension = sunTopocentricRightAscensionCalculator.calculate(sunRightAscension, sunParallaxInRightAscension);
        double sunTopocentricLocalHourAngle = sunTopocentricLocalHourAngleCalculator.calculate(sunLocalHourAngle, sunDeclination, sunParallaxSine, rhoCosPhiPrime);
        double sunTopocentricAltitude = sunTopocentricAltitudeCalculator.calculate(sunTopocentricDeclination, sunTopocentricLocalHourAngle);
        double sunTopocentricAzimuth = sunTopocentricAzimuthCalculator.calculate(sunTopocentricLocalHourAngle, sunTopocentricDeclination);

        assertForElements(moonCoordinateElements, GlobalCoord.MOON_COORDINATE_ELEMENTS);
        assertForElements(earthNutuationElements, GlobalCoord.EARTH_NUTUATION_ELEMENTS);
        assertForNumber(earthLongitude, GlobalCoord.EARTH_LONGITUDE);
        assertForNumber(earthSunRadius, GlobalCoord.EARTH_SUN_RADIUS);
        assertForNumber(sunParallaxSine, GlobalCoord.SUN_PARALLAX_SINE);
        assertForNumber(sunParallax, GlobalCoord.SUN_PARALLAX);
        assertForNumber(eclipticMeanObliquity, GlobalCoord.ECLIPTIC_MEAN_OBLIQUITY);
        assertForNumber(sunGeometricLongitude, GlobalCoord.SUN_GEOMETRIC_LONGITUDE);
        assertForNumber(earthNutuationInLongitude, GlobalCoord.EARTH_NUTUATION_IN_LONGITUDE);
        assertForNumber(earthNutuationInObliquity, GlobalCoord.EARTH_NUTUATION_IN_OBLIQUITY);
        assertForNumber(eclipticTrueObliquity, GlobalCoord.ECLIPTIC_TRUE_OBLIQUITY);
        assertForNumber(siderealMeanTime0, GlobalCoord.SIDEREAL_MEAN_TIME_0);
        assertForNumber(siderealApparentTime0, GlobalCoord.SIDEREAL_APPARENT_TIME_0);
        assertForNumber(aberrationEarthSun, GlobalCoord.ABERRATION_EARTH_SUN);
        assertForNumber(moonLongitude, GlobalCoord.MOON_LONGITUDE);
        assertForNumber(earthLatitude, GlobalCoord.EARTH_LATITUDE);
        assertForNumber(sunLatitude, GlobalCoord.SUN_LATITUDE);
        assertForNumber(sunApparentLongitude, GlobalCoord.SUN_APPARENT_LONGITUDE);
        assertForNumber(sunAberratedLongitude, GlobalCoord.SUN_ABERRATED_LONGITUDE);
        assertForNumber(sunDeclination, GlobalCoord.SUN_DECLINATION);
        assertForNumber(sunRightAscension, GlobalCoord.SUN_RIGHT_ASCENSION);
        assertForNumber(sunHourAngle0, GlobalCoord.SUN_HOUR_ANGLE_0);
        assertForNumber(moonLatitude, GlobalCoord.MOON_LATITUDE);
        assertForNumber(moonEarthDistance, GlobalCoord.MOON_EARTH_DISTANCE);
        assertForNumber(moonParallaxSine, GlobalCoord.MOON_PARALLAX_SINE);
        assertForNumber(moonParallax, GlobalCoord.MOON_PARALLAX);
        assertForNumber(moonApparentLongitude, GlobalCoord.MOON_APPARENT_LONGITUDE);
        assertForNumber(moonOverSunApparentLongitudeExcess, GlobalCoord.MOON_OVER_SUN_APPARENT_LONGITUDE_EXCESS);
        assertForNumber(moonDeclination, GlobalCoord.MOON_DECLINATION);
        assertForNumber(moonRightAscension, GlobalCoord.MOON_RIGHT_ASCENSION);
        assertForNumber(moonHourAngle0, GlobalCoord.MOON_HOUR_ANGLE_0);
        assertForNumber(moonSunElongation, GlobalCoord.MOON_SUN_ELONGATION);
        assertForNumber(rhoSinPhiPrime, LocalCoord.RHO_SIN_PHI_PRIME);
        assertForNumber(rhoCosPhiPrime, LocalCoord.RHO_COS_PHI_PRIME);
        assertForNumber(moonLocalHourAngle, LocalCoord.MOON_LOCAL_HOUR_ANGLE);
        assertForNumber(moonAltitude, LocalCoord.MOON_ALTITUDE);
        assertForNumber(moonAzimuth, LocalCoord.MOON_AZIMUTH);
        assertForNumber(moonParallaxInRightAscension, LocalCoord.MOON_PARALLAX_IN_RIGHT_ASCENSION);
        assertForNumber(moonTopocentricDeclination, LocalCoord.MOON_TOPOCENTRIC_DECLINATION);
        assertForNumber(moonTopocentricRightAscension, LocalCoord.MOON_TOPOCENTRIC_RIGHT_ASCENSION);
        assertForNumber(moonTopocentricLocalHourAngle, LocalCoord.MOON_TOPOCENTRIC_LOCAL_HOUR_ANGLE);
        assertForNumber(moonTopocentricAltitude, LocalCoord.MOON_TOPOCENTRIC_ALTITUDE);
        assertForNumber(moonTopocentricAzimuth, LocalCoord.MOON_TOPOCENTRIC_AZIMUTH);
        assertForNumber(sunLocalHourAngle, LocalCoord.SUN_LOCAL_HOUR_ANGLE);
        assertForNumber(sunAltitude, LocalCoord.SUN_ALTITUDE);
        assertForNumber(sunAzimuth, LocalCoord.SUN_AZIMUTH);
        assertForNumber(sunParallaxInRightAscension, LocalCoord.SUN_PARALLAX_IN_RIGHT_ASCENSION);
        assertForNumber(sunTopocentricDeclination, LocalCoord.SUN_TOPOCENTRIC_DECLINATION);
        assertForNumber(sunTopocentricRightAscension, LocalCoord.SUN_TOPOCENTRIC_RIGHT_ASCENSION);
        assertForNumber(sunTopocentricLocalHourAngle, LocalCoord.SUN_TOPOCENTRIC_LOCAL_HOUR_ANGLE);
        assertForNumber(sunTopocentricAltitude, LocalCoord.SUN_TOPOCENTRIC_ALTITUDE);
        assertForNumber(sunTopocentricAzimuth, LocalCoord.SUN_TOPOCENTRIC_AZIMUTH);
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
