package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;
import java.util.function.*;
import com.github.sigrarr.lunisolarcalc.coords.Subject;
import com.github.sigrarr.lunisolarcalc.time.UniversalTimelinePoint;
import com.github.sigrarr.lunisolarcalc.util.Calcs;

class DiurnalPhaseCalcDayValues {

    static final int COORD_LOCAL_HOUR_ANGLE = 0;
    static final int COORD_DECLINATION = 1;
    static final int COORD_RIGHT_ASCENSION = 2;
    static final int COORD_NUTUATION_IN_LONGITUDE = 3;
    static final int COORD_ECLIPTIC_OBLIQUITY = 4;
    static int coordsN = 5;
    static final int NOON_EXTREME_LOCAL_HOUR_ANGLE_COS = 0;
    static final int NOON_TO_TRANSIT_VECTOR = 1;
    static final int TRANSIT_EXTREME_LOCAL_HOUR_ANGLE_COS = 2;
    static int dependantsN = 3;

    protected final DiurnalPhaseCalcCore core;
    final UniversalTimelinePoint noon;
    final Double[] coordValues = new Double[getCoordsN()];
    final Double[] dependantValues = new Double[getDependantsN()];
    Optional<UniversalTimelinePoint> finalTransit = null;

    DiurnalPhaseCalcDayValues(DiurnalPhaseCalcCore core, UniversalTimelinePoint noon) {
        this.core = core;
        this.noon = noon;
    }

    public boolean has(int dependantKey) {
        return dependantValues[dependantKey] != null;
    }

    public void set(int dependantKey, double newValue) {
        dependantValues[dependantKey] = newValue;
    }

    public double get(int dependantKey, DoubleSupplier calculation) {
        if (dependantValues[dependantKey] == null)
            dependantValues[dependantKey] = calculation.getAsDouble();
        return dependantValues[dependantKey];
    }

    public void clear(int dependantKey) {
        dependantValues[dependantKey] = null;
    }

    public double getNoonCoord(int coordKey) {
        if (coordValues[coordKey] == null)
            loadCoords();
        return coordValues[coordKey];
    }

    public boolean hasFinalizedTransit() {
        return finalTransit != null;
    }

    public Optional<UniversalTimelinePoint> getFinalTransit() {
        return finalTransit;
    }

    public void setFinalTransitByVectorFromNoon(OptionalDouble finalNoonToTransitVector) {
        if (finalNoonToTransitVector.isPresent()) {
            set(NOON_TO_TRANSIT_VECTOR, finalNoonToTransitVector.getAsDouble());
            finalTransit = Optional.of(noon.add(finalNoonToTransitVector.getAsDouble()));
        } else
            finalTransit = Optional.empty();
    }

    protected void loadCoords() {
        Map<Subject, Object> values = core.coordsCalc.calculate(noon);
        coordValues[COORD_LOCAL_HOUR_ANGLE] = Calcs.Angle.toNormalSignedLongitude(
            (Double) values.get(core.body.hourAngleSubject) - core.getRequest().longitude
        );
        coordValues[COORD_DECLINATION] = (Double) values.get(core.body.declinationSubject);
        coordValues[COORD_RIGHT_ASCENSION] = (Double) values.get(core.body.rightAscensionSubject);
        coordValues[COORD_NUTUATION_IN_LONGITUDE] = (Double) values.get(Subject.EARTH_NUTUATION_IN_LONGITUDE);
        coordValues[COORD_ECLIPTIC_OBLIQUITY] = (Double) values.get(Subject.ECLIPTIC_TRUE_OBLIQUITY);
    }

    protected int getCoordsN() {
        return coordsN;
    }

    protected int getDependantsN() {
        return dependantsN;
    }
}
