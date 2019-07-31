package com.sandip.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.sandip.enums.TransactionType;
import com.sandip.model.TradeInstruction;
import com.sandip.utils.TradeFormulaes;
import com.sandip.utils.TradeReportUtil;

/**
 * 
 * @author sandip.p.sangale
 *
 */
public class Settlements {

	private static final String REPORT_HEADER_PRINT_FORMAT = "%-20s %-20s%n";
	DecimalFormat df = new DecimalFormat("0.00");

	/**
	 * Accepts List of Trade Instruction in list format and process them one by one
	 * Also calculate and set working day of settlementDate
	 * 
	 * @param tradeInstructions
	 */
	public void settleTradeInstructions(List<TradeInstruction> tradeInstructions) {
		// For each TradeInstruction If settlement date falls on weekend then set it to
		// Next working day
		tradeInstructions.stream().forEach(tradeInstruction -> tradeInstruction.setSettlementDate(TradeReportUtil
				.workingDayOfSettlementDate(tradeInstruction.getSettlementDate(), tradeInstruction.getCurrencyType())));

		dailyIncomingOutgoingReport(tradeInstructions);
		rankingOfEntities(tradeInstructions);
	}

	/**
	 * This methods Group by TransactionType and Settlement Date and summing of tradeAmount Settlement date wise
	 * 
	 * @param tradeInstructions
	 */
	private void dailyIncomingOutgoingReport(List<TradeInstruction> tradeInstructions) {
		Map<TransactionType, Map<LocalDate, Double>> tradeEachDay = tradeInstructions.stream()
				.collect(Collectors.groupingBy(TradeInstruction::getTransactionType,
						Collectors.groupingBy(TradeInstruction::getSettlementDate,
								Collectors.summingDouble(TradeFormulaes.TRADE_AMOUNT_IN_USD::applyAsDouble))));

		displayDaywiseIncomingOutgoingTrade(tradeEachDay, TransactionType.BUY);
		displayDaywiseIncomingOutgoingTrade(tradeEachDay, TransactionType.SELL);
	}

	private void displayDaywiseIncomingOutgoingTrade(Map<TransactionType, Map<LocalDate, Double>> tradeEachDay,
			TransactionType transactionType) {
		printIncomingOutgoingReportHeaders(transactionType);
		tradeEachDay.get(transactionType).forEach((settlementDate, amount) -> System.out
				.printf(REPORT_HEADER_PRINT_FORMAT, settlementDate, "$" + df.format(amount)));
	}

	private void printIncomingOutgoingReportHeaders(TransactionType transactionType) {
		System.out.println();
		if (TransactionType.BUY.equals(transactionType)) {

			System.out.println("** Outgoing Everyday **");
		} else {
			System.out.println("** Incoming Everyday **");
		}
		System.out.println();
		System.out.printf(REPORT_HEADER_PRINT_FORMAT, "Settlement Date", "Amount");
		System.out.printf(REPORT_HEADER_PRINT_FORMAT, "---------------", "-------");
	}

	private void rankingOfEntities(List<TradeInstruction> tradeInstructions) {
		Map<TransactionType, Map<String, Double>> tradeEachDay = tradeInstructions.stream()
				.collect(Collectors.groupingBy(TradeInstruction::getTransactionType,
						Collectors.groupingBy(TradeInstruction::getEntity,
								Collectors.summingDouble(TradeFormulaes.TRADE_AMOUNT_IN_USD::applyAsDouble))));

		tradeEachDay.forEach(this::tradeAmountWiseRankingOfEntities);
	}

	private void tradeAmountWiseRankingOfEntities(TransactionType transactionType,
			Map<String, Double> entityTradeAmountMap) {
		printRankingReportHeader(transactionType);
		LinkedHashMap<String, Double> entityAmountRanking = entityTradeAmountMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		entityAmountRanking.forEach((entityName, entityTradeAmount) -> {
			System.out.printf(REPORT_HEADER_PRINT_FORMAT, entityName, "$" + df.format(entityTradeAmount));
		});
	}

	private void printRankingReportHeader(TransactionType transactionType) {
		System.out.println();
		if (TransactionType.BUY.equals(transactionType)) {
			System.out.println("** Outgoing Entity Ranking **");
		} else {
			System.out.println("** Incoming Entity Ranking **");
		}
		System.out.println();
		System.out.printf(REPORT_HEADER_PRINT_FORMAT, "Entity Name", "Amount");
		System.out.printf(REPORT_HEADER_PRINT_FORMAT, "---------------", "-------");
	}

}
