package woowacourse.movie.domain

import com.example.domain.model.SeatRank
import org.junit.Assert.assertEquals
import org.junit.Test
import woowacourse.movie.model.MoneyMapper
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DiscountCalculatorTest {
    @Test
    fun `B등급 좌석 예매 시 무비데이면 10% 할인을 받는다`() {
        val moneyMapper = MoneyMapper(SeatRank.B.money)
        val date = LocalDate.of(2023, 4, 10)
        val time = LocalTime.of(12, 0)
        val dateTime = LocalDateTime.of(date, time)
        val discountCalculator = DiscountCalculator()
        val actual = discountCalculator.discount(moneyMapper, dateTime)

        val expected = MoneyMapper(9000)
        assertEquals(expected, actual)
    }

    @Test
    fun `B등급 좌석 예매 시 영화 시간대가 조조면 2000원 할인을 받는다`() {
        val moneyMapper = MoneyMapper(SeatRank.B.money)
        val date = LocalDate.of(2023, 4, 12)
        val time = LocalTime.of(10, 0)
        val dateTime = LocalDateTime.of(date, time)
        val discountCalculator = DiscountCalculator()
        val actual = discountCalculator.discount(moneyMapper, dateTime)

        val expected = MoneyMapper(8000)

        assertEquals(actual, expected)
    }

    @Test
    fun `B등급 좌석 예매 시 영화 시간대가 야간이면 2000원 할인을 받는다`() {
        val moneyMapper = MoneyMapper(SeatRank.B.money)
        val date = LocalDate.of(2023, 4, 12)
        val time = LocalTime.of(22, 0)
        val dateTime = LocalDateTime.of(date, time)
        val discountCalculator = DiscountCalculator()
        val actual = discountCalculator.discount(moneyMapper, dateTime)

        val expected = MoneyMapper(8000)
        assertEquals(actual, expected)
    }

    @Test
    fun `B등급 좌석 예매 시 무비데이이고 조조면 10%를 할인 받고 추가로 2000원 할인을 더 받는다`() {
        val moneyMapper = MoneyMapper(SeatRank.B.money)
        val date = LocalDate.of(2023, 4, 10)
        val time = LocalTime.of(10, 0)
        val dateTime = LocalDateTime.of(date, time)
        val discountCalculator = DiscountCalculator()
        val actual = discountCalculator.discount(moneyMapper, dateTime)

        val expected = MoneyMapper(7000)

        assertEquals(actual, expected)
    }
}
