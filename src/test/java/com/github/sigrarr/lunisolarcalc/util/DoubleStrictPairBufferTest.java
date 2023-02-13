package com.github.sigrarr.lunisolarcalc.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DoubleStrictPairBufferTest {

    private DoubleStrictPairBuffer pair;

    @Test
    public void shouldKeepNumbersInFifoMannerWithLastInLabelledAsCurrent() {
        pair = new DoubleStrictPairBuffer();
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

        pair = new DoubleStrictPairBuffer(1.0);
        assertStateInformationByExpectedCount(1);
        assertEquals(1.0, pair.getCurrent());

        pair = new DoubleStrictPairBuffer(2.0, 3.0);
        assertStateInformationByExpectedCount(2);
        assertEquals(3.0, pair.getCurrent());
        assertEquals(2.0, pair.getPrevious());
    }

    @Test
    public void shouldTrhowIllegalStateException() {
        pair = new DoubleStrictPairBuffer();
        assertThrows(IllegalStateException.class, () -> pair.getCurrent());
        assertThrows(IllegalStateException.class, () -> pair.getPrevious());

        pair.push(1.0);
        assertDoesNotThrow(() -> pair.getCurrent());
        assertThrows(IllegalStateException.class, () -> pair.getPrevious());

        pair.push(2.0);
        assertDoesNotThrow(() -> pair.getCurrent());
        assertDoesNotThrow(() -> pair.getPrevious());

        pair.clear();
        assertThrows(IllegalStateException.class, () -> pair.getCurrent());
        assertThrows(IllegalStateException.class, () -> pair.getPrevious());
    }

    @Test
    public void shouldDistinguishBetweenAddedZeroAndLackOfValue() {
        pair = new DoubleStrictPairBuffer();
        assertStateInformationByExpectedCount(0);
        pair.push(0.0);
        assertStateInformationByExpectedCount(1);
        pair.push(0.0);
        assertStateInformationByExpectedCount(2);
    }

    private void assertStateInformationByExpectedCount(int expectedCount) {
        assertEquals(expectedCount, pair.getCount());
        switch (expectedCount) {
            case 0:
                assertTrue(pair.isEmpty());
                assertFalse(pair.hasBothValues());
                assertFalse(pair.hasCurrent());
                assertFalse(pair.hasPrevious());
                break;
            case 1:
                assertFalse(pair.isEmpty());
                assertFalse(pair.hasBothValues());
                assertTrue(pair.hasCurrent());
                assertFalse(pair.hasPrevious());
                break;
            case 2:
                assertFalse(pair.isEmpty());
                assertTrue(pair.hasBothValues());
                assertTrue(pair.hasCurrent());
                assertTrue(pair.hasPrevious());
                break;
        }
    }
}
