package com.github.sigrarr.lunisolarcalc.util;

/**
 * A row of numbers.
 */
public interface DoubleRow {
    /**
     * Gets the size of this row (how many numbers it contains).
     *
     * @return size of this row (how many numbers it contains)
     */
    public int getSize();

    /**
     * Gets a number by given index (counting from 0).
     *
     * @param index     index (from 0)
     * @return          value at given index
     */
    public double getValue(int index);
}
