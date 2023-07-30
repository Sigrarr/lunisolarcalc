package com.github.sigrarr.lunisolarcalc.phenomena.exceptions;

import com.github.sigrarr.lunisolarcalc.time.exceptions.JulianDayOutOfPeriodException;

public class DiurnalPhaseSearchTooCloseToPeriodBoundaryException extends RuntimeException {
    public DiurnalPhaseSearchTooCloseToPeriodBoundaryException(JulianDayOutOfPeriodException cause) {
        super(
            "Search for diurnal phase(s) failed: too close to the current Julian Period's boundary."
            + " The tool requires about two days of margin from each boundary.",
            cause
        );
    }
}
