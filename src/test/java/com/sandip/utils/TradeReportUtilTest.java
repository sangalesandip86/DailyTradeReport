package com.sandip.utils;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sandip.enums.CurrencyType;

public class TradeReportUtilTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testSettlementDateDontChangeIfValidWeekDay() {
		// Given
		LocalDate settlementDate = LocalDate.of(2019, 07, 28);
		// When
		LocalDate actualSettlementDate = TradeReportUtil.workingDayOfSettlementDate(settlementDate, CurrencyType.INR);
		// Then
		assertEquals(LocalDate.of(2019, 07, 29), actualSettlementDate);
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
