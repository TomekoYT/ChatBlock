package tomeko.chatblock.config

//? if = 1.8.9 {
import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.core.ConfigUtils
import cc.polyfrost.oneconfig.config.data.*
import cc.polyfrost.oneconfig.config.elements.*
import tomeko.chatblock.element.*
import java.lang.reflect.Field
//?} else {
/*import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.annotations.*
*///?}
import tomeko.chatblock.utils.Constants

object ChatBlockConfig : Config(
    //? if = 1.8.9 {
    Mod(Constants.MOD_NAME,
    ModType.UTIL_QOL,
    "/assets/${Constants.MOD_ID}/icon.png"),
    "${Constants.MOD_ID}.json"
    //?} else {
    /*"${Constants.MOD_ID}.json",
    "/assets/${Constants.MOD_ID}/icon.png",
    Constants.MOD_NAME,
    Category.QOL
    *///?}
) {
    fun register() {
        //? if = 1.8.9 {
        initialize()
        //?} else {
        /*save()
        *///?}
    }

    @Info(
        //? if = 1.8.9 {
        text = "Block Receiving Custom Messages",
        type = InfoType.INFO,
        size = 2
        //?} else {
        /*description = "Block Receiving Custom Messages",
        *///?}
    )
    private var receivingTitle = null

    @Switch(
        //? if = 1.8.9 {
        name
        //?} else {
        /*title
            *///?}
        = "Case-sensitive"
    )
    var blockReceivingCaseSensitive: Boolean = false

    @Switch(
        //? if = 1.8.9 {
        name
        //?} else {
        /*title
            *///?}
        = "Ignore formatting"
    )
    var blockReceivingIgnoreFormatting: Boolean = true

    @Switch(
        //? if = 1.8.9 {
        name
        //?} else {
        /*title
            *///?}
        = "Send message informing about a block"
    )
    var blockReceivingInfoMessage: Boolean = false

    @Info(
        //? if = 1.8.9 {
        text = "Block receiving following messages:",
        type = InfoType.INFO,
        size = 2
        //?} else {
        /*description = "Block receiving following messages:",
        *///?}
    )
    private var receivingInfo = null

    //? if = 1.8.9 {
    @CustomOption(id = "blockReceiving")
    //?}
    var messagesToBlockReceiving: Array<String> = emptyArray()


    @Info(
        //? if = 1.8.9 {
        text = "Block Sending Custom Words",
        type = InfoType.INFO,
        size = 2
        //?} else {
        /*description = "Block Sending Custom Words",
        *///?}
    )
    private var sendingTitle = null

    @Slider(
        //? if = 1.8.9 {
        name
        //?} else {
        /*title
            *///?}
        = "Similarity",
        min = 1f,
        max = 100f,
        step =
            //? if = 1.8.9 {
            1
            //?} else {
            /*1f
            *///?}
    )
    var blockSendingSimilarity: Int = 100

    @Switch(
        //? if = 1.8.9 {
        name
        //?} else {
        /*title
            *///?}
        = "Send message informing about a block"
    )
    var blockSendingInfoMessage: Boolean = true

    @Info(
        //? if = 1.8.9 {
        text = "Block sending following words:",
        type = InfoType.INFO,
        size = 2
        //?} else {
        /*description = "Block sending following words:",
        *///?}
    )
    private var sendingInfo = null

    //? if = 1.8.9 {
    @CustomOption(id = "blockSending")
    //?}
    var wordsToBlockSending: Array<String> = emptyArray()

    //? if = 1.8.9 {
    override fun getCustomOption(
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
                WrappedBlock(message) {
                    willBeRemoved = it
                }
            })
        }

        BlockSendingListOption.apply {
            items.clear()
            items.addAll(wordsToBlockSending.map { message ->
                WrappedBlock(message) {
                    willBeRemoved = it
                }
            })
        }
    }

    override fun save() {
        messagesToBlockReceiving = BlockReceivingListOption.items
            .map { it.message }
            .toTypedArray()

        wordsToBlockSending = BlockSendingListOption.items
            .map { it.message }
            .toTypedArray()

        super.save()
    }
    //?}
}