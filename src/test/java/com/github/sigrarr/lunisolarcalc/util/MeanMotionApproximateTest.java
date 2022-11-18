package com.github.sigrarr.lunisolarcalc.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MeanMotionApproximateTest {

    private static final double DELTA_DEGREES = Calcs.arcsecondsToDegrees(0.25);
    private static final double DELTA_RADIANS = Math.toRadians(DELTA_DEGREES);

    @Test
    public void shouldCalculateAngleFromTime() {
        for (MeanMotionApproximate approximate : MeanMotionApproximate.values()) {
            assertEquals(0.0, approximate.degreesPerTimeSeconds(0));
            assertEquals(0.0, approximate.radiansPerTimeSeconds(0));
            assertEquals(0.0, approximate.degreesPerTimeMiliseconds(0));
            assertEquals(0.0, approximate.arcsecondsPerTimeMiliseconds(0));

            double percentSeconds = approximate.lengthSeconds * 0.01;
            assertEquals(0.01 * 360, approximate.degreesPerTimeSeconds((int) Math.round(percentSeconds)), DELTA_DEGREES);
            assertEquals(0.01 * Calcs.ROUND, approximate.radiansPerTimeSeconds((int) Math.round(percentSeconds)), DELTA_RADIANS);
            assertEquals(0.01 * 360, approximate.degreesPerTimeMiliseconds((int) Math.round(1000.0 * percentSeconds)), DELTA_DEGREES);
            assertEquals(Calcs.toArcseconds(0.01 * 360), approximate.arcsecondsPerTimeMiliseconds((int) Math.round(1000.0 * percentSeconds)), 0.001);
        }
    }

    @Test
    public void shouldCalculateTimeFromAngle() {
        for (MeanMotionApproximate approximate : MeanMotionApproximate.values()) {
            assertEquals(0.0, approximate.secondsPerRadians(0.0));
            assertEquals(0.0, approximate.secondsPerDegrees(0.0));

            double percent = Calcs.ROUND * 0.01;
            double percendDegrees = Math.toDegrees(percent);
            assertEquals(approximate.getLengthSeconds() * 0.01, approximate.secondsPerRadians(percent), 0.0005);
            assertEquals(approximate.getLengthSeconds() * 0.01, approximate.secondsPerDegrees(percendDegrees), 0.0005);
        }
    }

}
