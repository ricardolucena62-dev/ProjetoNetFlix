package co.tiagoaguiar.netflixremake.util

import android.util.Log
import java.io.IOException
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class CategoryTask {
     fun execute(url:String){

         //Ultilizando a UI-thread(1)
         val executor = Executors.newSingleThreadExecutor()

         //Ultilizando a nova UI-Thread em (processo paralelo) (2)
         executor.execute{
             try {
                 val requestURL = URL(url)
                 val urlConection = requestURL.openConnection() as HttpsURLConnection // abrir a conexão.
                 urlConection.readTimeout = 2000 // tempo de leitura
                 urlConection.connectTimeout = 2000 // tempo de conexão
                 val statusCode: Int = urlConection.responseCode
                 if (statusCode > 400){
                    throw IOException("Erro na comunicação com o servidor.")
                 }
                 val stream = urlConection.inputStream // sequencia de bytes
                 val jsonAsString = stream.bufferedReader().use { it.readText() } // bytes -> String
                 Log.i("Teste", jsonAsString)

            }catch(e:IOException){
                Log.e("Teste", e.message ?: "Erro desconhecido.",e)
            }

         }

     }
}