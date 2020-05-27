import io.kotlintest.shouldBe
import jetbrains.datalore.plot.config.Option
import org.jetbrains.kotlin.letsPlot.invoke
import org.jetbrains.kotlin.letsPlot.plot
import org.jetbrains.kotlin.letsPlot.points
import org.junit.Test
import java.time.*

class Tests(){

    @Test
    fun `datetime postprocess`(){

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
        val data = plot.spec[Option.Plot.DATA] as Map<String, List<Any>>
        data.size shouldBe 3
        val x = data["list0"] as List<Long>
        val zoneOffset = OffsetDateTime.now().offset
        val actualTimes = x.map { LocalDateTime.ofEpochSecond(it / 1000, 0, zoneOffset).toLocalDate() }
        actualTimes shouldBe times.map {it.second}
    }
}