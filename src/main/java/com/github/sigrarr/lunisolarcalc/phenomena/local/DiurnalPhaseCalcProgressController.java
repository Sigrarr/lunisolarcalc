package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;
import java.util.stream.Collector;

import com.github.sigrarr.lunisolarcalc.time.DynamicalTimelinePoint;

class DiurnalPhaseCalcProgressController {

    private final static Collector<DiurnalPhase, List<DiurnalPhase>, List<DiurnalPhase>> TO_UNMODIFIABLE_LIST = Collector.of(
        ArrayList::new,
        List::add,
        (left, right) -> {
            left.addAll(right);
            return left;
        },
        Collections::unmodifiableList
    );

    private final DiurnalPhaseCalcCore core;
    private List<DiurnalPhase> orderedPhasesInScope;
    private Iterator<DiurnalPhase> phaseIt;
    private DiurnalPhase currentPhase;
    private boolean startFlag;

    DiurnalPhaseCalcProgressController(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    void reset() {
        orderedPhasesInScope = core.getRequest().phases.stream().sorted().collect(TO_UNMODIFIABLE_LIST);
        phaseIt = orderedPhasesInScope.listIterator();
        currentPhase = phaseIt.next();
        core.midnightTT.setCenter(DynamicalTimelinePoint.ofCalendarPoint(core.getRequest().baseMidnight));
        core.midnightTT.setBack(core.midnightTT.getCenter().add(-1.0));
        core.midnightTT.setFront(core.midnightTT.getCenter().add(+1.0));
        core.equatorialCoords.clear();
        core.dateLevel.recalculate();
        startFlag = true;
    }

    void phaseForward() {
        if (!phaseIt.hasNext()) {
            dateForward();
        }
        currentPhase = phaseIt.next();
    }

    void dateForward() {
        phaseIt = orderedPhasesInScope.listIterator();
        core.midnightTT.push(core.midnightTT.getFront().add(1.0));
        core.equatorialCoords.push(null);
        core.dateLevel.recalculate();
    }

    DiurnalPhase getCurrentPhase() {
        return currentPhase;
    }

    boolean pullStartFlag() {
        boolean pulledFlag = startFlag;
        startFlag = false;
        return pulledFlag;
    }
}