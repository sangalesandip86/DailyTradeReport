package com.sandip.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.sandip.enums.CurrencyType;
import com.sandip.enums.InstructionType;
import com.sandip.model.TradeInstruction;

public class TradeReportUtil {

	private static final String SHOULD_BE_POSITIVE = "Price, Unit, AgreedFx should be valid positive number";
	private static final LocalDate INSTRUCTION_DATE = LocalDate.of(2019, Month.JULY, 31);
	private static final LocalDate SETTLEMENT_DATE = LocalDate.of(2019, Month.JULY, 31);
	private static final LocalDate SETTLEMENT_DATE_SATURDAY = LocalDate.of(2019, Month.JULY, 27);
	private static final LocalDate SETTLEMENT_DATE_MONDAY = LocalDate.of(2019, Month.AUGUST, 01);

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

	/**
	 * This methods returns default test data
	 * 
	 * Note : Modify dataset as per requirement
	 * @return
	 */
	public static List<TradeInstruction> populateTradeInstructions() {
		List<TradeInstruction> tradeInstructions = new ArrayList<>();

		TradeInstruction tradeInstruction = new TradeInstruction("Doo", InstructionType.BUY, CurrencyType.INR, 1.0,
				INSTRUCTION_DATE, SETTLEMENT_DATE, 1, 100.0);
		tradeInstructions.add(tradeInstruction);
		
		tradeInstruction = new TradeInstruction("Doo", InstructionType.BUY, CurrencyType.INR, 1.0, INSTRUCTION_DATE,
				SETTLEMENT_DATE, 1, 125.6);
		tradeInstructions.add(tradeInstruction);

		tradeInstruction = new TradeInstruction("Too", InstructionType.BUY, CurrencyType.INR, 1.0, INSTRUCTION_DATE,
				SETTLEMENT_DATE_SATURDAY, 1, 150.0);
		tradeInstructions.add(tradeInstruction);

		tradeInstruction = new TradeInstruction("Roo", InstructionType.SELL, CurrencyType.INR, 1.0, INSTRUCTION_DATE,
				SETTLEMENT_DATE, 1, 190.0);
		tradeInstructions.add(tradeInstruction);

		tradeInstruction = new TradeInstruction("Foo", InstructionType.SELL, CurrencyType.INR, 1.0, INSTRUCTION_DATE,
				SETTLEMENT_DATE_MONDAY, 1, 116.0);
		tradeInstructions.add(tradeInstruction);
		return tradeInstructions;
	}

	private TradeReportUtil() {
		// Private Constructor
	}

}
