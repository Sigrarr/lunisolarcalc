# Luni-Solar Calc
A Java library for astronomical calculations regarding the Sun and the Moon.

\[WORK IN PROGRESS.\]

This project is mainly a selective implementation of "Astronomical Algorithms" (Meeus 1998) but does contain original solutions and utilities. [Handling of ΔT](#notes-about-handling-of-time) is based on a work by Morrison and Stephenson (Morrison & Stephenson 2004).

* [Purpose and limitations](#purpose-and-limitations)
* [Main functionalities](#main-functionalities)
    * [Phenomena](#phenomena)
    * [Space-by-time calculators](#space-by-time-calculators)
    * [Time](#time)
* [Notes about handling of time](#notes-about-handling-of-time)
* [References](#references)

## Purpose and limitations
Luni-Solar Calc is meant primarily for historical purposes, being best suited to the period from the year -700 (701 BCE) to +2000 (CE). However, the project may be reasonably used for many other purposes like astrology, illustrations or rough simulations of supported phenomena or just for daily-life checks of time of some events or positions of the celestial bodies.

If you need very accurate values for a scientific or technical application dealing with present times, this is probably not the best choice.

## Main functionalities
### Phenomena
The package `com.github.sigrarr.lunisolarcalc.phenomena` provides tools for finding occurrences of principal phases of the Moon and of (solar) equinoxes and solstices. Its two most important classes are:
* `MoonPhaseFinder`
* `SunSeasonPointFinder`

Both classes have several methods named "find(...)". Note that those called "findMany(...)" return **unterminated streams** - in order to avoid an infinite loop, you have to terminate such stream for yourself or enforce a stop condition in another way (the method `limit` is the simplest solution).

#### Example Ph.1
```java
MoonPhaseFinder finder = new MoonPhaseFinder();
TimelinePoint startAround = TimelinePoint.ofCalendarPoint(new CalendarPoint(2023, 1, 1.0));

TimelinePoint firstFullMoon2023 = finder
    .findMany(startAround, MoonPhase.FULL_MOON)
    .map(occurrence -> occurrence.timelinePoint.toUniversalTime())
    .filter(tp -> tp.toCalendarPoint().y == 2023)
    .findAny()
    .get();

System.out.println(firstFullMoon2023.formatCalendrically());
```
Output: `+2023-01-06 23:07 UT`

#### Example Ph.2
```java
new SunSeasonPointFinder()
    .findMany(1581)
    .limit(4)
    .map((occurrence) -> String.format("%s\t%s",
        occurrence.timelinePoint.formatCalendrically(),
        occurrence.type.getTitle())
    )
    .forEach(System.out::println);
```
Output:
```
+1581-03-10 18:03 TT    March Equinox
+1581-06-11 19:36 TT    June Solstice
+1581-09-13 06:56 TT    September Equinox
+1581-12-11 20:08 TT    December Sosltice
```

### Space-by-time calculators
The package `com.github.sigrarr.lunisolarcalc.spacebytime` provides a group of calculators of certain spatial coordinates. Some of them need to receive results yielded by the others, which constitutes a dependency graph. You can use the in-built *calculation composer* to resolve those dependencies (which is recommended) or do it manually. For the list of quantities supported by the package, see the `Subject` enumeration class. The package may be extended in the future.

#### Example S.1
```java
SingleOutputComposition<Subject, TimelinePoint> lambdaCalc
    = SpaceByTimeCalcComposition.compose(Subject.SUN_APPARENT_LONGITUDE);
TimelinePoint newYear2000 = TimelinePoint.ofCalendarPoint(new CalendarPoint(2000, 1, 1.0));
double newYear2000Lambda = (Double) lambdaCalc.calculate(newYear2000);

System.out.println(String.format("%.2f", Math.toDegrees(newYear2000Lambda)));
```
Output: `279.86` (over a week after the December solstice defined by λ = 270°).

### Time
The package `com.github.sigrarr.lunisolarcalc.time` contains classes regarding time.

`TimelinePoint` represents a single point of time; it is equipped with several static constructing methods (named "of...") and with methods converting to different representations of time (named "to...").

The subpackage `calendar` supports calendaric representations of time:
* `CalendarPoint` - represents a date-time in the standard Julian/Gregorian calendar (the Julian calendar up to +1582-10-04 and the Gregorian calendar from +1582-10-15 on)
* `ProlepticGregorianCalendarPoint`
* `ProlepticJulianCalendarPoint`

## Notes about handling of time

\[Under preparation.\]

## References
* J. **Meeus**, **1998**: *Astronomical Algorithms*, 2nd ed.
* L.V. **Morrison** & F.R. **Stephenson**, **2004**: *Historical values of the Earth's clock error ΔT and the calculation of eclipses*, Journal for the History of Astronomy, Vol. 35, Part 3, No. 120, pp. 327 - 336.
* P.K. **Seidelmann** (Ed.), **1992**: *Explanatory Supplement to the Astronomical Almanac*, U.S. Naval Observatory
