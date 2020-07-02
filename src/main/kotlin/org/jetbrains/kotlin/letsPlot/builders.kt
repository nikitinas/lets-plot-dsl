package org.jetbrains.kotlin.letsPlot

import jetbrains.datalore.plot.base.Aes
import jetbrains.datalore.plot.config.Option
import jetbrains.letsPlot.Pos
import jetbrains.letsPlot.Stat
import jetbrains.letsPlot.ggsize
import jetbrains.letsPlot.intern.*
import jetbrains.letsPlot.intern.layer.PosOptions
import jetbrains.letsPlot.intern.layer.StatOptions
import kotlin.reflect.KProperty

open class BuilderBase<T>(val bindings: DataBindings<T>) {

    class PropertyProvider<C, T, P : BindableProperty<C, T>>(private val owner: BuilderBase<C>, val create: (String) -> P) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): P {
            return owner.properties.getOrPut(property.name) { create(property.name) } as P
        }
    }

    val data: Iterable<T> get() = bindings.data

    internal val properties = mutableMapOf<String, BindableProperty<T, *>>()

    protected fun <P> prop() = PropertyProvider<T, P, WriteableProperty<T, P>>(this) { WriteableProperty(it) }

    protected fun <P> bindProp() = PropertyProvider<T, P, BindableProperty<T, P>>(this) { BindableProperty(it) }

    protected fun <P> scaleProp(aes: Aes<*>) = PropertyProvider<T, P, ScaleableProperty<T, P>>(this) { ScaleableProperty(it, aes) }

    fun <P> map(selector: Getter<T, P>) = selector

    internal open fun getSpec(): Map<String, Any> = collectParameters() + (Option.Plot.MAPPING to collectMappings())

    private fun collectParameters() =
            properties.mapValues { (it.value as? WriteableProperty<T, *>)?.constValue }
                    .filterNonNullValues()

    private fun collectMappings() =
            properties.mapValues {prop ->
                prop.value.mapping?.let{ bindings.getDataName(it, prop.key) }
            }.filterNonNullValues()

}

open class GenericBuilder<T>(bindings: DataBindings<T>) : BuilderBase<T>(bindings) {
    val alpha by prop<Double>()
    val color by scaleProp<Any>(Aes.COLOR)
    val fill by scaleProp<Any>(Aes.FILL)
}

class PlotBuilder<T>(data: DataBindings<T>) : GenericBuilder<T>(data) {

    val x by scaleProp<Any>(Aes.X)
    val y by scaleProp<Any>(Aes.Y)

    private val layers = mutableListOf<BuilderBase<*>>()

    private val otherFeatures = mutableListOf<OtherPlotFeature>()

    private val otherScales = mutableListOf<Scale>()

    private val postProcessors = listOf(DateTimeAxisPostProcessor(), BarsStatPostProcessor())

    private fun collectScales() =
            (layers.flatMap { it.properties.values } + properties.values).mapNotNull { (it as? ScaleableProperty<T, *>)?.scale }


    internal fun <C, B : BuilderBase<C>> addLayer(builder: B, body: B.() -> Unit) {
        layers.add(builder)
        body(builder)
    }

    internal fun <C> createContext(data: Iterable<C>) = LayerContext<C>(bindings.getBindings(data), this)

    private fun collectLayers() = if (layers.isNotEmpty()) layers
    else if (x.isInitialized && y.isInitialized) listOf(PointsLayer(createContext(data)))
    else if (x.isInitialized) listOf(BarsLayer(createContext(data)))
    else emptyList()

    override fun getSpec() = (super.getSpec() + mapOf(
            Option.Meta.KIND to Option.Meta.Kind.PLOT,
            Option.Plot.LAYERS to collectLayers().map { it.getSpec() },
            Option.Plot.DATA to bindings.dataForSpec,
            Option.Plot.SCALES to (collectScales() + otherScales).map { it.toSpec() }
    ) + otherFeatures.map { it.kind to it.toSpec() }).let {
        postProcessors.fold(it) { spec, processor -> processor.process(spec) }
    }

    operator fun OtherPlotFeature.unaryPlus() {
        otherFeatures.add(this)
    }

    operator fun Scale.unaryPlus() {
        otherScales.add(this)
    }
}

open class LayerBuilder<T>(
        context: LayerContext<T>,
        private val geomKind: GeomKind,
        var stat: StatOptions = Stat.identity,
        var position: PosOptions = Pos.identity
) : GenericBuilder<T>(context.bindingsManager) {

    private val plot: PlotBuilder<*> = context.plot
    override fun getSpec() = (super.getSpec() +
            mapOf<String, Any>(
                    Option.Layer.GEOM to geomKind.optionName(),
                    Option.Layer.STAT to stat.kind.optionName(),
                    Option.Layer.POS to position.kind.optionName()
            ) + stat.parameters.map).let {
        if (bindings.data != plot.bindings.data) it + (Option.Plot.DATA to bindings.dataForSpec)
        else it
    }

    val density get() = Stat.density()
    val count get() = Stat.count()
    val bin get() = Stat.bin()
    val boxplot get() = Stat.boxplot()
}

fun PlotBuilder<*>.size(width: Int, height: Int) =
        +ggsize(width, height)
