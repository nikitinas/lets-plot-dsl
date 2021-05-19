package org.jetbrains.kotlin.letsPlot

import jetbrains.letsPlot.geom.geomBar
import jetbrains.letsPlot.geom.geomHistogram
import jetbrains.letsPlot.letsPlot
import org.jetbrains.dataframe.*
import org.jetbrains.dataframe.columns.DataColumn
import org.jetbrains.dataframe.impl.trackColumnAccess

fun <T> PlotBuilder<*>.lineLayer(data: DataFrame<T>, body: LinesLayer<DataRow<T>>.() -> Unit) = lineLayer(data.rows(), body)
fun <T> PlotBuilder<*>.bars(data: DataFrame<T>, body: BarsLayer<DataRow<T>>.() -> Unit) = bars(data.rows(), body)
fun <T> PlotBuilder<*>.vlines(data: DataFrame<T>, body: VLinesLayer<DataRow<T>>.() -> Unit) = vlines(data.rows(), body)
fun <T> PlotBuilder<*>.hlines(data: DataFrame<T>, body: HLinesLayer<DataRow<T>>.() -> Unit) = hlines(data.rows(), body)
fun <T> PlotBuilder<*>.histogram(data: DataFrame<T>, body: HistogramLayer<DataRow<T>>.() -> Unit) = histogram(data.rows(), body)
fun <T> PlotBuilder<*>.area(data: DataFrame<T>, body: AreaLayer<DataRow<T>>.() -> Unit) = area(data.rows(), body)
fun <T> PlotBuilder<*>.density(data: DataFrame<T>, body: DensityLayer<DataRow<T>>.() -> Unit) = density(data.rows(), body)
fun <T> DataFrame<T>.plot(body: PlotBuilder<DataRow<T>>.() -> Unit) = rows().plot(DataFrameNameProvider(), body)

fun <T:Number> DataColumn<T>.histogram() = letsPlot(mapOf(name() to toList())) + geomHistogram { x = name() }
fun <T:Number> DataColumn<T>.bars() = letsPlot(mapOf(name() to toList())) + geomBar { x = name() }

fun <T> DataFrame<T>.plotBars(body: BarsLayer<DataRow<T>>.() -> Unit) = plot {
    bars(body)
}

fun <T> DataFrame<T>.plotPoints(body: PointsLayer<DataRow<T>>.() -> Unit) = plot {
    pointsLayer(body)
}

fun <T> DataFrame<T>.plotHistogram(body: HistogramLayer<DataRow<T>>.() -> Unit) = plot {
    histogram(body)
}

fun <T> DataFrame<T>.plotDensity(body: DensityLayer<DataRow<T>>.() -> Unit) = plot {
    density(body)
}

fun <T> DataFrame<T>.plotArea(body: AreaLayer<DataRow<T>>.() -> Unit) = plot {
    area(body)
}

fun <T> DataFrame<T>.plotVlines(body: VLinesLayer<DataRow<T>>.() -> Unit) = plot {
    vlines(body)
}

fun <T> DataFrame<T>.plotHlines(body: HLinesLayer<DataRow<T>>.() -> Unit) = plot {
    hlines(body)
}

fun <T> DataFrame<T>.plotLine(body: LinesLayer<DataRow<T>>.() -> Unit) = plot {
    lineLayer(body)
}

class DataFrameNameProvider : MappingNameProvider {

    private val defaultProvider = DefaultMappingNameProvider()

    override fun <T> getName(data: Iterable<T>, mapping: Mapping<T>, propertyName: String) =
            data.firstOrNull()?.let {
                val columns = trackColumnAccess { mapping(it, it) }
                if (columns.isEmpty()) defaultProvider.getName(data, mapping, propertyName)
                else columns.joinToString()
            } ?: defaultProvider.getName(data, mapping, propertyName)
}