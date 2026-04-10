package co.tiagoaguiar.netflixremake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie
import co.tiagoaguiar.netflixremake.util.CategoryTask

class MainActivity : AppCompatActivity(),CategoryTask.Callback {

    lateinit var progress : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = findViewById(R.id.progress_main)

        val categories = mutableListOf<Category>()

        val adapter = CategoryAdapter(categories)
        val rv : RecyclerView = findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        CategoryTask(this).execute("https://atway.tiagoaguiar.co/fenix/netflixapp/home?apiKey=034f17ec-8079-403c-b55a-5286bfd46b53")
    }

    override fun onPreExecute() {
        progress.visibility= View.VISIBLE
    }

    override fun onResult(categories: List<Category>) {
        //Aqui será quando o categoryTask chamar de volta.
        //Callback - listener
        Log.i("Teste MainActiviti", categories.toString())
        progress.visibility= View.GONE
    }

    override fun onFailure(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        progress.visibility= View.GONE
    }
}