package com.github.sigrarr.lunisolarcalc.util;

/**
 * A triad of objects with the functionality of accepting a new element.
 *
 * The elements are labelled "back" (first), "center" (second) and "front" (third).
 * They are kept and cycled in a manner resembling a queue:
 * first in is first to forget.
 */
public class FlexTriadBuffer<T> {

    @SuppressWarnings("unchecked")
    private T[] elements = (T[]) new Object[3];

    /**
     * Constructs an empty triad.
     */
    public FlexTriadBuffer() {}

    /**
     * Constructs a triad with three elements
     * ("back"/first, "center"/second and "front"/third).
     *
     * @param back      an element ("back"/first)
     * @param center    an element ("center"/second)
     * @param front     an element ("front"/third)
     */
    public FlexTriadBuffer(T back, T center, T front) {
        elements[0] = back;
        elements[1] = center;
        elements[2] = front;
    }

    /**
     * Gets the back (first) element.
     *
     * @return  the back (first) element (null, if absent)
     */
    public T getBack() {
        return elements[0];
    }

    /**
     * Gets the center (second) element.
     *
     * @return  the center (second) element (null, if absent)
     */
    public T getCenter() {
        return elements[1];
    }

    /**
     * Gets the front (third) element.
     *
     * @return  the front (third) element (null, if absent)
     */
    public T getFront() {
        return elements[2];
    }

    public void setBack(T newElement) {
        elements[0] = newElement;
    }

    public void setCenter(T newElement) {
        elements[1] = newElement;
    }

    public void setFront(T newElement) {
        elements[2] = newElement;
    }

    public void push(T newElement) {
        elements[0] = elements[1];
        elements[1] = elements[2];
        elements[2] = newElement;
    }

    public void pushFromBack(T newElement) {
        elements[2] = elements[1];
        elements[1] = elements[0];
        elements[0] = newElement;
    }

    /**
     * Checks whether this triad has got the back (first) element.
     *
     * @return  {@code true} if this triad has got the back (first) element;
     *          {@code false} - otherwise
     */
    public boolean hasBack() {
        return elements[0] != null;
    }

    /**
     * Checks whether this triad has got the center (second) element.
     *
     * @return  {@code true} if this triad has got the center (second) element;
     *          {@code false} - otherwise
     */
    public boolean hasCenter() {
        return elements[1] != null;
    }

    /**
     * Checks whether this triad has got the front (third) element.
     *
     * @return  {@code true} if this triad has got the front (third) element;
     *          {@code false} - otherwise
     */
    public boolean hasFront() {
        return elements[2] != null;
    }

    /**
     * Checks whether this triad has got all three elements.
     *
     * @return  {@code true} if this triad has got all three elements;
     *          {@code false} - otherwise
     */
    public boolean isComplete() {
        for (T element : elements)
            if (element == null)
                return false;
        return true;
    }

    /**
     * Checks whether this triad is empty (has no elements).
     *
     * @return  {@code true} if this triad is empty (has no elements),
     *          {@code false} - otherwise
     */
    public boolean isEmpty() {
        for (T element : elements)
            if (element != null)
                return false;
        return true;
    }

    /**
     * How many elements are set (0, 1, 2 or 3).
     *
     * @return  how many elements are set (0, 1, 2 or 3)
     */
    public int getCount() {
        int count = 0;
        for (T element : elements)
            if (element != null)
                count++;
        return count;
    }

    /**
     * Clears the triad, makes it empty.
     */
    public void clear() {
        elements[0] = elements[1] = elements[2] = null;
    }
}
