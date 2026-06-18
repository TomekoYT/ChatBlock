package tomeko.chatblock.config.visualizer

//? if >= 26.1 {
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.polyfrost.oneconfig.api.config.v1.Property
import org.polyfrost.oneconfig.api.config.v1.Visualizer
import tomeko.chatblock.config.ChatBlockConfig

class StringListVisualizer : Visualizer {
    @Composable
    override fun visualize(prop: Property<*>) {
        val delimiter = if (prop.id == "wordsToBlockSendingStringList") " " else "\n"
        val rawValue = prop.get()

        val initialList = remember(rawValue) {
            when (rawValue) {
                is String -> rawValue.split(delimiter).filter { it.isNotBlank() }
                is Array<*> -> rawValue.mapNotNull { it?.toString() }
                is List<*> -> rawValue.mapNotNull { it?.toString() }
                else -> emptyList()
            }
        }

        var items by remember { mutableStateOf(initialList) }
        var newItemText by remember { mutableStateOf("") }

        LaunchedEffect(initialList) {
            items = initialList
        }

        fun updateProperty(newItems: List<String>) {
            items = newItems
            val joinedString = newItems.joinToString(delimiter)

            @Suppress("UNCHECKED_CAST")
            (prop as Property<Any>).set(joinedString)

            ChatBlockConfig.save()
        }

        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            for (item in items) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Button(
                        onClick = { updateProperty(items - item) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD32F2F)),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text("-", color = Color.White)
                    }
                    Text(text = item, color = Color.White)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Button(
                    onClick = {
                        if (newItemText.isNotBlank() && !items.contains(newItemText)) {
                            updateProperty(items + newItemText.trim())
                            newItemText = ""
                        }
                    },
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text("+", color = Color.White)
                }

                val placeHolderText = "Type to add new " + (if (prop.id == "wordsToBlockSendingStringList") "word" else "message") + "..."
                OutlinedTextField(
                    value = newItemText,
                    onValueChange = { newItemText = it },
                    placeholder = { Text(placeHolderText, color = Color.Gray) },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.DarkGray
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
//?}