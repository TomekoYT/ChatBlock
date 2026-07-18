package tomeko.chatblock.config

//? if = 1.8.9 {
/*import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.gui.elements.IFocusable
import cc.polyfrost.oneconfig.gui.elements.text.TextInputField
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import tomeko.chatblock.utils.Constants

class TextField(
    private val getMessage: () -> String,
    private val setMessage: (String) -> Unit,
    private val onKeyTyped: ((Char, Int) -> Boolean)? = null
) : TextInputField(608, 32, "", false, false, null) {

    override fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        input = getMessage()
        super.draw(vg, x, y, inputHandler)
    }

    fun isKeyTyped(key: Char, keyCode: Int): Boolean {
        if (!isToggled) return false

        if (onKeyTyped?.invoke(key, keyCode) == true) {
            return true
        }

        keyTyped(key, keyCode)
        setMessage(input)
        return true
    }

    fun focus() {
        toggled = true
        caretPos = input.length
        prevCaret = caretPos
        start = 0f
        end = 0f
        selectedText = null
    }

    fun unfocus() {
        toggled = false
        start = 0f
        end = 0f
        selectedText = null
    }
}

class WrappedBlock(
    var message: String,
    private val onRemove: (WrappedBlock) -> Unit,
    private val splitOnSpace: Boolean = false,
    private val owner: BlockListOption? = null
) {
    private val removeButton = BasicButton(
        32, 32,
        Constants.MINUS_ICON,
        BasicButton.ALIGNMENT_CENTER,
        ColorPalette.PRIMARY_DESTRUCTIVE
    )

    private val textField = TextField(
        getMessage = { message },
        setMessage = { message = it },
        onKeyTyped = { key, _ ->
            if (!splitOnSpace || key != ' ') {
                return@TextField false
            }

            if (message.isBlank()) {
                return@TextField true
            }

            unfocus()

            val next = WrappedBlock(
                "",
                onRemove,
                splitOnSpace = true,
                owner = owner
            )

            owner?.insertAfter(this, next)
            next.focus()

            return@TextField true
        }
    )

    init {
        removeButton.setClickAction {
            onRemove(this)
        }
    }

    fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        removeButton.draw(vg, x, y, inputHandler)
        textField.draw(vg, x + 40, y, inputHandler)
    }

    fun keyTyped(key: Char, keyCode: Int) =
        textField.isKeyTyped(key, keyCode)

    fun hasFocus() =
        textField.isToggled

    fun focus() {
        textField.focus()
    }

    fun unfocus() {
        textField.unfocus()
    }
}

abstract class AbstractListOption<T>(
    optionName: String = "",
    optionDescription: String = "",
    optionCategory: String = "General",
    optionSubCategory: String = ""
) :
    BasicOption(null, null, optionName, optionDescription, optionCategory, optionSubCategory, 2), IFocusable {

    protected val addButton = BasicButton(
        32, 32,
        Constants.PLUS_ICON,
        BasicButton.ALIGNMENT_CENTER,
        ColorPalette.PRIMARY
    )

    val items: MutableList<T> = ArrayList()
    var willBeRemoved: T? = null

    init {
        addButton.setClickAction {
            items.add(createWrapped())
        }
    }

    protected abstract fun createWrapped(): T

    override fun getHeight() = items.size * 48 + 32

    override fun draw(vg: Long, x: Int, y: Int, inputHandler: InputHandler) {
        var nextY = y

        for (item in items) {
            drawItem(item, vg, x.toFloat(), nextY.toFloat(), inputHandler)
            nextY += 48
        }

        addButton.draw(vg, x.toFloat(), nextY.toFloat(), inputHandler)

        checkWillBeRemoved()
    }

    private fun checkWillBeRemoved() {
        val item = willBeRemoved ?: return
        items.remove(item)
        willBeRemoved = null
    }

    override fun keyTyped(key: Char, keyCode: Int) {
        items.any { keyTypedItem(it, key, keyCode) }
    }

    override fun hasFocus() = items.any { hasFocusItem(it) }

    protected abstract fun drawItem(item: T, vg: Long, x: Float, y: Float, inputHandler: InputHandler)
    protected abstract fun keyTypedItem(item: T, key: Char, keyCode: Int): Boolean
    protected abstract fun hasFocusItem(item: T): Boolean

    fun insertAfter(current: T, newItem: T) {
        val index = items.indexOf(current)
        if (index != -1) {
            items.add(index + 1, newItem)
        } else {
            items.add(newItem)
        }
    }
}

open class BlockListOption(
    name: String,
    category: String,
    private val splitOnSpace: Boolean
) : AbstractListOption<WrappedBlock>(name, "", category) {

    override fun createWrapped() =
        WrappedBlock(
            "",
            { willBeRemoved = it },
            splitOnSpace = splitOnSpace,
            owner = this
        )

    override fun drawItem(item: WrappedBlock, vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        item.draw(vg, x, y, inputHandler)
    }

    override fun keyTypedItem(item: WrappedBlock, key: Char, keyCode: Int) =
        item.keyTyped(key, keyCode)

    override fun hasFocusItem(item: WrappedBlock) =
        item.hasFocus()
}

object BlockReceivingListOption : BlockListOption(
    "Block Receiving Custom Messages",
    ChatBlockConfig.CATEGORY_BLOCK_RECEIVING,
    splitOnSpace = false
)

object BlockSendingListOption : BlockListOption(
    "Block Sending Custom Words",
    ChatBlockConfig.CATEGORY_BLOCK_SENDING,
    splitOnSpace = true
)
*///?} else {
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.polyfrost.oneconfig.api.config.v1.Property
import org.polyfrost.oneconfig.api.config.v1.Visualizer
import org.polyfrost.oneconfig.api.config.v1.annotations.Option

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

class StringListVisualizer : Visualizer {
    val wordsToBlockSendingID = "wordsToBlockSendingStringList"

    @Composable
    override fun visualize(prop: Property<*>) {
        val delimiter = if (prop.id == wordsToBlockSendingID) " " else "\n"
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

        var editingIndex by remember { mutableStateOf<Int?>(null) }
        var editingText by remember { mutableStateOf("") }

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
            val title =
                if (prop.id == wordsToBlockSendingID) "Block sending following words:"
                else "Block receiving following messages (supports Java/Kotlin regex):"

            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            items.withIndex().forEach { (index, item) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    if (editingIndex == index) {
                        Button(
                            onClick = {
                                val trimmed = editingText.trim()
                                if (trimmed.isNotBlank()) {
                                    val newList = items.toMutableList()
                                    newList[index] = trimmed
                                    updateProperty(newList)
                                }
                                editingIndex = null
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2E7D32)),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Text("✓", color = Color.White)
                        }

                        Button(
                            onClick = { editingIndex = null },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF757575)),
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Text("X", color = Color.White)
                        }

                        OutlinedTextField(
                            value = editingText,
                            onValueChange = { editingText = it },
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.White,
                                cursorColor = Color.White,
                                focusedBorderColor = Color.LightGray,
                                unfocusedBorderColor = Color.DarkGray
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Button(
                            onClick = {
                                updateProperty(items - item)
                                if (editingIndex == index) editingIndex = null
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD32F2F)),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Text("-", color = Color.White)
                        }

                        Button(
                            onClick = {
                                editingIndex = index
                                editingText = item
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1976D2)),
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Text("✏", color = Color.White)
                        }

                        Text(
                            text = item,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                    }
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

                val placeHolderText =
                    "Type to add new " + (if (prop.id == wordsToBlockSendingID) "word" else "message") + "..."
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