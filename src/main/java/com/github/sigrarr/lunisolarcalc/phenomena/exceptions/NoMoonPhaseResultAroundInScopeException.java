package com.github.sigrarr.lunisolarcalc.phenomena.exceptions;

import java.util.EnumSet;
import java.util.stream.Collectors;

import com.github.sigrarr.lunisolarcalc.phenomena.global.MoonPhase;
import com.github.sigrarr.lunisolarcalc.time.*;
import com.github.sigrarr.lunisolarcalc.time.exceptions.JulianDayOutOfPeriodException;

public class NoMoonPhaseResultAroundInScopeException extends RuntimeException {

    public NoMoonPhaseResultAroundInScopeException(TimelinePoint tx, EnumSet<MoonPhase> phases) {
        super(
            "No "
            + phases.stream().map(ph -> ph.getTitle()).collect(Collectors.joining("/"))
            + " fitting in the current Julian Period found around the time argument: "
            + tx + " (" + tx.formatCalendrically() + ")."
            + " Try an argument with a greater margin from the Julian Period's boundary."
            + " " + JulianDayOutOfPeriodException.getCurrentJulianPeriodDescription()
        );
    }
}
