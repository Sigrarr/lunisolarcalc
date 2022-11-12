package com.github.sigrarr.lunisolarcalc.time.julianform;

public class ProlepticJulianCalendarPoint extends JulianformCalendarPoint implements Comparable<ProlepticJulianCalendarPoint> {

    public static final Rules RULES = Rules.JULIAN;

    public ProlepticJulianCalendarPoint(int y, int m, double dt) {
        super(y, m, dt);
    }

    public ProlepticJulianCalendarPoint(int y, int m, int d) {
        super(y, m, d);
    }

    public ProlepticJulianCalendarPoint(int y, int m, int d, int h, int min, int s) {
        super(y, m, d, h, min, s);
    }

    @Override
    public Rules getRules() {
        return RULES;
    }

    @Override
    public int compareTo(ProlepticJulianCalendarPoint pjcp) {
        return compareNominally(this, pjcp);
    }
}
