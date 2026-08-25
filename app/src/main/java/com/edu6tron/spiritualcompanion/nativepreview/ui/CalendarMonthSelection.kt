package com.edu6tron.spiritualcompanion.nativepreview.ui

import java.time.LocalDate
import java.time.YearMonth

/** Keeps the visible calendar month and its selected date in the same month. */
data class CalendarMonthSelection(
  val month: YearMonth,
  val selectedDate: LocalDate,
) {
  init {
    require(YearMonth.from(selectedDate) == month) { "Selected date must be inside the displayed month." }
  }
}

object CalendarMonthNavigator {
  fun previous(displayedMonth: YearMonth): CalendarMonthSelection = forMonth(displayedMonth.minusMonths(1))

  fun next(displayedMonth: YearMonth): CalendarMonthSelection = forMonth(displayedMonth.plusMonths(1))

  fun select(date: LocalDate): CalendarMonthSelection = CalendarMonthSelection(
    month = YearMonth.from(date),
    selectedDate = date,
  )

  private fun forMonth(month: YearMonth): CalendarMonthSelection = CalendarMonthSelection(
    month = month,
    selectedDate = month.atDay(1),
  )
}
