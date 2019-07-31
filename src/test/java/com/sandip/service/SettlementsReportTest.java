package com.sandip.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sandip.enums.CurrencyType;
import com.sandip.enums.TransactionType;
import com.sandip.model.TradeInstruction;

public class SettlementsReportTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static SettlementsReport settlementsReport;
	
	private static final LocalDate INSTRUCTION_DATE = LocalDate.of(2019, Month.JULY, 31);

	private static final LocalDate SETTLEMENT_DATE = LocalDate.of(2019, Month.JULY, 31);
	private static final LocalDate SETTLEMENT_DATE_SATURDAY = LocalDate.of(2019, Month.JULY, 27);
	private static final LocalDate SETTLEMENT_DATE_MONDAY = LocalDate.of(2019, Month.AUGUST, 01);
	private List<TradeInstruction> tradeInstructions = populateTradeInstructions();;
	
	@Test
	public void testSettlementsReportArgumentNull() {
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("TradeInstruction list should not be null");
		
		settlementsReport = new SettlementsReport(null);
	}
	
	@Test
	public void testDailyIncomingOutgoingReport() {
		// Given
		settlementsReport = new SettlementsReport(this.tradeInstructions);
		// When
		settlementsReport.dailyIncomingOutgoingReport();
	}
	
	@Test
	public void testRankingOfEntitiesReport() {
		// Given
		settlementsReport = new SettlementsReport(this.tradeInstructions);
		// When
		settlementsReport.rankingOfEntities();
	}

	private List<TradeInstruction> populateTradeInstructions() {
		List<TradeInstruction> tradeInstructions = new ArrayList<>();

		TradeInstruction tradeInstruction = new TradeInstruction("Doo", TransactionType.BUY, CurrencyType.INR, 1.0,
				INSTRUCTION_DATE, SETTLEMENT_DATE, 1, 100.0);
		tradeInstructions.add(tradeInstruction);

		tradeInstruction = new TradeInstruction("Doo", TransactionType.BUY, CurrencyType.INR, 1.0, INSTRUCTION_DATE,
				SETTLEMENT_DATE, 1, 125.6);
		tradeInstructions.add(tradeInstruction);

		tradeInstruction = new TradeInstruction("Too", TransactionType.BUY, CurrencyType.INR, 1.0, INSTRUCTION_DATE,
				SETTLEMENT_DATE_SATURDAY, 1, 150.0);
		tradeInstructions.add(tradeInstruction);

		tradeInstruction = new TradeInstruction("Roo", TransactionType.SELL, CurrencyType.INR, 1.0, INSTRUCTION_DATE,
				SETTLEMENT_DATE, 1, 190.0);
		tradeInstructions.add(tradeInstruction);

		tradeInstruction = new TradeInstruction("Foo", TransactionType.SELL, CurrencyType.INR, 1.0, INSTRUCTION_DATE,
				SETTLEMENT_DATE_MONDAY, 1, 116.0);
		tradeInstructions.add(tradeInstruction);
		return tradeInstructions;
	}

}
