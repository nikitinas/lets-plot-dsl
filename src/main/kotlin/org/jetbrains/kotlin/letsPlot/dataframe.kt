package org.jetbrains.kotlin.letsPlot

import jetbrains.letsPlot.geom.geom_bar
import jetbrains.letsPlot.geom.geom_histogram
import jetbrains.letsPlot.lets_plot
import krangl.*
import krangl.typed.TypedDataFrame
import krangl.typed.TypedDataFrameRow

fun <T> PlotBuilder<*>.line(data: TypedDataFrame<T>, body: LinesLayer<TypedDataFrameRow<T>>.() -> Unit) = line(data.rows, body)
fun <T> PlotBuilder<*>.bars(data: TypedDataFrame<T>, body: BarsLayer<TypedDataFrameRow<T>>.() -> Unit) = bars(data.rows, body)
fun <T> PlotBuilder<*>.vlines(data: TypedDataFrame<T>, body: VLinesLayer<TypedDataFrameRow<T>>.() -> Unit) = vlines(data.rows, body)
fun <T> PlotBuilder<*>.hlines(data: TypedDataFrame<T>, body: HLinesLayer<TypedDataFrameRow<T>>.() -> Unit) = hlines(data.rows, body)
fun <T> PlotBuilder<*>.histogram(data: TypedDataFrame<T>, body: HistogramLayer<TypedDataFrameRow<T>>.() -> Unit) = histogram(data.rows, body)
fun <T> PlotBuilder<*>.area(data: TypedDataFrame<T>, body: AreaLayer<TypedDataFrameRow<T>>.() -> Unit) = area(data.rows, body)
fun <T> PlotBuilder<*>.density(data: TypedDataFrame<T>, body: DensityLayer<TypedDataFrameRow<T>>.() -> Unit) = density(data.rows, body)
fun <T> TypedDataFrame<T>.plot(body: PlotBuilder<TypedDataFrameRow<T>>.() -> Unit) = rows.plot(body)

fun NumberCol.histogram() = lets_plot(mapOf(name to this.values().toList())) + geom_histogram()
fun DoubleCol.histogram() = lets_plot(mapOf(name to this.values.toList())) + geom_histogram()
fun NumberCol.bars() = lets_plot(mapOf(name to this.values().toList())) + geom_bar {x = name }
fun DoubleCol.bars() = lets_plot(mapOf(name to this.values().toList())) + geom_bar { x = name }