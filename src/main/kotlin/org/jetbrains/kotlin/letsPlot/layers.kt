package org.jetbrains.kotlin.letsPlot

import jetbrains.letsPlot.Pos
import jetbrains.letsPlot.Stat
import jetbrains.letsPlot.intern.GeomKind
import jetbrains.letsPlot.intern.layer.PosOptions
import jetbrains.letsPlot.intern.layer.StatOptions

data class LayerContext<T>(val bindingsManager: DataBindings<T>, val plot: PlotBuilder<*>)

open class XYNumbersLayer<T>(
    context: LayerContext<T>, geomKind: GeomKind, stat: StatOptions = Stat.identity,
    position: PosOptions = Pos.identity
) :
    LayerBuilder<T>(context, geomKind, stat, position) {

    val x by bindProp<Any>()
    val y by bindProp<Any>()
}

class LinesLayer<T>(context: LayerContext<T>) : XYNumbersLayer<T>(context, GeomKind.LINE) {
    val linetype by prop<String>()
    val size by prop<Number>()
}

class PointsLayer<T>(context: LayerContext<T>) : XYNumbersLayer<T>(context, GeomKind.POINT) {
    val shape by prop<String>()
    val stroke by prop<Number>()
    val size by prop<Number>()
}

class VLinesLayer<T>(context: LayerContext<T>) : LayerBuilder<T>(context, GeomKind.V_LINE) {
    val xintercept by prop<Number>()
    val x = xintercept
    val linetype by prop<String>()
}

class HLinesLayer<T>(context: LayerContext<T>) : LayerBuilder<T>(context, GeomKind.H_LINE) {
    val yintercept by prop<Number>()
    val y = yintercept
    val linetype by prop<String>()
}

class BarsLayer<T>(context: LayerContext<T>) : LayerBuilder<T>(context, GeomKind.BAR, position = Pos.stack) {
    val x by bindProp<Any>()
    val y by bindProp<Any>()
    val width by prop<Double>()
    val size by prop<Double>()
}

class AreaLayer<T>(context: LayerContext<T>) : LayerBuilder<T>(context, GeomKind.AREA, position = Pos.stack) {
    val x by bindProp<Double>()
    val y by bindProp<Double>()
    val linetype by prop<String>()
    val size by prop<Double>()
}

class DensityLayer<T>(context: LayerContext<T>) :
    LayerBuilder<T>(context, GeomKind.DENSITY, stat = Stat.density()) {
    val x by bindProp<Double>()
    val y by bindProp<Double>()
    val width by prop<Double>()
    val size by prop<Double>()
}

class HistogramLayer<T>(context: LayerContext<T>) :
    LayerBuilder<T>(context, GeomKind.HISTOGRAM, stat = Stat.bin(), position = Pos.stack) {
    val x by bindProp<Double>()
    val y by bindProp<Double>()
    val width by prop<Double>()
    val size by prop<Double>()
}

fun <T> PlotBuilder<*>.lineLayer(data: Iterable<T>, body: LinesLayer<T>.() -> Unit) =
    addLayer(LinesLayer(createContext(data)), body)

fun <T> PlotBuilder<T>.lineLayer(body: LinesLayer<T>.() -> Unit = {}) = lineLayer(data, body)

fun <T> PlotBuilder<*>.line(data: Iterable<T>, color: Any? = null, alpha: Double? = null, size: Number? = null, type: String? = null, x: Getter<T, Any>? = null, y: Getter<T, Any>? = null) =
    lineLayer(data) {
        color?.let { this.color.set(it) }
        alpha?.let { this.alpha.set(it) }
        type?.let { this.linetype.set(it) }
        size?.let { this.size.set(it) }
        x?.let { this.x.map(it) }
        y?.let { this.y.map(it) }
    }

fun <T> PlotBuilder<T>.line(color: String? = null, alpha: Double? = null, size: Number? = null, type: String? = null, x: Getter<T, Any>? = null, y: Getter<T, Any>? = null) = line(data, color, alpha, size, type, x, y)

fun <T> PlotBuilder<*>.pointsLayer(data: Iterable<T>, body: PointsLayer<T>.() -> Unit) = addLayer(PointsLayer(createContext(data)), body)
fun <T> PlotBuilder<T>.pointsLayer(body: PointsLayer<T>.() -> Unit) = pointsLayer(data, body)

fun <T> PlotBuilder<*>.points(data: Iterable<T>, color: Any? = null, alpha: Double? = null, fill: Any? = null, shape: String? = null, stroke: Number? = null, size: Number? = null, x: Getter<T, Any>? = null, y: Getter<T, Any>? = null) =
    pointsLayer(data) {
        color?.let { this.color.set(it) }
        alpha?.let { this.alpha.set(it) }
        fill?.let { this.fill.set(it) }
        shape?.let { this.shape.set(it) }
        stroke?.let { this.stroke.set(it) }
        size?.let { this.size.set(it) }
        x?.let { this.x.map(it) }
        y?.let { this.y.map(it) }
    }

fun <T> PlotBuilder<T>.points(color: String? = null, alpha: Double? = null, fill: Any? = null, shape: String? = null, stroke: Number? = null, size: Number? = null, x: Getter<T, Any>? = null, y: Getter<T, Any>? = null) = points(data, color, alpha, fill, shape, stroke, size, x, y)

fun <T> PlotBuilder<*>.vlines(data: Iterable<T>, body: VLinesLayer<T>.() -> Unit) =
    addLayer(VLinesLayer(createContext(data)), body)

fun <T> PlotBuilder<T>.vlines(body: VLinesLayer<T>.() -> Unit = {}) = vlines(data, body)

fun <T> PlotBuilder<*>.hlines(data: Iterable<T>, body: HLinesLayer<T>.() -> Unit) =
    addLayer(HLinesLayer(createContext(data)), body)

fun <T> PlotBuilder<T>.hlines(body: HLinesLayer<T>.() -> Unit = {}) = hlines(data, body)

fun <T> PlotBuilder<*>.bars(data: Iterable<T>, body: BarsLayer<T>.() -> Unit) =
    addLayer(BarsLayer(createContext(data)), body)

fun <T> PlotBuilder<T>.bars(body: BarsLayer<T>.() -> Unit = {}) = bars(data, body)

fun <T> PlotBuilder<*>.area(data: Iterable<T>, body: AreaLayer<T>.() -> Unit) =
    addLayer(AreaLayer(createContext(data)), body)

fun <T> PlotBuilder<T>.area(body: AreaLayer<T>.() -> Unit = {}) = area(data, body)

fun <T> PlotBuilder<*>.density(data: Iterable<T>, body: DensityLayer<T>.() -> Unit) =
    addLayer(DensityLayer(createContext(data)), body)

fun <T> PlotBuilder<T>.density(body: DensityLayer<T>.() -> Unit = {}) = density(data, body)

fun <T> PlotBuilder<*>.histogram(data: Iterable<T>, body: HistogramLayer<T>.() -> Unit) =
    addLayer(HistogramLayer(createContext(data)), body)

fun <T> PlotBuilder<T>.histogram(body: HistogramLayer<T>.() -> Unit = {}) = histogram(data, body)