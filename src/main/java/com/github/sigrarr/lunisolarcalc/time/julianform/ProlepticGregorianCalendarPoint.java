package com.github.sigrarr.lunisolarcalc.time.julianform;

public class ProlepticGregorianCalendarPoint extends JulianformCalendarPoint implements Comparable<ProlepticGregorianCalendarPoint> {

    public ProlepticGregorianCalendarPoint(int y, int m, double dt) {
        super(y, m, dt);
    }

    public ProlepticGregorianCalendarPoint(int y, int m, int d) {
        super(y, m, d);
    }

    public ProlepticGregorianCalendarPoint(int y, int m, int d, int h, int min, int s) {
        super(y, m, d, h, min, s);
    }

    @Override
    public Rules getRules() {
        return Rules.GREGORIAN;
    }

    @Override
    public int compareTo(ProlepticGregorianCalendarPoint pgcp) {
        return compareNominally(this, pgcp);
    }
}
