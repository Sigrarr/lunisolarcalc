package com.github.sigrarr.lunisolarcalc.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FlexPentadBufferTest {

    private FlexPentadBuffer<Integer> pentad;

    @Test
    public void shouldInitializeAndUpdate() {
        pentad = new FlexPentadBuffer<Integer>();
        assertStateAndGettersOutput(new Integer[5]);
        pentad = new FlexPentadBuffer<Integer>(1, 2, 3, 4, 5);
        assertStateAndGettersOutput(new Integer[] {1, 2, 3, 4, 5});
        pentad.setBackBack(6);
        assertStateAndGettersOutput(new Integer[] {6, 2, 3, 4, 5});
        pentad.setBack(null);
        assertStateAndGettersOutput(new Integer[] {6, null, 3, 4, 5});
        pentad.setCenter(7);
        assertStateAndGettersOutput(new Integer[] {6, null, 7, 4, 5});
        pentad.setFront(8);
        assertStateAndGettersOutput(new Integer[] {6, null, 7, 8, 5});
        pentad.setFrontFront(9);
        assertStateAndGettersOutput(new Integer[] {6, null, 7, 8, 9});
        pentad.clear();
        assertStateAndGettersOutput(new Integer[5]);
    }

    @Test
    public void shouldCycle() {
        pentad = new FlexPentadBuffer<Integer>(1, 2, 3, 4, 5);
        pentad.push(6);
        assertStateAndGettersOutput(new Integer[] {2, 3, 4, 5, 6});
        pentad.push(null);
        assertStateAndGettersOutput(new Integer[] {3, 4, 5, 6, null});
        pentad.pushFromBack(7);
        assertStateAndGettersOutput(new Integer[] {7, 3, 4, 5, 6});
        pentad.pushFromBack(null);
        assertStateAndGettersOutput(new Integer[] {null, 7, 3, 4, 5});
    }

    private void assertStateAndGettersOutput(Integer[] expectedElements) {
        assertEquals(expectedElements[0], pentad.getBackBack());
        assertEquals(expectedElements[1], pentad.getBack());
        assertEquals(expectedElements[2], pentad.getCenter());
        assertEquals(expectedElements[3], pentad.getFront());
        assertEquals(expectedElements[4], pentad.getFrontFront());

        assertEquals(expectedElements[0] != null, pentad.hasBackBack());
        assertEquals(expectedElements[1] != null, pentad.hasBack());
        assertEquals(expectedElements[2] != null, pentad.hasCenter());
        assertEquals(expectedElements[3] != null, pentad.hasFront());
        assertEquals(expectedElements[4] != null, pentad.hasFrontFront());

        int expectedCount = 0;
        for (int i = 0; i < 5; i++)
            if (expectedElements[i] != null)
                expectedCount++;
        assertEquals(expectedCount, pentad.getCount());
        assertEquals(expectedCount == 5, pentad.isComplete());
        assertEquals(expectedCount == 0, pentad.isEmpty());
    }
}
