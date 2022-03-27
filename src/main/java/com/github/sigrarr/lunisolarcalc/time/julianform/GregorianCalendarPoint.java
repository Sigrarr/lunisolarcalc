package com.github.sigrarr.lunisolarcalc.time.julianform;

public class GregorianCalendarPoint extends JulianformCalendarPoint implements Comparable<GregorianCalendarPoint> {

    public static final GregorianCalendarPoint FIRST_GREGORIAN_RULES_POINT = new GregorianCalendarPoint(1582, 10, 15);

    public GregorianCalendarPoint(int y, int m, double dt) {
        super(y, m, dt);
    }

    public GregorianCalendarPoint(int y, int m, int d) {
        super(y, m, d);
    }

    public GregorianCalendarPoint(int y, int m, int d, int h, int min, int s) {
        super(y, m, d, h, min, s);
    }

    @Override
    public Rules getRules() {
        return this.compareTo(FIRST_GREGORIAN_RULES_POINT) < 0 ? Rules.JULIAN : Rules.GREGORIAN;
    }

    @Override
    public int compareTo(GregorianCalendarPoint gcp) {
        return compareNominally(this, gcp);
    }
}
