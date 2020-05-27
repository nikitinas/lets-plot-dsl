import io.kotlintest.shouldBe
import jetbrains.datalore.plot.config.Option
import jetbrains.letsPlot.intern.GeomKind
import jetbrains.letsPlot.intern.StatKind
import jetbrains.letsPlot.scale.scale_color_hue
import jetbrains.letsPlot.scale.scale_fill_hue
import org.jetbrains.kotlin.letsPlot.*
import org.junit.Test
import java.time.*

class scales {

    @Test
    fun `hue`(){
        val spec1 = (1..10).plot {
            x {it}
            y {it*2}
            bars{
                fill{it}.hue()
            }
        }

        val spec2 = (1..10).plot {
            x {it}
            y {it*2}
            bars{
                fill{it}
            }
            +scale_fill_hue()
        }

        spec2 shouldBe spec1
    }
}