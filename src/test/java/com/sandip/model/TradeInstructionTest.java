package com.sandip.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sandip.enums.CurrencyType;
import com.sandip.enums.TransactionType;

/**
 * 
 * @author sandip.p.sangale
 *
 */
public class TradeInstructionTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testTradeInstructionObjectCreatedSuccesfully() {
		TradeInstruction tradeInstruction = new TradeInstruction("Foo", TransactionType.BUY, CurrencyType.INR, 1.2,
				LocalDate.of(2019, 07, 31), LocalDate.of(2019, 8, 01), 1, 123.0);
		assertNotNull(tradeInstruction);
	}

	@Test
	public void testTradeInstructionThrowExceptionIfAnyValueOfPriceUnitAgreedFxIsNegative() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Price, Unit, AgreedFx should be valid positive number");

		new TradeInstruction("Foo", TransactionType.BUY, CurrencyType.INR, -1.0, LocalDate.of(2019, 07, 31),
				LocalDate.of(2019, 8, 01), 1, 123.0);

	}

	@Test
	public void testCalculateUSDAmountOfTrade() {
		// Given
		TradeInstruction tradeInstruction = new TradeInstruction("Foo", TransactionType.BUY, CurrencyType.INR, 1.2,
				LocalDate.of(2019, 07, 31), LocalDate.of(2019, 8, 01), 1, 123.0);
		// when
		Double tradeAmountInUSD = tradeInstruction.calculateTradeAmountInUSD();
		// Then
		assertEquals(147.6, tradeAmountInUSD, 0);
	}
}
