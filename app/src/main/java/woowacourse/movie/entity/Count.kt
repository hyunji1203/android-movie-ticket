package woowacourse.movie.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@JvmInline
@Parcelize
value class Count(val value: Int) : Parcelable {
    init {
        require(value >= MIN_COUNT_RANGE) { COUNT_ERROR_MESSAGE }
    }

    companion object {
        private const val COUNT_ERROR_MESSAGE = "[ERROR] 예약 인원 수는 1명 이상이여야 합니다."
        private const val MIN_COUNT_RANGE = 1
    }
}
