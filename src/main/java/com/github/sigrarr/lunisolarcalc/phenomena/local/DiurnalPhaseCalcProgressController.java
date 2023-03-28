package com.github.sigrarr.lunisolarcalc.phenomena.local;

import java.util.*;
import java.util.stream.Collectors;

import com.github.sigrarr.lunisolarcalc.time.UniversalTimelinePoint;;

class DiurnalPhaseCalcProgressController {

    private final DiurnalPhaseCalcCore core;
    private List<DiurnalPhase> orderedPhasesInScope;
    private Iterator<DiurnalPhase> phaseIt;
    private DiurnalPhase currentPhase;

    DiurnalPhaseCalcProgressController(DiurnalPhaseCalcCore core) {
        this.core = core;
    }

    void reset() {
        DiurnalPhaseCalcRequest request = core.getRequest();
        orderedPhasesInScope = request.phases.stream().sorted().collect(Collectors.toList());
        phaseIt = orderedPhasesInScope.listIterator();
        currentPhase = phaseIt.next();
        core.dayValues.setBackBack(core.prepareDayValues(request.baseNoon.add(-2.0)));
        core.dayValues.setBack(core.prepareDayValues(request.baseNoon.add(-1.0)));
        core.dayValues.setCenter(core.prepareDayValues(request.baseNoon));
        core.dayValues.setFront(core.prepareDayValues(request.baseNoon.add(+1.0)));
        core.dayValues.setFrontFront(core.prepareDayValues(request.baseNoon.add(+2.0)));
    }

    void phaseForward() {
        if (!phaseIt.hasNext()) {
            dayForward();
        }
        currentPhase = phaseIt.next();
    }

    void dayForward() {
        phaseIt = orderedPhasesInScope.listIterator();
        UniversalTimelinePoint lastNoon = core.dayValues.getFrontFront().noon;
        core.dayValues.push(core.prepareDayValues(lastNoon.add(1.0)));
    }

    DiurnalPhase getCurrentPhase() {
        return currentPhase;
    }
}
