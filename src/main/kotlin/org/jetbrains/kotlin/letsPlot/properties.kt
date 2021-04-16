package org.jetbrains.kotlin.letsPlot

import jetbrains.datalore.plot.base.Aes
import jetbrains.datalore.plot.config.Option
import jetbrains.letsPlot.intern.Options
import jetbrains.letsPlot.intern.Scale

fun <C, T, P : BindableProperty<C, T>> P.set(values: Iterable<T>): P {
    this.values = values
    return this
}

fun <C, T, P : BindableProperty<C, T>> P.map(mapping: Getter<C, T>): P {
    this.mapping = mapping
    return this
}

fun <C, T, P : BindableProperty<C, T>> P.map(mapping: C.() -> T): P {
    this.mapping = { mapping(this) }
    return this
}

operator fun <C, T, P : BindableProperty<C, T>> P.invoke(mapping: Getter<C, T>) = map(mapping)

infix fun <C, T, P : BindableProperty<C, T>> P.to(mapping: Getter<C, T>) = map(mapping)

infix fun <C, T, P : BindableProperty<C, T>> P.of(mapping: Getter<C, T>) = map(mapping)

operator fun <C, T, P : BindableProperty<C, T>> P.invoke(values: Iterable<T>) = set(values)

operator fun <C, T, P : BindableProperty<C, T>> P.minus(mapping: Getter<C, T>) = map(mapping)

open class BindableProperty<C, T>(val name: String) {

    var mapping: Getter<C, T>? = null

    var values: Iterable<T>? = null

    val isInitialized get() = mapping != null || values != null
}

class ScaleableProperty<C, T>(name: String, val aes: Aes<*>) : BindableProperty<C, T>(name) {

    var scale: Scale? = null

    fun datetime(
            name: String? = null, breaks: List<Any>? = null,
            labels: List<String>? = null,
            limits: List<Any>? = null,
            expand: Any? = null,
            na_value: Any? = null
    ) {
        scale = Scale(
                aes,
                name, breaks, labels, limits, expand, na_value,
                otherOptions = Options(
                        mapOf(
                                Option.Scale.DATE_TIME to true
                        )
                )
        )
    }

    fun hue(
            h: Int? = null,
            c: Int? = null,
            l: Int? = null,
            h_start: Int? = null,
            direction: Int? = null,
            name: String? = null,
            breaks: List<Any>? = null,
            labels: List<String>? = null,
            limits: List<Any>? = null,
            expand: Any? = null,
            na_value: Any? = null,
            format: String? = null,
            guide: Any? = null,
            trans: String? = null) {
        scale = Scale(
                aes,
                name, breaks, labels, limits, expand, na_value, format, guide, trans,
                Options(
                        mapOf(
                                Option.Scale.HUE_RANGE to h,
                                Option.Scale.CHROMA to c,
                                Option.Scale.LUMINANCE to l,
                                Option.Scale.START_HUE to h_start,
                                Option.Scale.DIRECTION to direction,
                                Option.Scale.SCALE_MAPPER_KIND to "color_hue"
                        )
                ))
    }

    fun grey(
            start: Int?, end: Int?, direction: Int?,  // start, end: [0..100]. direction < 0 - reversed
            name: String? = null,
            breaks: List<Any>? = null,
            labels: List<String>? = null,
            limits: List<Any>? = null,
            expand: Any? = null,
            na_value: Any? = null,
            format: String? = null,
            guide: Any? = null,
            trans: String? = null
    ) {
        scale = Scale(
                aes,
                name, breaks, labels, limits, expand, na_value, format, guide, trans,
                Options(
                        mapOf(
                                Option.Scale.START to start,
                                Option.Scale.END to end,
                                Option.Scale.DIRECTION to direction,
                                Option.Scale.SCALE_MAPPER_KIND to "color_grey"
                        )
                )
        )
    }

    fun gradient(
            low: String, high: String,
            name: String? = null,
            breaks: List<Any>? = null,
            labels: List<String>? = null,
            limits: List<Any>? = null,
            expand: Any? = null,
            na_value: Any? = null,
            format: String? = null,
            guide: Any? = null,
            trans: String? = null
    ) {
        scale = Scale(
                aes,
                name, breaks, labels, limits, expand, na_value, format, guide, trans,
                Options(
                        mapOf(
                                Option.Scale.LOW to low,
                                Option.Scale.HIGH to high,
                                Option.Scale.SCALE_MAPPER_KIND to "color_gradient"
                        )
                )
        )
    }

    fun gradient2(
            low: String, mid: String, high: String, midpoint: Double = 0.0,
            name: String? = null,
            breaks: List<Any>? = null,
            labels: List<String>? = null,
            limits: List<Any>? = null,
            expand: Any? = null,
            na_value: Any? = null,
            format: String? = null,
            guide: Any? = null,
            trans: String? = null
    ) {
        scale = Scale(
                aes,
                name, breaks, labels, limits, expand, na_value, format, guide, trans,
                Options(
                        mapOf(
                                Option.Scale.LOW to low,
                                Option.Scale.HIGH to high,
                                Option.Scale.MID to mid,
                                Option.Scale.MIDPOINT to midpoint,
                                Option.Scale.SCALE_MAPPER_KIND to "color_gradient2"
                        )
                )
        )
    }
}

class WriteableProperty<C, T>(name: String) : BindableProperty<C, T>(name) {

    var constValue: T? = null

    operator fun invoke(value: T) = set(value)

    infix fun to(value: T) = set(value)

    infix fun of(value: T) = set(value)

    operator fun minus(value: T) = set(value)

    operator fun plusAssign(value: T) {
        set(value)
    }

    operator fun compareTo(value: T): Int {
        set(value)
        return 0
    }

    fun set(value: T) {
        this.constValue = value
    }
}