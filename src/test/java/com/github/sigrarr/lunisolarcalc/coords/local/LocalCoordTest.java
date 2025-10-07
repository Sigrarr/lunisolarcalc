package com.github.sigrarr.lunisolarcalc.coords.local;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.tutil.ExampleLocation;

public class LocalCoordTest {

    @Test
    public void shouldGetRightProvider() {
        for (LocalCoord localCoord : LocalCoord.values())
            assertEquals(localCoord.key(), localCoord.getProvider(ExampleLocation.WROCLAW).provides());
    }
}
