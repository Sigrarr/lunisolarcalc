package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;
import java.util.function.*;

import com.github.sigrarr.lunisolarcalc.coords.*;
import com.github.sigrarr.lunisolarcalc.phenomena.UniversalOccurrence;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.util.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.*;

abstract class DiurnalPhaseCalcCore {

    final Supplier<OptionalDouble> timeSupplier = new Supplier<OptionalDouble>() {
        @Override public OptionalDouble get() {
            return resolveNextTime();
        }
    };

    final Supplier<Optional<UniversalOccurrence<BodyDiurnalPhase>>> occurrenceSupplier = new Supplier<Optional<UniversalOccurrence<BodyDiurnalPhase>>>() {
        @Override public Optional<UniversalOccurrence<BodyDiurnalPhase>> get() {
            OptionalDouble time = resolveNextTime();
            if (!time.isPresent())
                return Optional.empty();
            UniversalTimelinePoint point = new UniversalTimelinePoint(midnightTT.getCenter().julianDay + time.getAsDouble());
            BodyDiurnalPhase bodyDiurnalPhase = BodyDiurnalPhase.of(body, controller.getCurrentPhase());
            return Optional.of(new UniversalOccurrence<>(point, bodyDiurnalPhase));
        }
    };

    final Body body = specifyBody();
    final FlexTriadBuffer<DynamicalTimelinePoint> midnightTT = new FlexTriadBuffer<>();
    final FlexTriadBuffer<Map<Subject, ?>> equatorialCoords = new FlexTriadBuffer<>();
    final DiurnalPhaseCalcProgressController controller = new DiurnalPhaseCalcProgressController(this);
    final DiurnalPhaseCalcDateLevelValues dateLevel = new DiurnalPhaseCalcDateLevelValues(this);
    final DiurnalPhaseCalcIterationLevelValues iterationLevel = new DiurnalPhaseCalcIterationLevelValues(this);
    private final MultiOutputComposition<Subject, TimelinePoint> equatorialCoordsCalc = CoordsCalcCompositions.compose(EnumSet.of(
        body.rightAscensionSubject, body.declinationSubject
    ));
    private final SingleOutputComposition<Subject, TimelinePoint> siderealTimeCalc = CoordsCalcCompositions.compose(
        Subject.SIDEREAL_APPARENT_TIME
    );

    private DiurnalPhaseCalcRequest request;
    boolean approximationMode = false;
    double precision = Calcs.SECOND_TO_DAY;

    OptionalDouble resolveNextTime() {
        if (!controller.pullStartFlag())
            controller.phaseForward();
        if (!dateLevel.areDiurnalPhasesPresent())
            return OptionalDouble.empty();

        double initialM;
        DoubleUnaryOperator mCorrectionFunction;
        switch (controller.getCurrentPhase()) {
            case RISE:
                initialM = dateLevel.calculateInitialRiseM();
                mCorrectionFunction = iterationLevel::calculateRise;
                break;
            case SET:
                initialM = dateLevel.calculateInitialSetM();
                mCorrectionFunction = iterationLevel::calculateSet;
                break;
            case TRANSIT:
            default:
                initialM = dateLevel.initialTransitM;
                mCorrectionFunction = iterationLevel::calculateTransit;
                break;
        }

        double m = approximationMode ? initialM : calculateCorrectM(initialM, mCorrectionFunction);
        return OptionalDouble.of(m);
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

    double getCenterUniversalMidnightSiderealTimeDegrees() {
        return (Double) siderealTimeCalc.calculate(midnightTT.getCenter().add(dateLevel.deltaTDays));
    }

    Map<Subject, ?> getBackEquatorialCoords() {
        if (!equatorialCoords.hasBack())
            equatorialCoords.setBack(equatorialCoordsCalc.calculate(midnightTT.getBack()));
        return equatorialCoords.getBack();
    }

    Map<Subject, ?> getCenterEquatorialCoords() {
        if (!equatorialCoords.hasCenter())
            equatorialCoords.setCenter(equatorialCoordsCalc.calculate(midnightTT.getCenter()));
        return equatorialCoords.getCenter();
    }

    Map<Subject, ?> getFrontEquatorialCoords() {
        if (!equatorialCoords.hasFront())
            equatorialCoords.setFront(equatorialCoordsCalc.calculate(midnightTT.getFront()));
        return equatorialCoords.getFront();
    }
}
