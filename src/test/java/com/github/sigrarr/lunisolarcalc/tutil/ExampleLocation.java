package com.github.sigrarr.lunisolarcalc.tutil;

import static com.github.sigrarr.lunisolarcalc.util.Calcs.Angle.toSingleDegreesValue;

import com.github.sigrarr.lunisolarcalc.phenomena.local.GeoCoords;

public abstract class ExampleLocation {
    // https://www.timeanddate.com/worldclock/@6620709
    public final static GeoCoords ADELAIDE = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue( -67, 13, 0)),
        Math.toRadians(toSingleDegreesValue( -68, 23, 0))
    );
    // https://www.timeanddate.com/worldclock/antarctica/belgrano-ii-base
    public final static GeoCoords BELGRANO2 = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue( -77, 52, 0)),
        Math.toRadians(toSingleDegreesValue( -34, 38, 0))
    );
    // https://www.timeanddate.com/worldclock/norway/bodo
    public final static GeoCoords BODOE = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  67, 17, 0)),
        Math.toRadians(toSingleDegreesValue(  14, 23, 0))
    );
    // https://www.timeanddate.com/worldclock/@3831442
    public final static GeoCoords ETAH = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  78, 19, 0)),
        Math.toRadians(toSingleDegreesValue( -72, 35, 0))
    );
    // https://www.timeanddate.com/worldclock/@7670547
    public final static GeoCoords GREENWICH_PARK = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue( 51, 29, 0)),
        0.0
    );
    // https://www.timeanddate.com/worldclock/usa/honolulu
    public final static GeoCoords HONOLULU = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  21, 19, 0)),
        Math.toRadians(toSingleDegreesValue(-157, 51, 0))
    );
    // https://www.timeanddate.com/worldclock/@2617832
    public final static GeoCoords LEJRE = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  55, 36, 0)),
        Math.toRadians(toSingleDegreesValue(  11, 58, 0))
    );
    // https://www.timeanddate.com/worldclock/norway/longyearbyen
    public final static GeoCoords LONGYEARBYEN = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  78, 13, 0)),
        Math.toRadians(toSingleDegreesValue(  15, 38, 0))
    );
    // https://www.timeanddate.com/worldclock/greenland/nuuk
    public final static GeoCoords NUUK = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  64, 10, 0)),
        Math.toRadians(toSingleDegreesValue( -51, 44, 0))
    );
    // https://www.timeanddate.com/worldclock/australia/sydney
    public final static GeoCoords SYDNEY = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue( -33, 52, 0)),
        Math.toRadians(toSingleDegreesValue( 151, 12, 0))
    );
    // https://www.timeanddate.com/worldclock/japan/tokyo
    public final static GeoCoords TOKYO = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  35, 41, 0)),
        Math.toRadians(toSingleDegreesValue( 139, 42, 0))
    );
    // https://www.timeanddate.com/worldclock/canada/vancouver
    public final static GeoCoords VANCOUVER = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  49, 17, 0)),
        Math.toRadians(toSingleDegreesValue(-123,  7, 0))
    );
    // https://www.timeanddate.com/worldclock/poland/wroclaw
    public final static GeoCoords WROCLAW = GeoCoords.ofConventional(
        Math.toRadians(toSingleDegreesValue(  51, 07, 0)),
        Math.toRadians(toSingleDegreesValue(  17, 02, 0))
    );
}
