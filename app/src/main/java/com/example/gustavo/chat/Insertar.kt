package com.example.gustavo.chat

import org.json.JSONException
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.util.HashMap

class Insertar {

    private var conn: HttpURLConnection? = null

    @Throws(JSONException::class)
    fun sendRequest(link: String, values: HashMap<String, String>?): Boolean {

        var res = false
        try {
            val url = URL(link)
            conn = url.openConnection() as HttpURLConnection
            conn!!.readTimeout = CONNECTION_TIMEOUT
            conn!!.connectTimeout = CONNECTION_TIMEOUT
            conn!!.requestMethod = "POST"
            conn!!.doInput = true
            conn!!.doOutput = true
            conn!!.connect()
            if (values != null) {
                val os = conn!!.outputStream
                val osWriter = OutputStreamWriter(os,
                        "UTF-8")
                val writer = BufferedWriter(osWriter)
                writer.write(getPostData(values))
                writer.flush()
                writer.close()
                os.close()
            }
            if (conn!!.responseCode == HttpURLConnection.HTTP_OK) {
                res = true
            }
        } catch (e: MalformedURLException) {
        } catch (e: IOException) {
        }

        return res
    }

    fun getPostData(values: HashMap<String, String>): String {
        val builder = StringBuilder()
        var first = true
        for ((key, value) in values) {
            if (first)
                first = false
            else
                builder.append("&")
            try {
                builder.append(URLEncoder.encode(key, "UTF-8"))
                builder.append("=")
                builder.append(URLEncoder.encode(value, "UTF-8"))
            } catch (e: UnsupportedEncodingException) {
            }

        }
        return builder.toString()
    }

    companion object {

        val CONNECTION_TIMEOUT = 15 * 1000
    }
}