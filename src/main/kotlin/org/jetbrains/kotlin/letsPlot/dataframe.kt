package org.jetbrains.kotlin.letsPlot

import org.jetbrains.dataframe.*
import jetbrains.letsPlot.geom.geom_bar
import jetbrains.letsPlot.geom.geom_histogram
import jetbrains.letsPlot.lets_plot
import org.jetbrains.dataframe.api.columns.ColumnData
import org.jetbrains.dataframe.impl.trackColumnAccess

fun <T> PlotBuilder<*>.line(data: DataFrame<T>, body: LinesLayer<DataRow<T>>.() -> Unit) = line(data.rows, body)
fun <T> PlotBuilder<*>.bars(data: DataFrame<T>, body: BarsLayer<DataRow<T>>.() -> Unit) = bars(data.rows, body)
fun <T> PlotBuilder<*>.vlines(data: DataFrame<T>, body: VLinesLayer<DataRow<T>>.() -> Unit) = vlines(data.rows, body)
fun <T> PlotBuilder<*>.hlines(data: DataFrame<T>, body: HLinesLayer<DataRow<T>>.() -> Unit) = hlines(data.rows, body)
fun <T> PlotBuilder<*>.histogram(data: DataFrame<T>, body: HistogramLayer<DataRow<T>>.() -> Unit) = histogram(data.rows, body)
fun <T> PlotBuilder<*>.area(data: DataFrame<T>, body: AreaLayer<DataRow<T>>.() -> Unit) = area(data.rows, body)
fun <T> PlotBuilder<*>.density(data: DataFrame<T>, body: DensityLayer<DataRow<T>>.() -> Unit) = density(data.rows, body)
fun <T> DataFrame<T>.plot(body: PlotBuilder<DataRow<T>>.() -> Unit) = rows.plot(DataFrameNameProvider(), body)

fun <T:Number> ColumnData<T>.histogram() = lets_plot(mapOf(name to this.toList())) + geom_histogram()
fun <T:Number> ColumnData<T>.bars() = lets_plot(mapOf(name to this.toList())) + geom_bar { x = name }

fun <T> DataFrame<T>.plotBars(body: BarsLayer<DataRow<T>>.() -> Unit) = plot {
    bars(body)
}

fun <T> DataFrame<T>.plotPoints(body: PointsLayer<DataRow<T>>.() -> Unit) = plot {
    points(body)
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
    line(body)
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