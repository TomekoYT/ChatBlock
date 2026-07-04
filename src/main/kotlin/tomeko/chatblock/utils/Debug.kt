package tomeko.chatblock.utils

import tomeko.chatblock.config.ChatBlockConfig

//? if >= 1.21.11 {
import org.slf4j.Logger
import org.slf4j.LoggerFactory
//?}

object Debug {
    //? if >= 1.21.11 {
    private val LOGGER: Logger = LoggerFactory.getLogger(Constants.MOD_ID)
    //?}

    fun print(message: String) {
        if (!ChatBlockConfig.debugModeEnabled) return

        forcePrint(message)
    }

    fun forcePrint(message: String) {
        //? if = 1.8.9 {
        /*kotlin.io.println("[${Constants.MOD_NAME}] $message")
        *///?} else {
        LOGGER.info("[${Constants.MOD_NAME}] $message")
        //?}
    }
}