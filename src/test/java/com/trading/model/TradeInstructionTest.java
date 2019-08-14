package com.trading.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.trading.enums.CurrencyType;
import com.trading.enums.InstructionType;
import com.trading.utils.TradeFormulaes;

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
		TradeInstruction tradeInstruction = new TradeInstruction("Foo", InstructionType.BUY, CurrencyType.INR, 1.2,
				LocalDate.of(2019, 07, 31), LocalDate.of(2019, 8, 01), 1, 123.0);
		assertNotNull(tradeInstruction);
	}

	@Test
	public void testTradeInstructionThrowExceptionIfAnyValueOfPriceUnitAgreedFxIsNegative() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Price, Unit, AgreedFx should be valid positive number");

		new TradeInstruction("Foo", InstructionType.BUY, CurrencyType.INR, -1.0, LocalDate.of(2019, 07, 31),
				LocalDate.of(2019, 8, 01), 1, 123.0);

	}

	@Test
	public void testCalculateUSDAmountOfTrade() {
		// Given
		TradeInstruction tradeInstruction = new TradeInstruction("Foo", InstructionType.BUY, CurrencyType.INR, 1.2,
				LocalDate.of(2019, 07, 31), LocalDate.of(2019, 8, 01), 1, 123.0);
		// when
		Double tradeAmountInUSD = TradeFormulaes.TRADE_AMOUNT_IN_USD.applyAsDouble(tradeInstruction);
		// Then
		assertEquals(147.6, tradeAmountInUSD, 0);
	}
}
