import io.kotlintest.shouldBe
import org.jetbrains.kotlin.letsPlot.invoke
import org.jetbrains.kotlin.letsPlot.plotLine
import org.jetbrains.kotlin.letsPlot.plotY
import org.junit.Test

class simplePlotting{

    @Test
    fun `plotY`(){
        val spec1 = (0..10).plotY { x * x }

        val spec2 = (0..10).plotY { it*it }

        val spec3 = (0..10).plotLine {
            x { it }
            y { it * it }
        }

        spec1 shouldBe spec2
        spec1 shouldBe spec3
    }
}