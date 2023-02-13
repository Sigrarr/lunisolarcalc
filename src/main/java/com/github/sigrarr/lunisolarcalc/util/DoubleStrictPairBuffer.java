package com.github.sigrarr.lunisolarcalc.util;

/**
 * A pair of numbers of the {@code double} type
 * with the functionality of accepting a new number.
 *
 * The numbers are labelled "current" (the last added number)
 * and "previous". They are kept and cycled in a manner resembling a queue:
 * first in is first to forget.
 */
public class DoubleStrictPairBuffer {

    private double current = 0.0;
    private double previous = 0.0;
    private int count = 0;

    /**
     * Constructs an empty pair.
     */
    public DoubleStrictPairBuffer() {}

    /**
     * Constructs a pair with a single number ("current").
     *
     * @param current   a number ("current")
     */
    public DoubleStrictPairBuffer(double current) {
        this.current = current;
        count = 1;
    }

    /**
     * Constructs a pair with two numbers
     * ("previous" and "current" respectively).
     *
     * @param previous  a number ("previous")
     * @param current   a number ("current")
     */
    public DoubleStrictPairBuffer(double previous, double current) {
        this.previous = previous;
        this.current = current;
        count = 2;
    }

    /**
     * Gets the current number
     * (or throws an {@link IllegalStateException} if it is not set yet).
     *
     * @return  the current number
     */
    public double getCurrent() {
        if (!hasCurrent()) {
            throw new IllegalStateException();
        }
        return current;
    }

    /**
     * Gets the previous number
     * (or throws an {@link IllegalStateException} if it is not set yet).
     *
     * @return  the previous number
     */
    public double getPrevious() {
        if (!hasPrevious()) {
            throw new IllegalStateException();
        }
        return previous;
    }

    /**
     * Pushes a new number. The new number will be now the "current".
     * The old "current" number, if exists, will be now the "previous".
     * The old "previous" number, if exists, will be forgotten.
     *
     * @param value     new number to push
     */
    public void push(double value) {
        previous = current;
        current = value;
        if (count < 2) {
            count++;
        }
    }

    /**
     * Sets a new value of the current number,
     * without affecting the previous number.
     *
     * @param value     new number to set as "current"
     */
    public void setCurrent(double value) {
        current = value;
        if (count == 0) {
            count = 1;
        }
    }

    /**
     * How many numbers are set (0, 1 or 2).
     *
     * @return  how many numbers are set (0, 1 or 2)
     */
    public int getCount() {
        return count;
    }

    /**
     * Checks whether this pair has got the current number.
     *
     * @return  {@code true} if this pair has got the current number;
     *          {@code false} - otherwise
     */
    public boolean hasCurrent() {
        return count > 0;
    }

    /**
     * Checks whether this pair has got the previous number.
     *
     * @return  {@code true} if this pair has got the previous number;
     *          {@code false} - otherwise
     */
    public boolean hasPrevious() {
        return count > 1;
    }

    /**
     * Checks whether this pair has got both the current number and the previous.
     *
     * @return  {@code true} if this pair has got two numbers;
     *          {@code false} - otherwise
     */
    public boolean hasBothValues() {
        return hasPrevious();
    }

    /**
     * Checks whether this pair is empty (has no values).
     *
     * @return  {@code true} if this pair is empty (has no values),
     *          {@code false} - otherwise
     */
    public boolean isEmpty() {
        return !hasCurrent();
    }

    /**
     * Clears the pair, makes it empty.
     */
    public void clear() {
        count = 0;
    }
}
