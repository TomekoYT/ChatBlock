package tomeko.chatblock.config

//? if = 1.8.9 {
/*import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.core.ConfigUtils
import cc.polyfrost.oneconfig.config.data.*
import cc.polyfrost.oneconfig.config.elements.*
import tomeko.chatblock.element.*
import java.lang.reflect.Field
*///?} else {
import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.annotations.*
import tomeko.chatblock.config.annotations.StringList
//?}
import tomeko.chatblock.utils.Constants

object ChatBlockConfig : Config(
    //? if = 1.8.9 {
    /*Mod(
        Constants.MOD_NAME,
        ModType.UTIL_QOL,
        "/assets/${Constants.MOD_ID}/icon.png"
    ),
    "${Constants.MOD_ID}.json"
    *///?} else {
    "${Constants.MOD_ID}.json",
    "/assets/${Constants.MOD_ID}/icon.png",
    Constants.MOD_NAME,
    Category.UTILITY
    //?}
) {
    fun register() {
        //? if = 1.8.9 {
        /*initialize()
        *///?} else {
        preload()
        clearPropertyLabels()
        //?}
    }

    @Info(
        //? if = 1.8.9 {
        /*text = "Block Receiving Custom Messages",
        type = InfoType.INFO,
        size = 2
        *///?} else {
        title = "Block Receiving Custom Messages",
        description = ""
        //?}
    )
    private var receivingTitle: Nothing? = null

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
            title
                //?}
        = "Case-sensitive"
    )
    var blockReceivingCaseSensitive: Boolean = false

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
            title
                //?}
        = "Send message informing about a block"
    )
    var blockReceivingInfoMessage: Boolean = false

    //? if = 1.8.9 {
    /*@Info(
        text = "Block receiving following messages (supports Java/Kotlin regex):",
        type = InfoType.INFO,
        size = 2
    )
    private var receivingInfo: Nothing? = null
    *///?}

    //? if >= 1.21.11 {
    @StringList
    var messagesToBlockReceivingStringList: String = ""
    //?}

    //? if = 1.8.9 {
    /*@CustomOption(id = "blockReceiving")
    *///?}
    var messagesToBlockReceiving: Array<String> = emptyArray()
    //? if >= 1.21.11 {
    get() = messagesToBlockReceivingStringList.split("\n").filter { it.isNotBlank() }.toTypedArray()
//?}


    @Info(
        //? if = 1.8.9 {
        /*text = "Block Sending Custom Words",
        type = InfoType.INFO,
        size = 2
        *///?} else {
        title = "Block Sending Custom Words",
        description = ""
        //?}
    )
    private var sendingTitle: Nothing? = null

    @Slider(
        //? if = 1.8.9 {
        /*name
            *///?} else {
            title
                //?}
        = "Similarity",
        min = 1f,
        max = 100f,
        step =
            //? if = 1.8.9 {
            /*1
        *///?} else {
        1f
    //?}
    )
    var blockSendingSimilarity: Int = 100

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
            title
                //?}
        = "Send message informing about a block"
    )
    var blockSendingInfoMessage: Boolean = true

    //? if = 1.8.9 {
    /*@Info(
        text = "Block sending following words:",
        type = InfoType.INFO,
        size = 2
    )
    private var sendingInfo: Nothing? = null
    *///?}

    //? if >= 1.21.11 {
    @StringList
    var wordsToBlockSendingStringList: String = ""
    //?}

    //? if = 1.8.9 {
    /*@CustomOption(id = "blockSending")
    *///?}
    var wordsToBlockSending: Array<String> = emptyArray()
    //? if >= 1.21.11 {
    get() = wordsToBlockSendingStringList.split(" ").filter { it.isNotBlank() }.toTypedArray()
    //?}

    @Switch(
        //? if = 1.8.9 {
        /*name
            *///?} else {
            title
                //?}
        = "Debug Mode"
    )
    var debugModeEnabled = false

    //? if = 1.8.9 {
    /*override fun getCustomOption(
        field: Field,
        annotation: CustomOption,
        page: OptionPage,
        mod: Mod,
        migrate: Boolean
    ): BasicOption {
        when (annotation.id) {
            "blockReceiving" -> {
                val option = BlockReceivingListOption
                ConfigUtils.getSubCategory(page, "General", "").options.add(option)
                return option
            }

            else -> {
                val option = BlockSendingListOption
                ConfigUtils.getSubCategory(page, "General", "").options.add(option)
                return option
            }
        }
    }

    override fun load() {
        super.load()

        BlockReceivingListOption.apply {
            items.clear()
            items.addAll(messagesToBlockReceiving.map { message ->
                WrappedBlock(
                    message = message,
                    onRemove = { willBeRemoved = it },
                    splitOnSpace = false
                )
            })
        }

        BlockSendingListOption.apply {
            items.clear()
            items.addAll(wordsToBlockSending.map { message ->
                WrappedBlock(
                    message = message,
                    onRemove = { willBeRemoved = it },
                    splitOnSpace = true
                )
            })
        }
    }

    override fun save() {
        messagesToBlockReceiving = BlockReceivingListOption.items
            .map { it.message }
            .toTypedArray()

        wordsToBlockSending = BlockSendingListOption.items
            .flatMap { it.message.split(Regex("\\s+")) }
            .filter { it.isNotBlank() }
            .toTypedArray()

        super.save()
    }
    *///?} else {
    private fun clearPropertyLabels() {
        try {
            var clazz: Class<*>? = this.javaClass
            while (clazz != null) {
                for (field in clazz.declaredFields) {
                    runCatching {
                        field.isAccessible = true
                        val value = field.get(this)
                        if (value != null) scanAndClean(value)
                    }
                }
                clazz = clazz.superclass
            }
        } catch (_: Throwable) {
        }
    }

    private fun scanAndClean(obj: Any) {
        val objClass = obj.javaClass
        val name = objClass.name

        if (obj is Map<*, *>) {
            obj.values.forEach { if (it != null) scanAndClean(it) }
            return
        }
        if (obj is Collection<*>) {
            obj.forEach { if (it != null) scanAndClean(it) }
            return
        }

        if (name.contains("polyfrost")) {
            runCatching {
                var c: Class<*>? = objClass
                var isTarget = false
                while (c != null) {
                    try {
                        val idField = c.getDeclaredField("id")
                        idField.isAccessible = true
                        val id = idField.get(obj)?.toString()
                        if (id == "messagesToBlockReceivingStringList" || id == "wordsToBlockSendingStringList") {
                            isTarget = true
                            break
                        }
                    } catch (_: Exception) {
                    }
                    c = c.superclass
                }

                if (isTarget) {
                    var currentClass: Class<*>? = objClass
                    while (currentClass != null) {
                        for (fieldName in listOf("title", "name", "label", "description")) {
                            try {
                                val f = currentClass.getDeclaredField(fieldName)
                                f.isAccessible = true
                                f.set(obj, "")
                            } catch (_: Exception) {
                            }
                        }
                        currentClass = currentClass.superclass
                    }
                }

                var scanClass: Class<*>? = objClass
                while (scanClass != null) {
                    for (field in scanClass.declaredFields) {
                        if (java.lang.reflect.Modifier.isStatic(field.modifiers)) continue
                        try {
                            field.isAccessible = true
                            val fieldVal = field.get(obj)
                            if (fieldVal != null && fieldVal !== obj && !field.type.isPrimitive) {
                                scanAndClean(fieldVal)
                            }
                        } catch (_: Exception) {
                        }
                    }
                    scanClass = scanClass.superclass
                }
            }
        }
    }
    //?}
}