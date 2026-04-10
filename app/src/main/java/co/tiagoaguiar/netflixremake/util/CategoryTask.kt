package co.tiagoaguiar.netflixremake.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class CategoryTask(private val callback : Callback) {

    private val handler = Handler(Looper.getMainLooper())
    interface Callback{
    fun onPreExecute()
    fun onResult(categories:List<Category>)
    fun onFailure(message : String)

    }
     fun execute(url:String){
         callback.onPreExecute()
         //Ultilizando a UI-thread(1)
         val executor = Executors.newSingleThreadExecutor()

         //Ultilizando a nova UI-Thread em (processo paralelo) (2)
         executor.execute{
             var urlConection: HttpsURLConnection? = null
             var buffer : BufferedInputStream? = null
             var stream : InputStream? = null
             try {
                 val requestURL = URL(url)
                 urlConection = requestURL.openConnection() as HttpsURLConnection // abrir a conexão.
                 urlConection.readTimeout = 2000 // tempo de leitura
                 urlConection.connectTimeout = 2000 // tempo de conexão
                 val statusCode: Int = urlConection.responseCode
                 if (statusCode > 400){
                    throw IOException("Erro na comunicação com o servidor.")
                 }
                 stream = urlConection.inputStream // sequencia de bytes
                 buffer = BufferedInputStream(stream) // Recebe o arquivo json
                 val jsonAsString = toString(buffer)
                 val categories = toCategories(jsonAsString)
                 handler.post {
                     callback.onResult(categories) // Aqui roda dentro a UI- thread
                 }


            }catch(e:IOException){
                val message = e.message ?: "Erro desconhecido."
                Log.e("Teste", e.message, e)
                 handler.post {
                     callback.onFailure(message)
                 }
            } finally {
                urlConection?.disconnect()
                 stream?.close()
                 buffer?.close()
             }
         }
     }
    private fun toCategories(jsonAsString: String): List<Category>{
        val categories = mutableListOf<Category>()
        val jsonRoot = JSONObject(jsonAsString)
        val jsonCategories= jsonRoot.getJSONArray("Category")
        for(i in 0 until jsonCategories.length()){
            val jsonCAtegory = jsonCategories.getJSONObject(i)
            val title = jsonCAtegory.getString("title")
            val jsonMovies = jsonCAtegory.getJSONArray("movie")
            val movies = mutableListOf<Movie>()
            for(j in  0 until jsonMovies.length()){
                val jsonMovie = jsonMovies.getJSONObject(j)
                val id = jsonMovie.getInt("id")
                val coverUrl = jsonMovie.getString("cover_url")
                movies.add(Movie(id,coverUrl))
            }
         categories.add(Category(title,movies))
        }
        return categories
    }

    private fun toString(stream : InputStream) : String {
        val bytes = ByteArray(1024)
        val baos =ByteArrayOutputStream()
        var read : Int
        while (true){
            read = stream.read(bytes)
            if(read <= 0){
                break
            }
            baos.write(bytes , 0 , read) // Escreva os bytes novos de zero ate onde read conseguir ler.
        }
        return String(baos.toByteArray())
    }

}