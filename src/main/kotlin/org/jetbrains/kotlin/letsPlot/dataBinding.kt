package org.jetbrains.kotlin.letsPlot

typealias Getter<C, T> = C.(C) -> T

typealias Mapping<T> = Getter<T, Any?>

interface MappingNameProvider {

    fun <T> getName(data: Iterable<T>, mapping: Mapping<T>, propertyName: String): String
}

interface BindingsManager {

    fun <T> getBindings(data: Iterable<T>): DataBindings<T>
}

interface DataBindings<T>: BindingsManager {

    val dataSource: Map<String, List<Any?>>

    val data: Iterable<T>

    fun makeUnique(name: String): String

    fun getDataName(mapping: Mapping<T>, propertyName: String): String
}

class DefaultMappingNameProvider : MappingNameProvider {

    override fun <T> getName(data: Iterable<T>, mapping: Mapping<T>, propertyName: String) = propertyName
}

class BindingsManagerImpl(private val nameProvider: MappingNameProvider) : BindingsManager {

    private val bindingsMap = mutableMapOf<Iterable<*>, DataBindingsImpl<*>>()

    override fun <T> getBindings(data: Iterable<T>) =
            bindingsMap.getOrPut(data, { DataBindingsImpl(data, this, nameProvider) }) as DataBindings<T>

}

class DataBindingsImpl<T>(override val data: Iterable<T>, private val owner: BindingsManagerImpl, private val nameProvider: MappingNameProvider) : DataBindings<T> {

    private val names = mutableMapOf<Mapping<T>, String>()

    override fun makeUnique(name: String): String {
        var res = name
        var counter = 2
        while(names.containsValue(res))
            res = name + " (${counter++})"
        return res
    }

    override fun getDataName(mapping: Mapping<T>, propertyName: String) = names.getOrPut(mapping) { nameProvider.getName(data, mapping, propertyName).let(::makeUnique) }

    override fun <C> getBindings(values: Iterable<C>) = owner.getBindings(values)

    override val dataSource by lazy {
        names.map { it.value to data.map { v -> it.key(v, v) } }.toMap()
    }
}