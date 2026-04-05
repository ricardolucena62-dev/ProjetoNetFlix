package co.tiagoaguiar.netflixremake

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.model.Movie

class MovieAdapter(private val movies : List<Movie>, @LayoutRes private val layoutId: Int ) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        // Espera que a gente infle um layout pra ele e informe o viewHolder.
        val view =LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size

    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)

    }

    //montando a estrutura pra um imagem de filme.
     inner class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bind (movie:Movie){
                val imageCover: ImageView =itemView.findViewById(R.id.img_cover)

                // aqui vai ser trocado por uma URL.
                //imageCover.setImageResource(movie.coverUrl)
         }
     }



}