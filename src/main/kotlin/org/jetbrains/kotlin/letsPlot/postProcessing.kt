package org.jetbrains.kotlin.letsPlot

import jetbrains.datalore.plot.base.Aes
import jetbrains.datalore.plot.config.Option
import jetbrains.letsPlot.intern.GeomKind
import jetbrains.letsPlot.intern.StatKind
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId


interface SpecPostProcessor {
    fun process(spec: Map<String, Any>): Map<String, Any>
}

class BarsStatPostProcessor: SpecPostProcessor {
    override fun process(spec: Map<String, Any>): Map<String, Any> {
        val generalYMapping = (spec[Option.Plot.MAPPING] as? Map<String, String>)?.get(Aes.Y.name)
        val newLayers = (spec[Option.Plot.LAYERS] as? List<Map<String, Any>>)?.map {
            val geom = it[Option.Layer.GEOM] as? String
            if(geom != null && geom == GeomKind.BAR.optionName()){
                val yMapping = (it[Option.Plot.MAPPING] as? Map<String, String>)?.get(Aes.Y.name)
                val stat = it[Option.Layer.STAT] as? String
                if(yMapping == null && generalYMapping == null && (stat == null || stat == StatKind.IDENTITY.optionName()))
                {
                    val layer = it + mapOf(Option.Layer.STAT to StatKind.COUNT.optionName())
                    layer
                }else it
            }else it
        }.orEmpty()
        return spec + mapOf(Option.Plot.LAYERS to newLayers)
    }
}

class DateTimeAxisPostProcessor : SpecPostProcessor {
    override fun process(spec: Map<String, Any>): Map<String, Any> {
        val data = spec[Option.Plot.DATA] as? Map<String, List<Any?>> ?: return spec
        val timeLists = mutableListOf<String>()
        val newData = data.map { (name, values) ->
            val newValues: List<Any?> = when (values.firstOrNull()) {
                null -> values
                is LocalDate -> values.map { (it as? LocalDate)?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toEpochSecond()?.let { it * 1000 } }
                        .also { timeLists.add(name) }
                is LocalDateTime -> values.map { (it as? LocalDateTime)?.atZone(ZoneId.systemDefault())?.toEpochSecond()?.let { it * 1000 } }
                        .also { timeLists.add(name) }
                else -> values
            }
            (name to newValues)
        }.toMap()

        val timeAeses = ((spec[Option.Plot.LAYERS] as List<Map<String, Any>>) + spec)
                .mapNotNull { it[Option.Plot.MAPPING] as? Map<String, String> }
                .flatMap {
                    it.mapNotNull {
                        if (timeLists.contains(it.value)) it.key else null
                    }
                }

        val newScales = spec[Option.Plot.SCALES] as List<Map<String, Any>> + timeAeses.map {
            val s = HashMap<String, Any>()
            s[Option.Scale.AES] = it
            s[Option.Scale.DATE_TIME] = true
            s
        }
        return spec + mapOf(Option.Plot.DATA to newData, Option.Plot.SCALES to newScales)
    }
}