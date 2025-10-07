package com.github.sigrarr.lunisolarcalc.coords.global;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GlobalCoordTest {

    @Test
    public void shouldGetRightProvider() {
        for (GlobalCoord globalCoord : GlobalCoord.values())
            assertEquals(globalCoord, globalCoord.getProvider().provides());
    }
}
