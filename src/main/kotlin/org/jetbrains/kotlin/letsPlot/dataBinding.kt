package org.jetbrains.kotlin.letsPlot

typealias Getter<C, T> = C.(C) -> T

typealias Mapping<T> = Getter<T, Any?>

interface MappingNameProvider {

    fun <T> getName(data: Iterable<T>, mapping: Mapping<T>, propertyName: String): String
}

class DefaultMappingNameProvider : MappingNameProvider {

    override fun <T> getName(data: Iterable<T>, mapping: Mapping<T>, propertyName: String) = propertyName
}

class BindingsManager(private val nameProvider: MappingNameProvider) {

    private val bindingsMap = mutableMapOf<Iterable<*>, DataBindings<*>>()

    fun <T> getManager(data: Iterable<T>) =
            bindingsMap.getOrPut(data, { DataBindings(data, this, nameProvider) }) as DataBindings<T>

}

class DataBindings<T>(val data: Iterable<T>, private val owner: BindingsManager, private val nameProvider: MappingNameProvider) {

    private val names = mutableMapOf<Mapping<T>, String>()

    private fun makeUnique(name: String): String {
        var res = name
        var counter = 2
        while(names.containsValue(res))
            res = name + " (${counter++})"
        return res
    }

    fun getDataName(mapping: Mapping<T>, propertyName: String) = names.getOrPut(mapping) { nameProvider.getName(data, mapping, propertyName).let(::makeUnique) }

    fun <C> getManager(values: Iterable<C>) = owner.getManager(values)

    val dataSource by lazy {
        names.map { it.value to data.map { v -> it.key(v, v) } }.toMap()
    }
}