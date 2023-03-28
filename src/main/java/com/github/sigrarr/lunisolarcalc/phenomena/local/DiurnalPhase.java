package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.util.Titled;

public enum DiurnalPhase implements Titled {

    RISE("Rise", -1),
    TRANSIT("Transit", 0),
    SET("Set", +1);

    /**
     * Signum of vector from to the phase from its transit: -1 if before, +1 if after, 0 for the transit.
     */
    protected final int direction;
    private final String title;

    private DiurnalPhase(String title, int direction) {
        this.title = title;
        this.direction = direction;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public boolean isExtreme() {
        return direction != 0;
    }
}
