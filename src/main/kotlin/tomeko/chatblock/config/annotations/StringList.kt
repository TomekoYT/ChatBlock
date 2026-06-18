package tomeko.chatblock.config.annotations

//? if >= 26.1 {
import org.polyfrost.oneconfig.api.config.v1.annotations.Option
import tomeko.chatblock.config.visualizer.StringListVisualizer

@Option(display = StringListVisualizer::class)
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class StringList(
    val title: String = "",
    val description: String = "",
    val category: String = "General",
    val subcategory: String = "General"
)
//?}