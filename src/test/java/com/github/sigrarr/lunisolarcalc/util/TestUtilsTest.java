package com.github.sigrarr.lunisolarcalc.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

public class TestUtilsTest {

    @Test
    public void shouldDetermineDecimalAutoDelta() {
        assertEquals(Calcs.EPSILON, TestUtils.decimalAutoDelta( 0.0), Calcs.EPSILON_MIN);
        assertEquals(0.5, TestUtils.decimalAutoDelta( 1.0), Calcs.EPSILON_MIN);
        assertEquals(0.05, TestUtils.decimalAutoDelta( 0.1), Calcs.EPSILON_MIN);
        assertEquals(0.0000005, TestUtils.decimalAutoDelta(-12.123456), Calcs.EPSILON_MIN);
        assertEquals(0.0000000000005, TestUtils.decimalAutoDelta(400.123456789012), Calcs.EPSILON_MIN);
        assertEquals(Calcs.EPSILON_MIN, TestUtils.decimalAutoDelta(0.1234567890123456789012345678901), 0.0000000000000000000000000000001);
    }

    @Test
    public void shouldAssertEquivalence() {
        assertDoesNotThrow(() -> TestUtils.assertEquivalence("abc", "abc"));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertNonEquivalence("abc", "abc"));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertEquivalence("a", "c"));
        assertDoesNotThrow(() -> TestUtils.assertNonEquivalence("a", "c"));

        PartialHashCell a1 = new PartialHashCell("a", 1);
        PartialHashCell b1 = new PartialHashCell("b", 1);
        PartialHashCell a1bis = new PartialHashCell("a", 1);
        PartialHashCell c2 = new PartialHashCell("c", 2);
        assertNotEquals(a1, b1);
        assertEquals(a1.hashCode(), b1.hashCode());

        assertThrows(AssertionFailedError.class, () -> TestUtils.assertEquivalence(a1, b1));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertNonEquivalence(a1, b1));
        assertDoesNotThrow(() -> TestUtils.assertEquivalence(a1, a1bis));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertNonEquivalence(a1, a1bis));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertEquivalence(a1, c2));
        assertDoesNotThrow(() -> TestUtils.assertNonEquivalence(a1, c2));

        PartialEqualsCell c3 = new PartialEqualsCell("c", 3);
        PartialEqualsCell c4 = new PartialEqualsCell("c", 4);
        PartialEqualsCell c4bis = new PartialEqualsCell("c", 4);
        PartialEqualsCell d5 = new PartialEqualsCell("d", 5);
        assertEquals(c3, c4);
        assertNotEquals(c3.hashCode(), c4.hashCode());

        assertThrows(AssertionFailedError.class, () -> TestUtils.assertEquivalence(c3, c4));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertNonEquivalence(c3, c4));
        assertDoesNotThrow(() -> TestUtils.assertEquivalence(c4, c4bis));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertNonEquivalence(c4, c4bis));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertEquivalence(c3, d5));
        assertDoesNotThrow(() -> TestUtils.assertNonEquivalence(c3, d5));
    }

    @Test
    public void shouldAssertConsistentEquivalence() {
        assertDoesNotThrow(() -> TestUtils.assertConsistentEquivalence("abc", "abc"));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertConsistentNonEquivalence("abc", "abc"));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertConsistentEquivalence("a", "c"));
        assertDoesNotThrow(() -> TestUtils.assertConsistentNonEquivalence("a", "c"));

        OverEquatingCmpCell x0 = new OverEquatingCmpCell("x", 0);
        OverEquatingCmpCell x5 = new OverEquatingCmpCell("x", 5);
        OverEquatingCmpCell x0bis = new OverEquatingCmpCell("x", 0);
        OverEquatingCmpCell x11 = new OverEquatingCmpCell("x", 11);
        TestUtils.assertNonEquivalence(x0, x5);
        assertEquals(0, x0.compareTo(x5));

        assertThrows(AssertionFailedError.class, () -> TestUtils.assertConsistentEquivalence(x0, x5));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertConsistentNonEquivalence(x0, x5));
        assertDoesNotThrow(() -> TestUtils.assertConsistentEquivalence(x0, x0bis));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertConsistentNonEquivalence(x0, x0bis));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertConsistentEquivalence(x0, x11));
        assertDoesNotThrow(() -> TestUtils.assertConsistentNonEquivalence(x0, x11));

        Cell a1 = new Cell("a", 1);
        Cell a1bis = new Cell("a", 1);
        Cell b2 = new Cell("b", 2);
        Cell b2bis = new Cell("b", 2);
        TestUtils.assertEquivalence(a1, a1bis);
        assertNotEquals(0, cellCmpWithACaveat.compare(a1, a1bis));

        assertThrows(AssertionFailedError.class, () -> TestUtils.assertConsistentEquivalence(a1, a1bis, cellCmpWithACaveat));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertConsistentNonEquivalence(a1, a1bis, cellCmpWithACaveat));
        assertDoesNotThrow(() -> TestUtils.assertConsistentEquivalence(b2, b2bis, cellCmpWithACaveat));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertConsistentNonEquivalence(b2, b2bis, cellCmpWithACaveat));
        assertThrows(AssertionFailedError.class, () -> TestUtils.assertConsistentEquivalence(a1, b2, cellCmpWithACaveat));
        assertDoesNotThrow(() -> TestUtils.assertConsistentNonEquivalence(a1, b2, cellCmpWithACaveat));
    }

    private class Cell {
        String word;
        int number;
        Cell(String word, int number) {
            this.word = word;
            this.number = number;
        }

        @Override public boolean equals(Object o) {
            if (!(o instanceof Cell))
                return false;
            Cell b = (Cell) o;
            return word.equals(b.word) && number == b.number;
        }

        @Override public int hashCode() {
            return Objects.hash(word, number);
        }
    }

    private class PartialHashCell extends Cell {
        PartialHashCell(String word, int number) {
            super(word, number);
        }

        @Override public int hashCode() {
            return number;
        }
    }

    private class PartialEqualsCell extends Cell {
        PartialEqualsCell(String word, int number) {
            super(word, number);
        }

        @Override public boolean equals(Object o) {
            if (!(o instanceof PartialEqualsCell))
                return false;
            PartialEqualsCell pe = (PartialEqualsCell) o;
            return word.equals(pe.word);
        }
    }

    private class OverEquatingCmpCell extends Cell implements Comparable<OverEquatingCmpCell> {
        OverEquatingCmpCell(String word, int number) {
            super(word, number);
        }

        @Override public int compareTo(OverEquatingCmpCell o) {
            int cmp = word.compareTo(o.word);
            if (cmp == 0) {
                int numberDiff = number - o.number;
                cmp = Math.abs(numberDiff) < 10 ? 0 : numberDiff;
            }
            return cmp;
        }
    }

    private static Comparator<Cell> cellCmpWithACaveat = (a, b) -> {
        int cmp = a.word.compareTo(b.word);
        if (cmp == 0)
            cmp = Integer.compare(a.number, b.number);
        return cmp == 0 && a.word.startsWith("a") ? -1 : cmp;
    };
}
