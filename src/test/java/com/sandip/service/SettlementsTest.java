package com.sandip.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sandip.enums.CurrencyType;
import com.sandip.enums.TransactionType;
import com.sandip.model.TradeInstruction;

public class SettlementsTest {
	private Settlements settlements;

	@Before
	public void setUp() throws IOException {
		settlements = new Settlements();
	}

	@Test
	public void testSettleDailyTrade() {
		// Given
		List<TradeInstruction> tradeInstructions = new ArrayList<>();
		
		TradeInstruction tradeInstruction = new TradeInstruction("Doo", TransactionType.BUY, CurrencyType.INR, 1.0, LocalDate.now(), LocalDate.now(), 1, 100.0);
		tradeInstructions.add(tradeInstruction);
		
		tradeInstruction = new TradeInstruction("Doo", TransactionType.BUY, CurrencyType.INR, 1.0, LocalDate.now(),	LocalDate.now(), 1, 125.6);
		tradeInstructions.add(tradeInstruction);
		
		tradeInstruction = new TradeInstruction("Too", TransactionType.BUY, CurrencyType.INR, 1.0, LocalDate.now(), LocalDate.now().plusDays(3), 1, 150.0);
		tradeInstructions.add(tradeInstruction);

		tradeInstruction = new TradeInstruction("Roo", TransactionType.SELL, CurrencyType.INR, 1.0, LocalDate.now(), LocalDate.now(), 1, 190.0);
		tradeInstructions.add(tradeInstruction);

		tradeInstruction = new TradeInstruction("Foo", TransactionType.SELL, CurrencyType.INR, 1.0, LocalDate.now(), LocalDate.now().plusDays(5), 1, 116.0);
		tradeInstructions.add(tradeInstruction);
		// When
		settlements.settleTradeInstructions(tradeInstructions);
		// Then

	}

}
