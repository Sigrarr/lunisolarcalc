package com.github.sigrarr.lunisolarcalc.util;

public class DoubleStepPair {

    private double current = 0.0;
    private double previous = 0.0;
    private int count = 0;

    public DoubleStepPair() {}
    
    public DoubleStepPair(double current) {
        this.current = current;
        count = 1;
    }

    public DoubleStepPair(double previous, double current) {
        this(current);
        this.previous = previous;
        count = 2;
    }

    public double getCurrent() {
        if (!hasCurrent()) {
            throw new IllegalStateException();
        }
        return current;
    }

    public double getPrevious() {
        if (!hasPrevious()) {
            throw new IllegalStateException();
        }
        return previous;
    }

    public void push(double value) {
        previous = current;
        current = value;
        if (count < 2) {
            count++;
        }
    }

    public void setCurrent(double value) {
        current = value;
        if (count == 0) {
            count = 1;
        }
    }

    public int getCount() {
        return count;
    }

    public boolean hasCurrent() {
        return count > 0;
    }

    public boolean hasPrevious() {
        return count > 1;
    }

    public boolean isComplete() {
        return hasPrevious();
    }

    public boolean isEmpty() {
        return !hasCurrent();
    }

    public void clear() {
        count = 0;
    }
}
