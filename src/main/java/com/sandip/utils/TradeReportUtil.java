package com.sandip.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.function.Predicate;

import com.sandip.enums.CurrencyType;

public class TradeReportUtil {

	private static final String SHOULD_BE_POSITIVE = "Price, Unit, AgreedFx should be valid positive number";

	private static final Predicate<LocalDate> SATURDAY_SUNDAY = date -> date.getDayOfWeek().equals(DayOfWeek.SATURDAY)
			|| date.getDayOfWeek().equals(DayOfWeek.SUNDAY);

	private static final Predicate<LocalDate> FRIDAY_SATURDAY = date -> date.getDayOfWeek().equals(DayOfWeek.FRIDAY)
			|| date.getDayOfWeek().equals(DayOfWeek.SATURDAY);

	/**
	 * Calculate next working day, if given date falls on weekends Also check
	 * Weekends based on currency type For AED, SGP : Friday, Saturday All others :
	 * Saturday, Sunday
	 * 
	 * @param settlementDate
	 * @param currencyType
	 * @return
	 */
	public static LocalDate workingDayOfSettlementDate(LocalDate settlementDate, CurrencyType currencyType) {

		LocalDate nextWorkingDay = settlementDate;
		if (CurrencyType.AED.equals(currencyType) || CurrencyType.SGP.equals(currencyType)) {
			if (FRIDAY_SATURDAY.test(settlementDate)) {
				nextWorkingDay = settlementDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
			}
		} else {
			if (SATURDAY_SUNDAY.test(settlementDate)) {
				nextWorkingDay = settlementDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
			}
		}
		return nextWorkingDay;
	}

	public static void shouldBeNonNegative(Double... numbers) {
		for (Double number : numbers) {
			if (number < 0 || number == 0.0) {
				throw new IllegalArgumentException(SHOULD_BE_POSITIVE);
			}
		}
	}

	private TradeReportUtil() {
		// Private Constructor
	}

}
