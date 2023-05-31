# Luni-Solar Calc
A Java library for astronomical calculations regarding the Sun and the Moon.

This project is mainly a selective implementation of "Astronomical Algorithms" (Meeus 1998) but does contain original solutions and utilities. [Determining ΔT](#time-scales-and-δt) is based on a work by Morrison and Stephenson (Morrison & Stephenson 2004).

* [Purpose and limitations](#purpose-and-limitations)
* [Main functionalities](#main-functionalities)
    * [Phenomena](#phenomena)
    * [Calculators of spatial coordinates](#calculators-of-spatial-coordinates)
    * [Time](#time)
* [Notes about the handling of time](#notes-about-the-handling-of-time)
* [References](#references)

## Purpose and limitations
Luni-Solar Calc is intended primarily for historical purposes but may be reasonably used for many others, including astrology, simple educational presentations and just daily-life checking of astronomical events' time or the celestial bodies' positions. The project is applicable to the current Julian Period, i.e. −4712-01-01 12:00 of the Julian calendar to +3268-01-23 12:00 of the Gregorian calendar, and best suited to the interval from the year −700 to +2000.

Numbers generated by Luni-Solar Calc are fairly accurate in the context of the above-mentioned fields, but keep in mind that many of the algorithms being in use here are far away from the top of what is achievable by modern science and technology. If you need values of high accuracy and precision for a scientific or technical application dealing with present times, this is probably not the best choice.

## Main functionalities
### Phenomena
The package `com.github.sigrarr.lunisolarcalc.phenomena` provides tools for finding occurrences of
* global phenomena, including:
    * principal phases of the Moon
    * (solar) equinoxes and solstices
* local phenomena: rises, transits and sets of the Moon and the Sun.

The most important classes are:
* `global.`
    * `MoonPhaseFinder`
    * `SunSeasonPointFinder`
* `local.`
    * `MoonDiurnalPhaseFinder`
    * `SunDiurnalPhaseFinder`

Each of these classes has got several methods named "find(...)". Note that those called "findMany(...)" return **unterminated streams** – to avoid an infinite loop, you have to terminate such stream for yourself or enforce a stop condition in another way (the method `limit` is the simplest).

#### **Example Ph.1**
```java
TimelinePoint startAround = TimelinePoint.ofCalendaricParameters(2023, 1, 1.0);
TimelinePoint firstFullMoon2023 = new MoonPhaseFinder()
    .findMany(startAround, MoonPhase.FULL_MOON)
    .map(occurrence -> occurrence.toUniversalTime().getTimelinePoint())
    .filter(tp -> tp.toCalendarPoint().y == 2023)
    .findAny()
    .get();

System.out.println(firstFullMoon2023.formatCalendrically());
```
Output: `+2023-01-06 23:07 UT`

#### **Example Ph.2**
```java
new SunSeasonPointFinder()
    .findMany(1581)
    .limit(4)
    .map(occurrence -> String.format("%s\t%s",
        occurrence.getTimelinePoint().formatCalendrically(),
        occurrence.getType().getTitle()
    ))
    .forEach(System.out::println);
```
Output:
```
+1581-03-10 18:03 TT    March Equinox
+1581-06-11 19:36 TT    June Solstice
+1581-09-13 06:56 TT    September Equinox
+1581-12-11 20:08 TT    December Solstice
```

#### **Example Ph.3**
```java
GeoCoords wroclaw = GeoCoords.ofDegreesWithDirections(51.107883, N, 17.038538, E);
CalendarPoint beforeFirstFullMoon2023 = new CalendarPoint(2023, 1, 6.5);

new MoonDiurnalPhaseFinder()
    .findMany(beforeFirstFullMoon2023, wroclaw)
    .limit(9)
    .filter(Optional::isPresent)
    .map(Optional::get)
    .map(occurrence -> String.format("%s (%s)\t%s",
        occurrence.getTimelinePoint().toLocalTimeCalendarPoint(wroclaw).formatDateTimeToMinutes(),
        occurrence.getTimelinePoint().formatCalendrically(),
        occurrence.getType().getTitle()
    ))
    .forEach(System.out::println);
```
Output:
```
+2023-01-06 15:11 (+2023-01-06 14:03 UT)        Moon-Rise
+2023-01-07 00:08 (+2023-01-06 22:59 UT)        Moon-Transit
+2023-01-07 08:58 (+2023-01-07 07:49 UT)        Moon-Set
+2023-01-07 16:12 (+2023-01-07 15:04 UT)        Moon-Rise
+2023-01-08 00:58 (+2023-01-07 23:50 UT)        Moon-Transit
+2023-01-08 09:30 (+2023-01-08 08:22 UT)        Moon-Set
```
Note the date differences between UT and the local solar time and the absence of three occurrences due to the lack of transit at +2023-01-06 (local solar time).

#### **Example Ph.4**
```java
GeoCoords rome = GeoCoords.ofDegreesWithDirections(41.902782, N, 12.496366, E);

new SunDiurnalPhaseFinder()
    .findMany(CalendarPoint.GREGORIAN_RULES_START, rome)
    .limit(3)
    .map(Optional::get)
    .map(occurrence -> String.format("%s\t%s",
        occurrence.getTimelinePoint().toLocalTimeCalendarPoint(rome).formatDateTimeToMinutes(),
        occurrence.getType().getTitle()
    ))
    .forEach(System.out::println);
```
Output:
```
+1582-10-15 06:11       Sun-Rise
+1582-10-15 11:45       Sun-Transit
+1582-10-15 17:19       Sun-Set
```

### Calculators of spatial coordinates
The package `com.github.sigrarr.lunisolarcalc.coords` provides a group of calculators of certain spatial coordinates. Some of them need to receive results yielded by the others, which constitutes a dependency graph. You can use the in-built *calculation composer* to resolve those dependencies (which is recommended) or do it manually. For the list of quantities supported by the package, see the `Subject` enumeration class.

#### **Example S.1**
```java
SingleOutputComposition<Subject, TimelinePoint> lambdaCalc
    = CalcCompositions.compose(Subject.SUN_APPARENT_LONGITUDE);
TimelinePoint newYear2000 = TimelinePoint.ofCalendaricParameters(2000, 1, 1.0);
double newYear2000Lambda = (Double) lambdaCalc.calculate(newYear2000);

System.out.println(String.format("%.2f", Math.toDegrees(newYear2000Lambda)));
```
Output: `279.86` (over a week after the December solstice defined by λ = 270°).

### Time
The package `com.github.sigrarr.lunisolarcalc.time` contains classes regarding time.

`TimelinePoint` represents a single point of time. This abstract class is divided into two concrete subclasses: `UnivesalTimelinePoint` and `DynamicalTimelinePoint` – instances of both are comparable internally but not cross-comparable. Methods converting to different representations of time are named "to...", and static factory methods are named "of...".

The subpackage `calendar` supports calendaric representations of time:
* `CalendarPoint` – represents a date-time in the main calendar
* `ProlepticGregorianCalendarPoint`
* `ProlepticJulianCalendarPoint`

## Notes about the handling of time

### Units and notation
* The leading time unit is ***Julian Day (JD)*** – the number of days (with a fraction) from the beginning of the current Julian Period (−4712-01-01 12:00).
* The astronomical numbering of years is used: 0 for 1 BCE, −1 for 2 BCE, and so on.
* The term *calendar* without specification (or the "main calendar") refers to the normalized Julian/Gregorian calendar, which works as the Julian calendar up to +1582-10-04 and the Gregorian calendar from +1582-10-15 on. Dates are expressed in this calendar by default.
* The project uses a date/time format compliant with the ISO 8601 standard: `±YYYY-MM-DD` extended with `hh:mm` (or `hh:mm:ss`; hours are numbered from 0 to 23, no AM/PM). Examples:
    * `−0045-12-24 12:00` – the noon of the 25th December 46 BCE
    * `+1582-10-15 23:45` – the 15th October 1582 (CE), quarter to midnight
* ***UT*** stands for the Universal Time, \
***TT*** stands for the Dynamical Time (or "Terrestrial Time"), \
***ΔT*** = TT − UT, \
 *Julian Day* in the Dynamical Time is called *Julian Ephemeris Day* ***(JDE)***.\
  See below.

### Time scales and ΔT

Luni-Solar Calc distinguishes two time scales:
* ***Universal Time (UT)***. Based on the Earth's rotation and affected by its variability. The most common, civil time.
* ***Dynamical Time*** (*TD* in Meeus 1998, ***TT*** here). Independent of the variability of the Earth's rotation and so fit for astronomical calculations.

Phenomena-finders return results in both scales and it may be often useful to convert between the two. For this reason classes `Occurrence` (used for storing results) and `TimelinePoint` have got methods `toDynamicalTime` and `toUniversalTime`.

In this project, the time scales are understood in a general sense. The difference between *Barycentric Dynamical Time (TDB)* and *Terrestrial Dynamical Time (TDT)* is so small that differentiating between them in Luni-Solar Calc would be inadequate for the project's simplicity. However, some components work on the *Terrestrial Time (TT)* specifically.

The Luni-Solar Calc's in-built time scale converter is based on the work by Morrison & Stephenson (2004), which contains a table of ΔT values for certain points between −700 and +2000, suggesting linear interpolation between them, and also a quadratic formula for extrapolation before -700 and after +2000 (for more theoretical details, see the referenced work; for more information about the implementation, see the description of the class `...time.timescaledelta.BasisMinus700ToPlus2000Resolver`).

Remember that ΔT (and consequently any scale conversion) bears serious uncertainty, regarding both the future and the distant past. For antiquity, standard errors reach the levels of multiple minutes!

### Conversion vs ordering
In most cases converting a collection of time points between different representations of time won't affect their order. However, if a collection contains multiple points which are very close to each other, mapping the collection to a different representation may indeed change the outcome of their sorting (only because the same points may be classified as *equal* or not depending on the representation, e.g. the time scale).

## References
* J. **Meeus**, **1998**: *Astronomical Algorithms*, 2nd ed.
* L.V. **Morrison** & F.R. **Stephenson**, **2004**: *Historical values of the Earth's clock error ΔT and the calculation of eclipses*, Journal for the History of Astronomy, Vol. 35, Part 3, No. 120, pp. 327 - 336.
* P.K. **Seidelmann** (Ed.), **1992**: *Explanatory Supplement to the Astronomical Almanac*, U.S. Naval Observatory
