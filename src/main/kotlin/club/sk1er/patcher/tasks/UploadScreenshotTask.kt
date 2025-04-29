package club.sk1er.patcher.tasks

import club.sk1er.patcher.Patcher
import club.sk1er.patcher.imgur.Imgur
import club.sk1er.patcher.util.chat.ChatUtilities
import club.sk1er.patcher.util.screenshot.AsyncScreenshots
import gg.essential.universal.ChatColor
import me.oondanomala.assential.Multithreading
import net.minecraft.client.Minecraft
import net.minecraft.event.ClickEvent
import net.minecraft.util.ChatComponentText
import java.io.File

object UploadScreenshotTask {
    private val client = Imgur("649f2fb48e59767")

    fun execute(file: File?) {
        try {
            if (file != null) {
                ChatUtilities.sendMessage("&aUploading screenshot...")

                Multithreading.runAsync {
                    val link = client.upload(file)
                    val message = ChatComponentText("${AsyncScreenshots.prefix}${ChatColor.GREEN}Screenshot was uploaded to $link.")
                    message.chatStyle.chatClickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, link)
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(message)
                }
            } else {
                ChatUtilities.sendMessage("&cFailed to upload screenshot, maybe the file was moved/deleted?")
            }
        } catch (e: Exception) {
            ChatUtilities.sendMessage("&cFailed to upload screenshot. ${e.message}")
            Patcher.instance.logger.error("Failed to upload screenshot.", e)
        }
    }
}
