import io.kotlintest.shouldBe
import jetbrains.datalore.plot.config.Option
import org.jetbrains.dataframe.*
import org.jetbrains.dataframe.api.columns.*
import org.jetbrains.kotlin.letsPlot.*
import org.junit.Test

class dataFramePlotting(){

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

    val DataRow<Person>.name @JvmName("name_row") get() = this["name"] as String
    val DataRow<Person>.age @JvmName("age_row") get() = this["age"] as Int
    val DataFrame<Person>.name get() = this["name"] as ColumnData<String>
    val DataFrame<Person>.age get() = this["age"] as ColumnData<Int>

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
}