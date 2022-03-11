package com.github.sigrarr.lunisolarcalc.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DoubleStepPairTest {

    private DoubleStepPair pair;

    @Test
    public void shouldKeepNumbersFifoLikeWithLastInLabelledAsCurrent() {
        pair = new DoubleStepPair();
        assertStateInformationByExpectedCount(0);
        pair.push(1.13);
        assertStateInformationByExpectedCount(1);
        assertEquals(1.13, pair.getCurrent());
        pair.push(-9.9);
        assertStateInformationByExpectedCount(2);
        assertEquals(-9.9, pair.getCurrent());
        assertEquals(1.13, pair.getPrevious());
        pair.push(3.14);
        assertStateInformationByExpectedCount(2);
        assertEquals(3.14, pair.getCurrent());
        assertEquals(-9.9, pair.getPrevious());
        pair.clear();
        assertStateInformationByExpectedCount(0);
        pair = new DoubleStepPair(1.0);
        assertStateInformationByExpectedCount(1);
        assertEquals(1.0, pair.getCurrent());
        pair = new DoubleStepPair(2.0, 3.0);
        assertStateInformationByExpectedCount(2);
        assertEquals(3.0, pair.getCurrent());
        assertEquals(2.0, pair.getPrevious());
    }

    @Test
    public void shouldTrhowIllegalStateException() {
        pair = new DoubleStepPair();
        assertThrows(IllegalStateException.class, () -> pair.getCurrent());
        assertThrows(IllegalStateException.class, () -> pair.getPrevious());
        pair.push(1.0);
        assertThrows(IllegalStateException.class, () -> pair.getPrevious());
        pair.push(2.0);
        pair.clear();
        assertThrows(IllegalStateException.class, () -> pair.getCurrent());
        assertThrows(IllegalStateException.class, () -> pair.getPrevious());
    }

    private void assertStateInformationByExpectedCount(int expectedCount) {
        assertEquals(expectedCount, pair.getCount());
        switch (expectedCount) {
            case 0:
                assertTrue(pair.isEmpty());
                assertFalse(pair.isComplete());
                assertFalse(pair.hasCurrent());
                assertFalse(pair.hasPrevious());
                break;
            case 1:
                assertFalse(pair.isEmpty());
                assertFalse(pair.isComplete());
                assertTrue(pair.hasCurrent());
                assertFalse(pair.hasPrevious());
                break;
            case 2:
                assertFalse(pair.isEmpty());
                assertTrue(pair.isComplete());
                assertTrue(pair.hasCurrent());
                assertTrue(pair.hasPrevious());
                break;
        }
    }
}
