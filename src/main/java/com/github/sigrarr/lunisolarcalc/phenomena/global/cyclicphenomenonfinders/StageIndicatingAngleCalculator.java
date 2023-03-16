package com.github.sigrarr.lunisolarcalc.phenomena.global.cyclicphenomenonfinders;

/**
 * A calculator of stage-indicating angle of an astronomical phenomenon.
 */
public interface StageIndicatingAngleCalculator {

    /**
     * Calculates stage-indicating angle of an astronomical phenomenon.
     * Typically, that angle measures a cyclical motion with value range: [0, 2π).
     *
     * @param julianEphemerisDay    time argument, in Julian Ephemeris Day
     *                              (in {@linkplain com.github.sigrarr.lunisolarcalc.time.TimeScale#DYNAMICAL TT})
     * @return                      stage-indicating angle, in radians, typically: [0, 2π)
     */
    public double calculateAngle(double julianEphemerisDay);

}
