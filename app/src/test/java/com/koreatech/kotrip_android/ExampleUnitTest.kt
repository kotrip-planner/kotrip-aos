package com.koreatech.kotrip_android

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.Period

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        val startDate = LocalDate.of(2024,2,1)
        val endDate = LocalDate.of(2024, 1, 29)


        val period = Math.abs(Period.between(startDate, endDate).days)

        assertEquals(3, period)


    }

    @Test
    fun `시간범위확인`() {
        val startDate = LocalDate.of(2024, 2, 27)
        val endDate = LocalDate.of(2024, 2, 28)

        assertEquals(startDate.isAfter(endDate), false)
//        var currentDate = startDate
//        while (!currentDate.isAfter(endDate)) {
//            println(currentDate)
//            currentDate = currentDate.plusDays(1)
//        }
    }
}