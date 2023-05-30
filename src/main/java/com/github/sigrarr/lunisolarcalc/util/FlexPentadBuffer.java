package com.github.sigrarr.lunisolarcalc.util;

/**
 * A pentad of objects with the functionality of accepting a new element.
 *
 * The elements are labelled "back-back" [0], "back" [1],
 * "center" [2], "front" [3] and "front-front" [4].
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
     * ("back-back" [0], "back" [1], "center" [2]
     * "front" [3] and "front-front" [4]).
     *
     * @param backBack      an element ("back-back" [0])
     * @param back          an element ("back" [1])
     * @param center        an element ("center" [2])
     * @param front         an element ("front" [3])
     * @param frontFront    an element ("front-front" [4])
     */
    public FlexPentadBuffer(T backBack, T back, T center, T front, T frontFront) {
        elements[0] = backBack;
        elements[1] = back;
        elements[2] = center;
        elements[3] = front;
        elements[4] = frontFront;
    }

    /**
     * Gets the back-back [0] element.
     *
     * @return  the back-back [0] element (null, if absent)
     */
    public T getBackBack() {
        return elements[0];
    }

    /**
     * Gets the back [1] element.
     *
     * @return  the back [1] element (null, if absent)
     */
    public T getBack() {
        return elements[1];
    }

    /**
     * Gets the center [2] element.
     *
     * @return  the center [2] element (null, if absent)
     */
    public T getCenter() {
        return elements[2];
    }

    /**
     * Gets the front [3] element.
     *
     * @return  the front [3] element (null, if absent)
     */
    public T getFront() {
        return elements[3];
    }

    /**
     * Gets the front-front [4] element.
     *
     * @return  the front-front [4] element (null, if absent)
     */
    public T getFrontFront() {
        return elements[4];
    }

    /**
     * Gets an element by the index [0-4].
     *
     * @param index     index [0-4]
     * @return          the element at the specified index
     */
    public T get(int index) {
        return elements[index];
    }

    /**
     * Adds a new element at the end of the pentad, pushing the old
     * ones back. The new element will be now the "front-front",
     * the old "front-front" will be now the "front", and so on.
     * The old "back-back" element will be forgotten.
     *
     * @param newElement    new element to push
     */
    public void push(T newElement) {
        elements[0] = elements[1];
        elements[1] = elements[2];
        elements[2] = elements[3];
        elements[3] = elements[4];
        elements[4] = newElement;
    }

    /**
     * Adds a new element at the beginning of the pentad, pushing the old
     * ones forward. The new element will be now the "back-back",
     * the old "back-back" will be now the "back", and so on.
     * The old "front-front" element will be forgotten.
     *
     * @param newElement    new element to push
     */
    public void pushFromBack(T newElement) {
        elements[4] = elements[3];
        elements[3] = elements[2];
        elements[2] = elements[1];
        elements[1] = elements[0];
        elements[0] = newElement;
    }

    /**
     * Sets the "back-back" [0] element,
     * without affecting the rest of the pentad.
     *
     * @param newElement    new "back-back" [0] element
     */
    public void setBackBack(T newElement) {
        elements[0] = newElement;
    }

    /**
     * Sets the "back" [1] element,
     * without affecting the rest of the pentad.
     *
     * @param newElement    new "back" [1] element
     */
    public void setBack(T newElement) {
        elements[1] = newElement;
    }

    /**
     * Sets the "center" [2] element,
     * without affecting the rest of the pentad.
     *
     * @param newElement    new "center" [2] element
     */
    public void setCenter(T newElement) {
        elements[2] = newElement;
    }

    /**
     * Sets the "front" [3] element,
     * without affecting the rest of the pentad.
     *
     * @param newElement    new "front" [3] element
     */
    public void setFront(T newElement) {
        elements[3] = newElement;
    }

    /**
     * Sets the "front-front" [4] element,
     * without affecting the rest of the pentad.
     *
     * @param newElement    new "front-front" [4] element
     */
    public void setFrontFront(T newElement) {
        elements[4] = newElement;
    }

    /**
     * Checks whether this pentad has got the "back-back" [0] element.
     *
     * @return  {@code true} if this pentad has got the "back-back" [0] element;
     *          {@code false} - otherwise (if it's null)
     */
    public boolean hasBackBack() {
        return elements[0] != null;
    }

    /**
     * Checks whether this pentad has got the "back" [1] element.
     *
     * @return  {@code true} if this pentad has got the "back" [1] element;
     *          {@code false} - otherwise (if it's null)
     */
    public boolean hasBack() {
        return elements[1] != null;
    }

    /**
     * Checks whether this pentad has got the "center" [2] element.
     *
     * @return  {@code true} if this pentad has got the "center" [2] element;
     *          {@code false} - otherwise (if it's null)
     */
    public boolean hasCenter() {
        return elements[2] != null;
    }

    /**
     * Checks whether this pentad has got the "front" [3] element.
     *
     * @return  {@code true} if this pentad has got the "front" [3] element;
     *          {@code false} - otherwise (if it's null)
     */
    public boolean hasFront() {
        return elements[3] != null;
    }

    /**
     * Checks whether this pentad has got the "front-front" [4] element.
     *
     * @return  {@code true} if this pentad has got the "front-front" [4] element;
     *          {@code false} - otherwise (if it's null)
     */
    public boolean hasFrontFront() {
        return elements[4] != null;
    }

    /**
     * Checks whether this pentad has got all five elements.
     *
     * @return  {@code true} if this pentad has got all five elements;
     *          {@code false} - otherwise (there is at least one null)
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
