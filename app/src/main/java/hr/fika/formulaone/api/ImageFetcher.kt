package hr.fika.formulaone.api

import android.content.Context
import hr.fika.formulaone.factory.createGetHttpUrlConnection
import java.io.File
import java.net.HttpURLConnection


const val WIKIPEDIA_BASE_URL =
    "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=xml&pithumbsize=500&titles="

class ImageFetcher {


    companion object {
        fun getImageUrl(context: Context, url: String): String {

            val urlTitle = url.substring(url.lastIndexOf('/') + 1)

            val con: HttpURLConnection =
                createGetHttpUrlConnection(WIKIPEDIA_BASE_URL.plus(urlTitle))

            con.inputStream.use {
                val xmlFile = createFile(context, "temp", "xml")
                xmlFile.writeBytes(it.readBytes())
                val text = xmlFile.readText()
                return try {
                    text.substring(text.lastIndexOf("https"), text.lastIndexOf("width") - 2)
                } catch (exception: StringIndexOutOfBoundsException) {
                    ""
                }
            }


        }

        fun createFile(context: Context, filename: String, ext: String): File {
            val dir = context.applicationContext.getExternalFilesDir(null)
            val file = File(dir, File.separator + filename + ext)
            if (file.exists()) {
                file.delete()
            }
            return file
        }

    }


}