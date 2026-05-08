package co.tiagoaguiar.netflixremake.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import co.tiagoaguiar.netflixremake.model.Category
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class DownloadImageTask(private val callback: Callback) {

    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

    interface  Callback{
        fun onResult(bitmap : Bitmap)
    }

    fun execute(url: String){
        executor.execute{
            var urlConection: HttpsURLConnection? = null
            var stream : InputStream? = null
            try {

                val requestURL = URL(url)
                urlConection = requestURL.openConnection() as HttpsURLConnection
                urlConection.readTimeout = 2000
                urlConection.connectTimeout = 2000

                val statusCode: Int = urlConection.responseCode
                if (statusCode > 400){
                    throw IOException("Erro na comunicação com o servidor.")
                }
                stream = urlConection.inputStream
                val bitmap = BitmapFactory.decodeStream(stream)

                handler.post{
                    callback.onResult(bitmap)
                }
            }catch (e : IOException){
                val message = e.message ?: "Erro desconhecido."
                Log.e("Teste", e.message, e)
            }finally {
                urlConection?.disconnect()
                stream?.close()
            }
        }
    }

}