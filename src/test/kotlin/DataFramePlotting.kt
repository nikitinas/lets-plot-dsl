import io.kotlintest.shouldBe
import jetbrains.datalore.plot.config.Option
import org.jetbrains.kotlin.letsPlot.*
import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.DataRow
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
import org.jetbrains.kotlinx.dataframe.api.typed
import org.jetbrains.kotlinx.dataframe.dataFrameOf
import org.junit.Test

class DataFramePlotting {

    val df = dataFrameOf("name", "age")(
            "Alice", 31,
            "Bob", 45,
            "Mark", 20
    )

    // Generated code

    @DataSchema
    interface Person {
        val name: String
        val age: Int
    }

    val DataRow<Person>.name @JvmName("name_row") get() = this["name"] as String
    val DataRow<Person>.age @JvmName("age_row") get() = this["age"] as Int
    val DataFrame<Person>.name get() = this["name"] as DataColumn<String>
    val DataFrame<Person>.age get() = this["age"] as DataColumn<Int>

    val typed = df.typed<Person>()

    @Test
    fun `track column access`(){
        val res = typed.plotPoints {
            x {name}
            y {age}
            fill { "$name ($age)"}
        }
        val data = res.spec[Option.PlotBase.DATA] as Map<String, List<Any>>
        data.keys.sorted() shouldBe listOf("age", "name", "name, age")
    }

    @Test
    fun `points and lines`(){
        val res = typed.plot {
            x { name }
            points { age }
            line("red") { age * 2 }
            fill { "$name ($age)"}
        }
        val data = res.spec[Option.PlotBase.DATA] as Map<String, List<Any>>
        data.keys.sorted() shouldBe listOf("age", "age (2)", "name", "name, age")
    }
}
