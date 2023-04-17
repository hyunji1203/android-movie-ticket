package woowacourse.movie.reservation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import woowacourse.movie.BackKeyActionBarActivity
import woowacourse.movie.KEY_MOVIE
import woowacourse.movie.KEY_RESERVATION_COUNT
import woowacourse.movie.KEY_RESERVATION_DATE
import woowacourse.movie.KEY_RESERVATION_TIME
import woowacourse.movie.KEY_RESTORE_COUNT
import woowacourse.movie.KEY_RESTORE_DATE
import woowacourse.movie.KEY_RESTORE_TIME
import woowacourse.movie.Movie
import woowacourse.movie.R
import woowacourse.movie.Toaster
import woowacourse.movie.confirm.ReservationConfirmActivity
import woowacourse.movie.databinding.ActivityMovieDetailBinding
import woowacourse.movie.domain.RunningDates
import woowacourse.movie.domain.RunningTimes
import woowacourse.movie.entity.Count
import woowacourse.movie.entity.ViewingDate
import woowacourse.movie.entity.ViewingTime
import woowacourse.movie.utils.getParcelableCompat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MovieDetailActivity : BackKeyActionBarActivity() {
    private val runningDateSetter: RunningDates by lazy { RunningDates(movie.startDate, movie.endDate) }
    private val runningTimeSetter: RunningTimes = RunningTimes()
    private lateinit var selectDate: ViewingDate
    private lateinit var selectTime: ViewingTime
    private lateinit var movie: Movie
    private val runningDates: List<LocalDate> by lazy { runningDateSetter.getRunningDates() }
    private lateinit var runningTimes: List<LocalTime>

    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreateView(savedInstanceState: Bundle?) {
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        movie = intent.getParcelableCompat(KEY_MOVIE)!!
        initSetOnClickListener()
        initMovieData()
        setDateSpinnerAdapter()
        initInstanceState()
        savedInstanceState?.let { restoreInstanceState(it) }
        setOnClickDateListener()
        setOnClickTimeListener()
    }

    private fun initMovieData() {
        binding.detailImage.setImageResource(movie.imgResourceId)
        binding.detailTitle.text = movie.title
        binding.startDate.text = movie.startDate.format(DATE_TIME_FORMATTER)
        binding.endDate.text = movie.endDate.format(DATE_TIME_FORMATTER)
        binding.detailTime.text = movie.runningTime.value.toString()
        binding.description.text = movie.description
    }

    private fun setDateSpinnerAdapter() {
        val dateSpinnerAdapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            runningDates.map { it.toString() }
        )
        binding.dateSpinner.adapter = dateSpinnerAdapter
    }

    private fun initSetOnClickListener() {
        binding.minus.setOnClickListener {
            var previous = binding.count.text.toString().toInt()
            previous--
            if (previous <= 0) {
                Toaster.showToast(this, getString(R.string.error_reservation_min_count))
                return@setOnClickListener
            }
            binding.count.text = previous.toString()
        }

        binding.plus.setOnClickListener {
            var previous = binding.count.text.toString().toInt()
            previous++
            binding.count.text = previous.toString()
        }

        binding.reservationConfirm.setOnClickListener {
            val intent = Intent(this, ReservationConfirmActivity::class.java)
            intent.putExtra(KEY_MOVIE, movie)
            intent.putExtra(KEY_RESERVATION_COUNT, Count(binding.count.text.toString().toInt()))
            intent.putExtra(KEY_RESERVATION_DATE, selectDate)
            intent.putExtra(KEY_RESERVATION_TIME, selectTime)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_RESTORE_DATE, selectDate)
        outState.putParcelable(KEY_RESTORE_TIME, selectTime)
        outState.putInt(KEY_RESTORE_COUNT, binding.count.text.toString().toInt())
    }

    fun setTimeSpinnerAdapter() {
        runningTimes = runningTimeSetter.getRunningTimes(selectDate.value)
        val timeSpinnerAdapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            runningTimes.map { it.toString() }
        )
        binding.timeSpinner.adapter = timeSpinnerAdapter
    }

    private fun initInstanceState() {
        selectDate = ViewingDate(movie.startDate)
        setTimeSpinnerAdapter()
        runningTimes = runningTimeSetter.getRunningTimes(selectDate.value)
        selectTime = ViewingTime(runningTimes[0])
    }

    private fun restoreInstanceState(savedInstanceState: Bundle) {
        selectDate = savedInstanceState.getParcelableCompat(KEY_RESTORE_DATE)!!
        setTimeSpinnerAdapter()
        selectTime = savedInstanceState.getParcelableCompat(KEY_RESTORE_TIME)!!
        runningTimes = RunningTimes().getRunningTimes(selectDate.value)
        binding.dateSpinner.setSelection(runningDates.indexOf(selectDate.value), false)
        binding.timeSpinner.setSelection(runningTimes.indexOf(selectTime.value), false)
        binding.count.text = savedInstanceState.getInt(KEY_RESTORE_COUNT).toString()
    }

    private fun setOnClickDateListener() {
        binding.dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectDate = ViewingDate(runningDates[position])
                setTimeSpinnerAdapter()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setOnClickTimeListener() {
        binding.timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectTime = ViewingTime(runningTimes[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    companion object {
        private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy.M.d")
    }
}
