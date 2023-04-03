package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static com.github.sigrarr.lunisolarcalc.phenomena.local.DiurnalPhaseCalcDayValues.*;

import java.util.function.DoubleUnaryOperator;

import com.github.sigrarr.lunisolarcalc.util.*;

class DiurnalPhaseCalcTransitResolver {

    protected final DiurnalPhaseCalcCore core;

    DiurnalPhaseCalcTransitResolver(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    double approximateNoonToTransitVector(int dayPosition) {
        double lha = core.getDay(dayPosition).getCoord(COORD_NOON_LOCAL_HOUR_ANGLE);
        return Calcs.Angle.toNormalSignedLongitude(- lha / Calcs.TURN, 1.0);
    }

    double findCloseNoonToTransitVector(int dayPosition) {
        return findNoonToTransitVector(
            dayPosition,
            vector -> core.coordsCombiner.combineCloseLocalHourAngle(dayPosition, vector)
        );
    }

    double findCentralNoonToTransitVector() {
        return findNoonToTransitVector(
            0,
            vector -> core.coordsCombiner.combineCentralLocalHourAngle(vector)
        );
    }

    private double findNoonToTransitVector(int dayPosition, DoubleUnaryOperator lhaEvaluation) {
        DiurnalPhaseCalcDayValues dayValues = core.getDay(dayPosition);

        double vector = dayValues.has(NOON_TO_TRANSIT_VECTOR) ?
            dayValues.get(NOON_TO_TRANSIT_VECTOR, null)
            : approximateNoonToTransitVector(dayPosition);
        double lha = lhaEvaluation.applyAsDouble(vector);

        DoubleStrictPairBuffer correction = new DoubleStrictPairBuffer(vector);
        correction.push(-lha/Calcs.TURN);

        while (
            Double.compare(Math.abs(lha), core.getRequest().precisionAngle) > 0
            && Double.compare(Math.abs(correction.getCurrent()), Math.abs(correction.getPrevious())) < 0
        ) {
            vector += correction.getCurrent();
            lha = lhaEvaluation.applyAsDouble(vector);
            correction.push(-lha/Calcs.TURN);
        }

        return vector;
    }
}
