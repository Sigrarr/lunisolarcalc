package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import static com.github.sigrarr.lunisolarcalc.util.calccomposition.ExampleComponents.completeComposer;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.Map.Entry;

import com.github.sigrarr.lunisolarcalc.util.calccomposition.ExampleComponents.*;

import org.junit.jupiter.api.Test;

public class MultiOutputCompositionTest {

    private static final Map<String, EnumSet<ExampleSubject>> NAMED_EXAMPLE_SUBJECT_SETS = new HashMap<String, EnumSet<ExampleSubject>>() {{
        put("A", EnumSet.of(ExampleSubject.A));
        put("BD", EnumSet.of(ExampleSubject.B, ExampleSubject.D));
        put("ABC", EnumSet.of(ExampleSubject.A, ExampleSubject.B, ExampleSubject.C));
        put("CDE", EnumSet.of(ExampleSubject.C, ExampleSubject.D, ExampleSubject.E));
        put("ABCDEF", EnumSet.allOf(ExampleSubject.class));
        put("", EnumSet.noneOf(ExampleSubject.class));
    }};
    private static final Map<String, Map<ExampleSubject, Integer>> SUBJECT_SET_NAME_TO_EXPECTED_EVALUATION_OF_ONE = new HashMap<String, Map<ExampleSubject, Integer>>() {{
        put("A", new HashMap<ExampleSubject, Integer>() {{
            put(ExampleSubject.A, 2);
        }});
        put("BD", new HashMap<ExampleSubject, Integer>() {{
            put(ExampleSubject.B, 3);
            put(ExampleSubject.D, 2 * 7);
        }});
        put("ABC", new HashMap<ExampleSubject, Integer>() {{
            put(ExampleSubject.A, 2);
            put(ExampleSubject.B, 3);
            put(ExampleSubject.C, 5);
        }});
        put("CDE", new HashMap<ExampleSubject, Integer>() {{
            put(ExampleSubject.C, 5);
            put(ExampleSubject.D, 2 * 7);
            put(ExampleSubject.E, 3 * 5 * 11);
        }});
        put("ABCDEF", new HashMap<ExampleSubject, Integer>() {{
            put(ExampleSubject.A, 2);
            put(ExampleSubject.B, 3);
            put(ExampleSubject.C, 5);
            put(ExampleSubject.D, 2 * 7);
            put(ExampleSubject.E, 3 * 5 * 11);
            put(ExampleSubject.F, 2 * (2 * 7) * (3 * 5 * 11) * 13);
        }});
        put("", new HashMap<>());
    }};

    private MultiOutputComposition<ExampleSubject, Integer> composition;

    @Test
    public void shouldCalculateSubjectToValueMap() {
        for (Entry<String, EnumSet<ExampleSubject>> entry : NAMED_EXAMPLE_SUBJECT_SETS.entrySet()) {
            String setName = entry.getKey();
            EnumSet<ExampleSubject> set = entry.getValue();
            composition = completeComposer.compose(set);
            assertEquals(SUBJECT_SET_NAME_TO_EXPECTED_EVALUATION_OF_ONE.get(setName), composition.calculate(1));
        }
    }

    @Test
    public void shouldReplicate() {
        for (EnumSet<ExampleSubject> subjectSet: NAMED_EXAMPLE_SUBJECT_SETS.values()) {
            composition = completeComposer.compose(subjectSet);
            MultiOutputComposition<ExampleSubject, Integer> replica = composition.replicate();
            assertNotEquals(composition, replica);
            for (int rootArgument = 0; rootArgument < 10; rootArgument++) {
                assertEquals(composition.calculate(rootArgument), replica.calculate(rootArgument));
            }
        }
    }
}
