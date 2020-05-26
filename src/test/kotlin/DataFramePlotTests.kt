import io.kotlintest.shouldBe
import jetbrains.datalore.plot.config.Option
import krangl.IntCol
import krangl.StringCol
import krangl.dataFrameOf
import krangl.typed.DataFrameType
import krangl.typed.TypedDataFrame
import krangl.typed.TypedDataFrameRow
import krangl.typed.typed
import org.jetbrains.kotlin.letsPlot.invoke
import org.jetbrains.kotlin.letsPlot.plot
import org.jetbrains.kotlin.letsPlot.plotBars
import org.jetbrains.kotlin.letsPlot.plotPoints
import org.junit.Test

class DataFramePlotTests(){

    val df = dataFrameOf("name", "age")(
            "Alice", 31,
            "Bob", 45,
            "Mark", 20
    )

    // Generated code

    @DataFrameType
    interface Person {
        val name: String
        val age: Int
    }

    val TypedDataFrameRow<Person>.name get() = this["name"] as String
    val TypedDataFrameRow<Person>.age get() = this["age"] as Int
    val TypedDataFrame<Person>.name get() = this["name"] as StringCol
    val TypedDataFrame<Person>.age get() = this["age"] as IntCol

    val typed = df.typed<Person>()

    @Test
    fun `track column access`(){
        val res = typed.plotPoints {
            x {name}
            y {age}
            fill { "$name ($age)"}
        }
        val data = res.spec[Option.Plot.DATA] as Map<String, List<Any>>
        data.keys.sorted() shouldBe listOf("age", "name", "name, age")
    }
}