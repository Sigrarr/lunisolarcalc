package com.github.sigrarr.lunisolarcalc.util;

/**
 * A pentad of objects with the functionality of accepting a new element.
 *
 * The elements are labelled "back-back" (first), "back" (second),
 * "center" (third), "front" (fourth) and "front-front" (fifth).
 * They are kept and cycled in a manner resembling a queue:
 * first in is first to forget.
 */
public class FlexPentadBuffer<T> {

    @SuppressWarnings("unchecked")
    private T[] elements = (T[]) new Object[5];

    /**
     * Constructs an empty pentad.
     */
    public FlexPentadBuffer() {}

    /**
     * Constructs a pentad with five elements
     * ("back-back"/first, "back"/second, "center"/third
     * "front"/fourth and "front-front"/fifth).
     *
     * @param backBack      an element ("back-back"/first)
     * @param back          an element ("back"/second)
     * @param center        an element ("center"/third)
     * @param front         an element ("front"/fourth)
     * @param frontFront    an element ("front-front"/fifth)
     */
    public FlexPentadBuffer(T backBack, T back, T center, T front, T frontFront) {
        elements[0] = backBack;
        elements[1] = back;
        elements[2] = center;
        elements[3] = front;
        elements[4] = frontFront;
    }

    /**
     * Gets the back-back (first) element.
     *
     * @return  the back-back (first) element (null, if absent)
     */
    public T getBackBack() {
        return elements[0];
    }

    /**
     * Gets the back (second) element.
     *
     * @return  the back (second) element (null, if absent)
     */
    public T getBack() {
        return elements[1];
    }

    /**
     * Gets the center (third) element.
     *
     * @return  the center (third) element (null, if absent)
     */
    public T getCenter() {
        return elements[2];
    }

    /**
     * Gets the front (fourth) element.
     *
     * @return  the front (fourth) element (null, if absent)
     */
    public T getFront() {
        return elements[3];
    }

    /**
     * Gets the front-front (fifth) element.
     *
     * @return  the front-front (fifth) element (null, if absent)
     */
    public T getFrontFront() {
        return elements[4];
    }

    /**
     * Gets an element by the index (from 0).
     *
     * @param index     index
     * @return          the element at the specified index (from 0)
     */
    public T get(int index) {
        return elements[index];
    }

    public void push(T newElement) {
        elements[0] = elements[1];
        elements[1] = elements[2];
        elements[2] = elements[3];
        elements[3] = elements[4];
        elements[4] = newElement;
    }

    public void pushFromBack(T newElement) {
        elements[4] = elements[3];
        elements[3] = elements[2];
        elements[2] = elements[1];
        elements[1] = elements[0];
        elements[0] = newElement;
    }

    public void setBackBack(T newElement) {
        elements[0] = newElement;
    }

    public void setBack(T newElement) {
        elements[1] = newElement;
    }

    public void setCenter(T newElement) {
        elements[2] = newElement;
    }

    public void setFront(T newElement) {
        elements[3] = newElement;
    }

    public void setFrontFront(T newElement) {
        elements[4] = newElement;
    }

    /**
     * Checks whether this pentad has got the back-back (first) element.
     *
     * @return  {@code true} if this pentad has got the back-back (first) element;
     *          {@code false} - otherwise
     */
    public boolean hasBackBack() {
        return elements[0] != null;
    }

    /**
     * Checks whether this pentad has got the back (second) element.
     *
     * @return  {@code true} if this pentad has got the back (second) element;
     *          {@code false} - otherwise
     */
    public boolean hasBack() {
        return elements[1] != null;
    }

    /**
     * Checks whether this pentad has got the center (third) element.
     *
     * @return  {@code true} if this pentad has got the center (third) element;
     *          {@code false} - otherwise
     */
    public boolean hasCenter() {
        return elements[2] != null;
    }

    /**
     * Checks whether this pentad has got the front (fourth) element.
     *
     * @return  {@code true} if this pentad has got the front (fourth) element;
     *          {@code false} - otherwise
     */
    public boolean hasFront() {
        return elements[3] != null;
    }

    /**
     * Checks whether this pentad has got the front-front (fifth) element.
     *
     * @return  {@code true} if this pentad has got the front-front (fifth) element;
     *          {@code false} - otherwise
     */
    public boolean hasFrontFront() {
        return elements[4] != null;
    }

    /**
     * Checks whether this pentad has got all five elements.
     *
     * @return  {@code true} if this pentad has got all five elements;
     *          {@code false} - otherwise
     */
    public boolean isComplete() {
        for (T element : elements)
            if (element == null)
                return false;
        return true;
    }

    /**
     * Checks whether this pentad is empty (has no elements).
     *
     * @return  {@code true} if this pentad is empty (has no elements),
     *          {@code false} - otherwise
     */
    public boolean isEmpty() {
        for (T element : elements)
            if (element != null)
                return false;
        return true;
    }

    /**
     * How many elements are set (0, 1, 2, 3, 4 or 5).
     *
     * @return  how many elements are set (0, 1, 2, 3, 4 or 5)
     */
    public int getCount() {
        int count = 0;
        for (T element : elements)
            if (element != null)
                count++;
        return count;
    }

    /**
     * Clears the pentad, makes it empty.
     */
    public void clear() {
        elements[0] = elements[1] = elements[2] = elements[3] = elements[4] = null;
    }
}
