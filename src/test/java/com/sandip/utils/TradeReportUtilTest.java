package com.sandip.utils;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Month;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sandip.enums.CurrencyType;

public class TradeReportUtilTest {

	private static final LocalDate SETTLEMENT_DATE_SATURDAY = LocalDate.of(2019, Month.JULY, 27);
	private static final LocalDate SETTLEMENT_DATE_SUNDAY = LocalDate.of(2019, Month.JULY, 28);
	private static final LocalDate SETTLEMENT_DATE_FRIDAY = LocalDate.of(2019, Month.JULY, 26);
	private static final LocalDate SETTLEMENT_DATE_MONDAY = LocalDate.of(2019, Month.JULY, 29);

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testSettlementDateChangeIfNotValidWeekDay() {
		// Given
		LocalDate settlementDate = SETTLEMENT_DATE_SATURDAY;
		// When
		LocalDate actualSettlementDate = TradeReportUtil.workingDayOfSettlementDate(settlementDate, CurrencyType.INR);
		// Then
		assertEquals(LocalDate.of(2019, 07, 29), actualSettlementDate);
	}

	@Test
	public void testSettlementDateDontChangeIfValidWeekDay() {
		// Given
		LocalDate settlementDate = SETTLEMENT_DATE_MONDAY;
		// When
		LocalDate actualSettlementDate = TradeReportUtil.workingDayOfSettlementDate(settlementDate, CurrencyType.INR);
		// Then
		assertEquals(LocalDate.of(2019, 07, 29), actualSettlementDate);
	}

	@Test
	public void testSettlementDateChangeIfNotValidWeekDayCurrencyAED() {
		// Given
		LocalDate settlementDate = SETTLEMENT_DATE_FRIDAY;
		// When
		LocalDate actualSettlementDate = TradeReportUtil.workingDayOfSettlementDate(settlementDate, CurrencyType.AED);
		// Then
		assertEquals(LocalDate.of(2019, 07, 28), actualSettlementDate);
	}

	@Test
	public void testSettlementDateSundayIsValidWeekDayWhenCurrencyAED() {
		// Given
		LocalDate settlementDate = SETTLEMENT_DATE_SUNDAY;
		// When
		LocalDate actualSettlementDate = TradeReportUtil.workingDayOfSettlementDate(settlementDate, CurrencyType.AED);
		// Then
		assertEquals(LocalDate.of(2019, 07, 28), actualSettlementDate);
	}

	@Test
	public void testshouldBeNonNegativeAllPositive() {
		// Given
		Double price = 100.0;
		Double units = 1.0;
		Double agreedFX = 1.2;
		// When
		TradeReportUtil.shouldBeNonNegative(price, units, agreedFX);
	}

	@Test
	public void testshouldBeNonNegativeForNegative() {
		// Given
		Double price = 100.0;
		Double units = -1.0;
		Double agreedFX = 1.2;
		// When
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Price, Unit, AgreedFx should be valid positive number");

		TradeReportUtil.shouldBeNonNegative(price, units, agreedFX);

	}
}
