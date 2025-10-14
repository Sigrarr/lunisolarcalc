package com.github.sigrarr.lunisolarcalc.coords;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.coords.global.GlobalCoord;
import com.github.sigrarr.lunisolarcalc.coords.local.LocalCoord;
import com.github.sigrarr.lunisolarcalc.subjects.GeoPosition;
import com.github.sigrarr.lunisolarcalc.testing.*;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;

public class TopoTest {

    // Meeus 1998: Example 11.a, p. 83 + Example 40.a, p. 280
    private static class Example {
        static GeoPosition gp = ExampleLocation.PALOMAR_OBSERVATORY;
        static double rhoSinPhiPrime = 0.546861;
        static double rhoCosPhiPrime = 0.836339;
        static double declination = Math.toRadians(-15.771083);
        static double rightAscension = Math.toRadians(339.530208);
        static double ehParallax = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(23.592));
        static double theta0 = Math.toRadians(360.0 * Calcs.Time.timeToDays(1, 40, 45));
        static double localHourAngle = Math.toRadians(288.7958);
        static double parallaxInRightAscension = Math.toRadians(0.0053917);
        static double topocentricDeclination = Math.toRadians(Calcs.Angle.toSingleDegreesValue(-15, 46, 30));
    }

    @Test
    public void shouldCalculateParallaxInRightAscension() {
        double actual = Topo.calculateParallaxInRightAscension(
            Example.declination,
            Example.localHourAngle,
            Math.sin(Example.ehParallax),
            Example.rhoCosPhiPrime
        );
        double delta = Math.toRadians(TestUtils.decimalAutoDelta(0.0000001));
        assertEquals(Example.parallaxInRightAscension, actual, delta);
    }

    @Test
    public void shouldCalculateTopocentricRightAscension() {
        double actual = Topo.calculateTopocentricRightAscension(Example.rightAscension, Example.parallaxInRightAscension);
        assertEquals(Example.rightAscension + Example.parallaxInRightAscension, actual, Calcs.EPSILON_MIN);
    }

    @Test
    public void shouldCalculateTopocentricDeclination() {
        double actual = Topo.calculateTopocentricDeclination(
            Example.declination,
            Example.parallaxInRightAscension,
            Example.localHourAngle,
            Math.sin(Example.ehParallax),
            Example.rhoSinPhiPrime,
            Example.rhoCosPhiPrime
        );
        double delta = Math.toRadians(Calcs.Angle.arcsecondsToDegrees(0.5));
        assertEquals(Example.topocentricDeclination, actual, delta);
    }

    @Test
    public void shouldCalculateRhoSinPhiPrime() {
        double actual = Topo.calculateRhoSinPhiPrime(Example.gp.coords.getLatitude(), Example.gp.elevation);
        assertEquals(Example.rhoSinPhiPrime, actual, TestUtils.decimalAutoDelta(Example.rhoSinPhiPrime));
    }

    @Test
    public void shouldCalculateRhoCosPhiPrime() {
        double actual = Topo.calculateRhoCosPhiPrime(Example.gp.coords.getLatitude(), Example.gp.elevation);
        assertEquals(Example.rhoCosPhiPrime, actual, TestUtils.decimalAutoDelta(Example.rhoCosPhiPrime));
    }


    @Test
    public void shouldTopocentricHourAngleBeConsistent() {
        double fromGeocentric = Topo.calculateTopocentricLocalHourAngle(
            Example.localHourAngle,
            Example.declination,
            Math.sin(Example.ehParallax),
            Example.rhoCosPhiPrime
        );
        double fromTopocentricRightAscension = Transformations.calculateLocalHourAngle(
            Example.theta0,
            Example.gp.coords.getPlanetographicLongitude(),
            Example.rightAscension + Example.parallaxInRightAscension
        );

        assertEquals(fromTopocentricRightAscension, fromGeocentric, Math.toRadians(4*Calcs.ARCSECOND_TO_DEGREE));
    }

    @Test
    public void shouldApproximateTopocentricAltitude() {
        Map<Key, Object> v = CoordsCalcCompositions.compose(
            Sets.of(
                LocalCoord.MOON_ALTITUDE.key(),
                LocalCoord.MOON_TOPOCENTRIC_ALTITUDE.key(),
                GlobalCoord.MOON_PARALLAX_SINE.key()
            ),
            GeoPosition.of(ExampleLocation.WROCLAW, 10)
        ).calculate(Timeline.EPOCH_2000_UT);

        double geoAlt = (Double) v.get(LocalCoord.MOON_ALTITUDE.key());
        double formulaTopoAlt = (Double) v.get(LocalCoord.MOON_TOPOCENTRIC_ALTITUDE.key());
        double approxTopoAlt = Topo.approximateTopocentricAltitude(
            geoAlt,
            (Double) v.get(GlobalCoord.MOON_PARALLAX_SINE.key()),
            Topo.calculateGeocentricRadius(ExampleLocation.WROCLAW.getLatitude())
        );

        assumeTrue(formulaTopoAlt < geoAlt);
        assertTrue(approxTopoAlt < geoAlt);
        assertTrue(
                Math.abs(formulaTopoAlt - approxTopoAlt) < Math.abs(formulaTopoAlt - geoAlt)
            &&  Math.abs(formulaTopoAlt - approxTopoAlt) < Math.abs(approxTopoAlt - geoAlt)
        );

        geoAlt = Transformations.calculateAltitude(Example.declination, Example.localHourAngle, Example.gp.coords.getLatitude());
        formulaTopoAlt = Transformations.calculateAltitude(
            Example.topocentricDeclination,
            Topo.calculateTopocentricLocalHourAngle(
                Example.localHourAngle,
                Example.declination,
                Math.sin(Example.ehParallax),
                Example.rhoCosPhiPrime
            ),
            Example.gp.coords.getLatitude()
        );
        approxTopoAlt = Topo.approximateTopocentricAltitude(
            geoAlt,
            Math.sin(Example.ehParallax),
            Topo.calculateGeocentricRadius(Example.gp.coords.getLatitude())
        );

        assumeTrue(formulaTopoAlt < geoAlt);
        assertTrue(approxTopoAlt < geoAlt);
        assertTrue(
                Math.abs(formulaTopoAlt - approxTopoAlt) < Math.abs(formulaTopoAlt - geoAlt)
            &&  Math.abs(formulaTopoAlt - approxTopoAlt) < Math.abs(approxTopoAlt - geoAlt)
        );
    }
}
