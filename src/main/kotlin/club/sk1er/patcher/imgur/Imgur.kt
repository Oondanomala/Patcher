package club.sk1er.patcher.imgur

import club.sk1er.patcher.Patcher
import club.sk1er.patcher.util.chat.ChatUtilities
import com.google.gson.JsonParser
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Base64

class Imgur(private val clientId: String) {
    fun upload(file: File): String {
        val data = Base64.getEncoder().encodeToString(file.readBytes())
        val encodedParams = "image=" + URLEncoder.encode(data, "UTF-8")

        val connection = URL("https://api.imgur.com/3/image").openConnection() as HttpURLConnection
        connection.doOutput = true
        connection.doInput = true
        connection.requestMethod = "POST"
        connection.setRequestProperty("Authorization", "Client-ID $clientId")
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        connection.connect()

        connection.outputStream.bufferedWriter().use { it.write(encodedParams) }
        if (connection.responseCode != 200) {
            ChatUtilities.sendMessage("&cImgur responded with ${connection.responseCode}. Perhaps you're uploading too quickly?")
            Patcher.instance.logger.error("Failed to upload image, Imgur responded with {}", connection.responseCode)
        }

        connection.inputStream.reader().use {
            val imgurJson = JsonParser().parse(it).asJsonObject
            val dataJson = imgurJson.getAsJsonObject("data")
            return dataJson.get("link").asString
        }
    }
}
