package com.github.sigrarr.lunisolarcalc.util.calccomposition;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;

import com.github.sigrarr.lunisolarcalc.util.calccomposition.ExampleComponents.*;
import com.github.sigrarr.lunisolarcalc.util.calccomposition.exceptions.*;

import org.junit.jupiter.api.*;

public class ProvidersRegisterTest {

    private ProvidersRegister<ExampleSubject, Integer> register = new ProvidersRegister<>(ExampleSubject.class);

    @Test
    public void shouldAddAndCheckAndGetAdded() {
        register.add(new AIndependentProvider());
        register.add(new BIndependentProvider());
        register.add(new FDependentOnADEProvider());

        assertTrue(register.has(ExampleSubject.A));
        assertTrue(register.has(ExampleSubject.B));
        assertFalse(register.has(ExampleSubject.C));
        assertFalse(register.has(ExampleSubject.D));
        assertFalse(register.has(ExampleSubject.E));
        assertTrue(register.has(ExampleSubject.F));

        assertInstanceOf(AIndependentProvider.class, register.getRequired(ExampleSubject.A).calculator);
        assertInstanceOf(BIndependentProvider.class, register.getRequired(ExampleSubject.B).calculator);
        assertInstanceOf(FDependentOnADEProvider.class, register.getRequired(ExampleSubject.F).calculator);
    }

    @Test
    public void shouldSetDependeesEagerly() {
        register.add(new CIndependentProvider());
        register.add(new EDependentOnBCProvider());
        assertHasTheseDependeesExcactly(ExampleSubject.E, EnumSet.of(ExampleSubject.C));

        register.add(new FDependentOnADEProvider());
        assertHasTheseDependeesExcactly(ExampleSubject.F, EnumSet.of(ExampleSubject.E));

        register.add(new DDependentOnAProvider());
        assertHasTheseDependeesExcactly(ExampleSubject.D, EnumSet.noneOf(ExampleSubject.class));

        register.add(new AIndependentProvider());
        assertHasTheseDependeesExcactly(ExampleSubject.D, EnumSet.of(ExampleSubject.A));
        assertHasTheseDependeesExcactly(ExampleSubject.F, EnumSet.of(ExampleSubject.A, ExampleSubject.D, ExampleSubject.E));

        register.add(new BIndependentProvider());
        assertHasTheseDependeesExcactly(ExampleSubject.E, EnumSet.of(ExampleSubject.B, ExampleSubject.C));
    }

    @Test
    public void shouldThrowProviderLackExceptionOnGettingAbsentSubject() {
        register.add(new AIndependentProvider());
        register.add(new BIndependentProvider());
        register.add(new FDependentOnADEProvider());

        ProviderLackException exception = assertThrows(ProviderLackException.class, () -> register.getRequired(ExampleSubject.C));
        assertEquals(ExampleSubject.C, exception.getMissingSubjects().iterator().next());

        exception = assertThrows(ProviderLackException.class, () -> register.getRequired(ExampleSubject.D));
        assertEquals(ExampleSubject.D, exception.getMissingSubjects().iterator().next());

        exception = assertThrows(ProviderLackException.class, () -> register.getRequired(ExampleSubject.E));
        assertEquals(ExampleSubject.E, exception.getMissingSubjects().iterator().next());
    }

    @Test
    public void shouldThrowDoubledProviderExceptionOnAddingSameSubjectTwice() {
        register.add(new AIndependentProvider());
        register.add(new CIndependentProvider());

        DoubledProviderException exception = assertThrows(DoubledProviderException.class, () -> register.add(new AIndependentProvider()));
        assertInstanceOf(AIndependentProvider.class, exception.getRegisteredProvider());
        assertInstanceOf(AIndependentProvider.class, exception.getRejectedProvider());

        exception = assertThrows(DoubledProviderException.class, () -> register.add(new CDangerouslyDependentOnFProvider()));
        assertInstanceOf(CIndependentProvider.class, exception.getRegisteredProvider());
        assertInstanceOf(CDangerouslyDependentOnFProvider.class, exception.getRejectedProvider());
    }

    private void assertHasTheseDependeesExcactly(ExampleSubject checkedSubject, Set<ExampleSubject> controlSubjects) {
        assertEquals(
            controlSubjects,
            register.getRequired(checkedSubject).directDependees.stream()
                .map(n -> n.calculator.provides())
                .collect(Collectors.toSet())
        );
    }
}
