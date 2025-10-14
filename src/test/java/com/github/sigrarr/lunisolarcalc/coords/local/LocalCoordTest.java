package com.github.sigrarr.lunisolarcalc.coords.local;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.sigrarr.lunisolarcalc.subjects.GeoPosition;
import com.github.sigrarr.lunisolarcalc.testing.ExampleLocation;

public class LocalCoordTest {

    @Test
    public void shouldGetRightProvider() {
        GeoPosition gp = GeoPosition.of(ExampleLocation.WROCLAW);
        for (LocalCoord localCoord : LocalCoord.values())
            assertEquals(localCoord.key(), localCoord.getProvider(gp).provides());
    }
}
