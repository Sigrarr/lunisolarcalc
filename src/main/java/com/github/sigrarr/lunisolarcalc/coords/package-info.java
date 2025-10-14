/**
 * Spatial coordinates.
 *
 * Supported quantities are distributed between subpackages {@code global} and {@code local}
 * which contain calculators and enumerations:
 * {@link com.github.sigrarr.lunisolarcalc.coords.global.GlobalCoord GlobalCoord},
 * {@link com.github.sigrarr.lunisolarcalc.coords.local.LocalCoord LocalCoord}.
 *
 * Some of the calculators need to receive results yielded by the others, which constitutes a dependency graph.
 * You can use the {@linkplain com.github.sigrarr.lunisolarcalc.coords.CoordsCalcCompositions in-built calculation composer}
 * to resolve those dependencies (which is recommended) or do it manually.
 *
 * Classes
 * {@link com.github.sigrarr.lunisolarcalc.coords.Transformations Transformations}
 * and {@link com.github.sigrarr.lunisolarcalc.coords.Topo Topo}
 * provide useful static formulae.
 */
package com.github.sigrarr.lunisolarcalc.coords;
