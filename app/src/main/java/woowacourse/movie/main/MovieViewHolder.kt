package woowacourse.movie.main

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import woowacourse.movie.R
import woowacourse.movie.model.MovieAndAd
import java.time.format.DateTimeFormatter

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val image: ImageView = itemView.findViewById(R.id.image)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val startDate: TextView = itemView.findViewById(R.id.start_date)
    private val endDate: TextView = itemView.findViewById(R.id.end_date)
    val time: TextView = itemView.findViewById(R.id.time)
    val reservation: Button = itemView.findViewById(R.id.reservation)

    fun bind(movie: MovieAndAd.Movie) {
        image.setImageResource(movie.imgResourceId)
        title.text = movie.title
        startDate.text = movie.startDate.format(DATE_FORMATTER)
        endDate.text = movie.endDate.format(DATE_FORMATTER)
        time.text = movie.runningTime.value.toString()
    }

    companion object {
        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.M.d")
    }
}
