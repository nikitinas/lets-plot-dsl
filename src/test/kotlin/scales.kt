import io.kotlintest.shouldBe
import jetbrains.letsPlot.scale.scale_fill_hue
import org.jetbrains.kotlin.letsPlot.bars
import org.jetbrains.kotlin.letsPlot.invoke
import org.jetbrains.kotlin.letsPlot.plot
import org.junit.Test

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