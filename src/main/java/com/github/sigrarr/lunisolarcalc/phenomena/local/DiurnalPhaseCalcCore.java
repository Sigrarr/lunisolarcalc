package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Supplier;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.phenomena.UniversalOccurrence;
import com.github.sigrarr.lunisolarcalc.phenomena.local.DiurnalPhaseCalcRequest.Mode;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.MultiOutputComposition;

abstract class DiurnalPhaseCalcCore {

    final Supplier<UniversalTimelinePoint> timelinePointSupplier = new Supplier<UniversalTimelinePoint>() {
        @Override public UniversalTimelinePoint get() {
            return resolveNext();
        }
    };

    final Supplier<UniversalOccurrence<BodyDiurnalPhase>> occurrenceSupplier = new Supplier<UniversalOccurrence<BodyDiurnalPhase>>() {
        @Override public UniversalOccurrence<BodyDiurnalPhase> get() {
            UniversalTimelinePoint timelinePoint = resolveNext();
            return timelinePoint == null ? null
                : new UniversalOccurrence<BodyDiurnalPhase>(timelinePoint, BodyDiurnalPhase.of(body, controller.getCurrentPhase()));
        }
    };

    final Body body = specifyBody();
    final FlexTriadBuffer<UniversalTimelinePoint> midnight = new FlexTriadBuffer<>();
    final FlexTriadBuffer<Map<Subject, ?>> equatorialCoords = new FlexTriadBuffer<>();
    final DiurnalPhaseCalcProgressController controller = new DiurnalPhaseCalcProgressController(this);
    final DiurnalPhaseCalcDateLevelValues dateLevel = new DiurnalPhaseCalcDateLevelValues(this);
    final DiurnalPhaseCalcIterationLevelValues iterationLevel = new DiurnalPhaseCalcIterationLevelValues(this);
    private final MultiOutputComposition<Subject, TimelinePoint> coordsCalc = CoordsCalcCompositions.compose(EnumSet.of(
        body.rightAscensionSubject, body.declinationSubject, Subject.SIDEREAL_APPARENT_TIME
    ));

    private DiurnalPhaseCalcRequest request;
    double precision = Calcs.SECOND_TO_DAY;

    UniversalTimelinePoint resolveNext() {
        if (!controller.pullStartFlag())
            controller.phaseForward();
        if (!dateLevel.areDiurnalPhasesPresent())
            return null;

        double initialM;
        DoubleUnaryOperator mCorrectionFunction;
        switch (controller.getCurrentPhase()) {
            case RISING:
                initialM = dateLevel.initialTransitM - dateLevel.hourAngle / Calcs.TURN;
                mCorrectionFunction = iterationLevel::calculateRising;
                break;
            case SETTING:
                initialM = dateLevel.initialTransitM + dateLevel.hourAngle / Calcs.TURN;
                mCorrectionFunction = iterationLevel::calculateSetting;
                break;
            case TRANSIT:
            default:
                initialM = dateLevel.initialTransitM;
                mCorrectionFunction = iterationLevel::calculateTransit;
                break;
        }

        double m = request.mode == Mode.APPROXIMATION ? initialM : calculateCorrectM(initialM, mCorrectionFunction);
        return midnight.getCenter().add(m);
    }

    void reset(DiurnalPhaseCalcRequest request) {
        this.request = request;
        controller.reset();
    }

    private double calculateCorrectM(double initialM, DoubleUnaryOperator mCorrectionFunction) {
        double m = mCorrectionFunction.applyAsDouble(initialM);
        while (Math.abs(iterationLevel.getLastMCorrection()) > precision) {
            m = mCorrectionFunction.applyAsDouble(m);
        }
        return m;
    }

    DiurnalPhaseCalcRequest getRequest() {
        return request;
    }

    abstract Body specifyBody();

    abstract double getCenterStandardAltitude();

    Map<Subject, ?> getBackCoords() {
        if (!equatorialCoords.hasBack())
            equatorialCoords.setBack(coordsCalc.calculate(midnight.getBack()));
        return equatorialCoords.getBack();
    }

    Map<Subject, ?> getCenterCoords() {
        if (!equatorialCoords.hasCenter())
            equatorialCoords.setCenter(coordsCalc.calculate(midnight.getCenter()));
        return equatorialCoords.getCenter();
    }

    Map<Subject, ?> getFrontCoords() {
        if (!equatorialCoords.hasFront())
            equatorialCoords.setFront(coordsCalc.calculate(midnight.getFront()));
        return equatorialCoords.getFront();
    }
}
