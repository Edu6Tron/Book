package com.edu6tron.spiritualcompanion.nativepreview.ui

import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Test

class CalendarMonthSelectionTest {
  @Test
  fun `next month selects the first day of that next month`() {
    val selection = CalendarMonthNavigator.next(YearMonth.of(2026, 12))

    assertEquals(YearMonth.of(2027, 1), selection.month)
    assertEquals(LocalDate.of(2027, 1, 1), selection.selectedDate)
  }

  @Test
  fun `previous month selects the first day of that previous month`() {
    val selection = CalendarMonthNavigator.previous(YearMonth.of(2026, 1))

    assertEquals(YearMonth.of(2025, 12), selection.month)
    assertEquals(LocalDate.of(2025, 12, 1), selection.selectedDate)
  }

  @Test
  fun `explicit date selection keeps the selected date and displayed month aligned`() {
    val selection = CalendarMonthNavigator.select(LocalDate.of(2026, 8, 18))

    assertEquals(YearMonth.of(2026, 8), selection.month)
    assertEquals(LocalDate.of(2026, 8, 18), selection.selectedDate)
  }
}
