/**
 * Calculators of certain spatial coordinates,
 * divided into `global` and `local` (by depedence on the observer's location on Earth).
 *
 * Some of them need to receive results yielded by the others, which constitutes a dependency graph.
 * You can use the {@linkplain com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions in-built calculation composer}
 * to resolve those dependencies (which is recommended) or do it manually.
 *
 * For the list of quantities supported by the package, see the enumeration classes
 * {@link com.github.sigrarr.lunisolarcalc.coords.global.GlobalCoord GlobalCoord}
 * and {@link com.github.sigrarr.lunisolarcalc.coords.global.GlobalCoord LocalCoord}.
 */
package com.github.sigrarr.lunisolarcalc.coords;
