import io.kotlintest.shouldBe
import jetbrains.datalore.plot.config.Option
import jetbrains.letsPlot.intern.GeomKind
import jetbrains.letsPlot.intern.StatKind
import org.jetbrains.kotlin.letsPlot.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime

class PostProcessors {

    fun PlotSpec.testBarsStat(statKind: StatKind){
        val layers = (spec[Option.Plot.LAYERS] as List<Map<String, Any>>)
        layers.size shouldBe 1
        val layer = layers[0]
        layer[Option.Layer.GEOM] shouldBe GeomKind.BAR.optionName()
        layer[Option.Layer.STAT] shouldBe statKind.optionName()
    }

    @Test
    fun `bars with y`(){
        (1..10).plotBars {
            x { it }
            y { it*2 }
        }.testBarsStat(StatKind.IDENTITY)
    }

    @Test
    fun `bars with inherited y`(){
        (1..10).plot {
            x { it }
            y { it*2 }
            bars()
        }.testBarsStat(StatKind.IDENTITY)
    }

    @Test
    fun `bars without y`(){
        (1..10).plot {
            x { it }
            bars()
        }.testBarsStat(StatKind.COUNT)
    }

    @Test
    fun `datetime axis`(){

        val times = (10 downTo 1).map { it to LocalDate.now().minusDays(it.toLong())}
        val plot = times.plot {
            x { second }
            y { first }.hue()
            points{
                fill{second}
            }
        }

        val scales = plot.spec[Option.Plot.SCALES] as List<Map<String, Any>>
        scales.size shouldBe 3
        scales[1][Option.Scale.DATE_TIME] shouldBe true
        scales[1][Option.Scale.AES] shouldBe "fill"
        scales[2][Option.Scale.DATE_TIME] shouldBe true
        scales[2][Option.Scale.AES] shouldBe "x"
        val data = plot.spec[Option.PlotBase.DATA] as Map<String, List<Any>>
        data.size shouldBe 3
        val x = data["x"] as List<Long>
        val zoneOffset = OffsetDateTime.now().offset
        val actualTimes = x.map { LocalDateTime.ofEpochSecond(it / 1000, 0, zoneOffset).toLocalDate() }
        actualTimes shouldBe times.map {it.second}
    }
}