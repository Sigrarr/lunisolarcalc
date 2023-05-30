package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.util.Titled;

/**
 * A principal diurnal phase of a celestial body, ie. a distinguished moment
 * in the movement of an astronomical body in the sky caused by the Earth's rotation.
 */
public enum DiurnalPhase implements Titled {
    /**
     * The moment when a body appears on the horizon.
     */
    RISE("Rise", -1),
    /**
     * The moment when a body crosses the meridian plane.
     */
    TRANSIT("Transit", 0),
    /**
     * The moment when a body hides under the horizon.
     */
    SET("Set", +1);

    /**
     * Signum of vector to the phase from its transit: -1 if before, +1 if after, 0 for the transit.
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

    /**
     * Informs whether this is an extreme phase,
     * when a body crosses the horizon (ie. {@link #RISE} or {@link #SET}).
     *
     * @return  {@code true} if this is an extreme phase,
     *          {@code false} otherwise
     */
    public boolean isExtreme() {
        return direction != 0;
    }
}
