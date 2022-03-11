package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import static com.github.sigrarr.lunisolarcalc.util.calccomposition.ExampleComponents.completeComposer;
import static org.junit.jupiter.api.Assertions.*;

import com.github.sigrarr.lunisolarcalc.util.calccomposition.ExampleComponents.*;

import org.junit.jupiter.api.Test;

public class SingleOutputCompositionTest {

    private SingleOutputComposition<ExampleSubject, Integer> composition;

    @Test
    public void shouldCalculateSingleValue() {
        composition = completeComposer.compose(ExampleSubject.A);
        assertEquals(2, composition.calculate(1));
        assertEquals(17 * 2, composition.calculate(17));

        composition = completeComposer.compose(ExampleSubject.B);
        assertEquals(3, composition.calculate(1));
        assertEquals(17 * 3, composition.calculate(17));

        composition = completeComposer.compose(ExampleSubject.C);
        assertEquals(5, composition.calculate(1));
        assertEquals(17 * 5, composition.calculate(17));

        composition = completeComposer.compose(ExampleSubject.D);
        assertEquals(2 * 7, composition.calculate(1));
        assertEquals(17 * 2 * 7, composition.calculate(17));

        composition = completeComposer.compose(ExampleSubject.E);
        assertEquals(3 * 5 * 11, composition.calculate(1));
        assertEquals((17 * 3) * (17 * 5) * 11, composition.calculate(17));

        composition = completeComposer.compose(ExampleSubject.F);
        assertEquals(2 * (2 * 7) * (3 * 5 * 11) * 13, composition.calculate(1));     
        assertEquals((17 * 2) * (17 * 2 * 7) * ((17 * 3) * (17 * 5) * 11) * 13, composition.calculate(17));
    }

    @Test
    public void shouldReplicate() {
        for (ExampleSubject subject : ExampleSubject.values()) {
            composition = completeComposer.compose(subject);
            SingleOutputComposition<ExampleSubject, Integer> replica = composition.replicate();
            assertNotEquals(composition, replica);
            for (int rootArgument = 0; rootArgument < 10; rootArgument++) {
                assertEquals(composition.calculate(rootArgument), replica.calculate(rootArgument));
            }
        }
    }
}
