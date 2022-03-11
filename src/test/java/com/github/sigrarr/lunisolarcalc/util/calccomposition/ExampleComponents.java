package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import java.util.*;

/**
 * C'~ C   B    A
 * |   ^   ^   ^ ^
 * |   \ E /   D |
 *  \    ^     ^ |
 *   \--> \ F /_/
 */
class ExampleComponents {    

    static enum ExampleSubject {
        A(2), B(3), C(5), D(7), E(11), F(13);

        public final int value;

        private ExampleSubject(int value) {
            this.value = value;
        }
    }

    static CalculationComposer<ExampleSubject, Integer> completeComposer = new CalculationComposer<ExampleSubject, Integer>(ExampleSubject.class) {{
        register(new AIndependentProvider());
        register(new BIndependentProvider());
        register(new CIndependentProvider());
        register(new DDependentOnAProvider());
        register(new EDependentOnBCProvider());
        register(new FDependentOnADEProvider());        
    }};

    static abstract class ExampleProvider implements Provider<ExampleSubject, Integer> {

        private int calcCount = 0;

        @Override public Object calculate(Integer rootArgument, Map<ExampleSubject, Object> calculatedValues) {
            calcCount++;
            int base = requires().isEmpty() ? rootArgument : selectRequiredAndMultiply(requires(), calculatedValues);
            return base * provides().value;
        }

        @Override public Provider<ExampleSubject, Integer> getInstanceForNewComposition() {
            try {
                return this.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }

        int getCalcCount() {
            return calcCount;
        }

        int selectRequiredAndMultiply(EnumSet<ExampleSubject> required, Map<ExampleSubject, Object> calculatedValues) {
            return required.stream().map(s -> (Integer) calculatedValues.get(s)).reduce(1, (a, b) -> a * b);
        }
    }

    static class AIndependentProvider extends ExampleProvider {
        @Override public EnumSet<ExampleSubject> requires() {
            return EnumSet.noneOf(ExampleSubject.class);
        }

        @Override public ExampleSubject provides() {
            return ExampleSubject.A;
        }
    }

    static class ADangerouslyDependentOnItselfProvider extends ExampleProvider {
        @Override public EnumSet<ExampleSubject> requires() {
            return EnumSet.of(ExampleSubject.A);
        }

        @Override public ExampleSubject provides() {
            return ExampleSubject.A;
        }
    }

    static class BIndependentProvider extends ExampleProvider {
        @Override public EnumSet<ExampleSubject> requires() {
            return EnumSet.noneOf(ExampleSubject.class);
        }

        @Override public ExampleSubject provides() {
            return ExampleSubject.B;
        }
    }

    static class CIndependentProvider extends ExampleProvider {
        @Override public EnumSet<ExampleSubject> requires() {
            return EnumSet.noneOf(ExampleSubject.class);
        }

        @Override public ExampleSubject provides() {
            return ExampleSubject.C;
        }
    }

    static class CDangerouslyDependentOnFProvider extends ExampleProvider {
        @Override public EnumSet<ExampleSubject> requires() {
            return EnumSet.of(ExampleSubject.F);
        }

        @Override public ExampleSubject provides() {
            return ExampleSubject.C;
        }
    }

    static class DDependentOnAProvider extends ExampleProvider {
        @Override public EnumSet<ExampleSubject> requires() {
            return EnumSet.of(ExampleSubject.A);
        }

        @Override public ExampleSubject provides() {
            return ExampleSubject.D;
        }
    }

    static class EDependentOnBCProvider extends ExampleProvider {
        @Override public EnumSet<ExampleSubject> requires() {
            return EnumSet.of(ExampleSubject.B, ExampleSubject.C);
        }

        @Override public ExampleSubject provides() {
            return ExampleSubject.E;
        }
    }

    static class FDependentOnADEProvider extends ExampleProvider {
        @Override public EnumSet<ExampleSubject> requires() {
            return EnumSet.of(ExampleSubject.D, ExampleSubject.A, ExampleSubject.E);
        }

        @Override public ExampleSubject provides() {
            return ExampleSubject.F;
        }
    }

    static class ExampleCompositionClass extends Composition<ExampleSubject, Integer> {
        public ExampleCompositionClass(Collection<CompositionNode<ExampleSubject, Integer>> orderedNodes, Class<ExampleSubject> subjectEnumClass) {
            super(orderedNodes, subjectEnumClass);
        }

        public ExampleCompositionClass(Composition<ExampleSubject, Integer> composition) {
            super(composition);
        }

        @Override public Composition<ExampleSubject, Integer> replicate() {
            throw new UnsupportedOperationException();
        }
    }
}
