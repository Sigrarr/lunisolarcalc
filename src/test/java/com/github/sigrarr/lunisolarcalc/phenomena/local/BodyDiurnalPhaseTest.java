package com.github.sigrarr.lunisolarcalc.phenomena.local;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.phenomena.Body;

public class BodyDiurnalPhaseTest {

    @Test
    public void shouldGetByBodyAndPhase() {
        for (Body body : Body.values())
            for (DiurnalPhase phase : DiurnalPhase.values()) {
                BodyDiurnalPhase combined = BodyDiurnalPhase.of(body, phase);
                assertEquals(body, combined.body);
                assertEquals(phase, combined.diurnalPhase);
            }
    }
}
