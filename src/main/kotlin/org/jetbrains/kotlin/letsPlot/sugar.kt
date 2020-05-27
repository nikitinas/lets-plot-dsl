package org.jetbrains.kotlin.letsPlot

fun <T> Iterable<T>.plotBars(body: BarsLayer<T>.() -> Unit) = plot {
    bars(body)
}

fun <T> Iterable<T>.plotPoints(body: PointsLayer<T>.() -> Unit) = plot {
    points(body)
}

fun <T> Iterable<T>.plotHistogram(body: HistogramLayer<T>.() -> Unit) = plot {
    histogram(body)
}

fun <T> Iterable<T>.plotDensity(body: DensityLayer<T>.() -> Unit) = plot {
    density(body)
}

fun <T> Iterable<T>.plotArea(body: AreaLayer<T>.() -> Unit) = plot {
    area(body)
}

fun <T> Iterable<T>.plotVlines(body: VLinesLayer<T>.() -> Unit) = plot {
    vlines(body)
}

fun <T> Iterable<T>.plotHlines(body: HLinesLayer<T>.() -> Unit) = plot {
    hlines(body)
}

fun <T> Iterable<T>.plotLine(body: LinesLayer<T>.() -> Unit) = plot {
    line(body)
}

fun <T:Number> Iterable<T>.plotY(getY: (T)->Any) = plotLines {
    y.map(getY)
}