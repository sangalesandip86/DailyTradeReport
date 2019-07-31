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
public class SettlementsReport {

	private static final String DOLLAR_SYMBOL = "$";
	private static final String OUTGOING_ENTITY_RANKING_TITLE = "** Outgoing Entity Ranking **";
	private static final String INCOMING_ENTITY_RANKING_TITLE = "** Incoming Entity Ranking **";
	private static final String ENTITY_NAME_HEADER = "Entity Name";
	private static final String AMOUNT_HEADER = "Amount";
	private static final String SETTLEMENT_DATE_HEADER = "Settlement Date";
	private static final String INCOMING_EVERYDAY_TITLE = "** Incoming Everyday **";
	private static final String OUTGOING_EVERYDAY_TITLE = "** Outgoing Everyday **";
	private static final String REPORT_HEADER_PRINT_FORMAT = "%-20s %-20s%n";
	private static final String TRADE_INSTRUCTION_LIST_SHOULD_NOT_BE_NULL = "TradeInstruction list should not be null";
	private static final DecimalFormat df = new DecimalFormat("0.00");
	private List<TradeInstruction> tradeInstructions;

	public static void main(String[] args) {
		SettlementsReport settlementsReport = new SettlementsReport(TradeReportUtil.populateTradeInstructions());
		settlementsReport.dailyIncomingOutgoingReport();
		settlementsReport.rankingOfEntitiesReport();
	}

	/**
	 * Accepts List of TradeInstruction and apply SettlementDate weekdays logic on
	 * each tradeInstruction object
	 * 
	 * @param tradeInstructions
	 */
	public SettlementsReport(final List<TradeInstruction> tradeInstructions) {
		if (tradeInstructions == null) {
			throw new IllegalArgumentException(TRADE_INSTRUCTION_LIST_SHOULD_NOT_BE_NULL);
		}
		this.tradeInstructions = tradeInstructions;
		this.calculateValidWeekdayForSettlementDate();
	}

	/**
	 * Calculate and set valid working day of settlementDate based on currency Type
	 * 
	 * @param tradeInstructions
	 */
	private void calculateValidWeekdayForSettlementDate() {
		// For each TradeInstruction If settlement date falls on weekend then set it to
		// Next working day
		this.tradeInstructions.stream().forEach(tradeInstruction -> tradeInstruction.setSettlementDate(TradeReportUtil
				.workingDayOfSettlementDate(tradeInstruction.getSettlementDate(), tradeInstruction.getCurrencyType())));
	}

	/**
	 * This methods Group by TransactionType and Settlement Date and summing of
	 * tradeAmount Settlement date wise
	 */
	public void dailyIncomingOutgoingReport() {
		Map<TransactionType, Map<LocalDate, Double>> tradeEachDay = this.tradeInstructions.stream()
				.collect(Collectors.groupingBy(TradeInstruction::getTransactionType,
						Collectors.groupingBy(TradeInstruction::getSettlementDate,
								Collectors.summingDouble(TradeFormulaes.TRADE_AMOUNT_IN_USD::applyAsDouble))));
		tradeEachDay.forEach(this::displayDaywiseIncomingOutgoingTrade);
	}

	/**
	 * 
	 * @param datewiseTradeAmountMap
	 * @param transactionType
	 */
	private void displayDaywiseIncomingOutgoingTrade(TransactionType transactionType,
			Map<LocalDate, Double> datewiseTradeAmountMap) {
		printIncomingOutgoingReportHeaders(transactionType);
		datewiseTradeAmountMap.forEach((settlementDate, amount) -> System.out.printf(REPORT_HEADER_PRINT_FORMAT,
				settlementDate, DOLLAR_SYMBOL + df.format(amount)));
	}

	/**
	 * prints Incoming/Outgoing Header to console
	 * 
	 * @param transactionType
	 */
	private void printIncomingOutgoingReportHeaders(TransactionType transactionType) {
		System.out.println();
		if (TransactionType.BUY.equals(transactionType)) {

			System.out.println(OUTGOING_EVERYDAY_TITLE);
		} else {
			System.out.println(INCOMING_EVERYDAY_TITLE);
		}
		System.out.println();
		System.out.printf(REPORT_HEADER_PRINT_FORMAT, SETTLEMENT_DATE_HEADER, AMOUNT_HEADER);
		System.out.printf(REPORT_HEADER_PRINT_FORMAT, "---------------", "-------");
	}

	/**
	 * Group TradeInstruction entity wise and rank them in decreasing order of sum
	 * of tradeAmountInUSD
	 */
	public void rankingOfEntitiesReport() {
		Map<TransactionType, Map<String, Double>> tradeEachDay = this.tradeInstructions.stream()
				.collect(Collectors.groupingBy(TradeInstruction::getTransactionType,
						Collectors.groupingBy(TradeInstruction::getEntity,
								Collectors.summingDouble(TradeFormulaes.TRADE_AMOUNT_IN_USD::applyAsDouble))));

		tradeEachDay.forEach(this::tradeAmountWiseRankingOfEntities);
	}

	/**
	 * Sort Entity tradeAmountInUSD in descending order and print values to console
	 * 
	 * @param transactionType
	 * @param entityTradeAmountMap
	 */
	private void tradeAmountWiseRankingOfEntities(TransactionType transactionType,
			Map<String, Double> entityTradeAmountMap) {
		printRankingReportHeader(transactionType);
		LinkedHashMap<String, Double> entityAmountRanking = entityTradeAmountMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		entityAmountRanking.forEach((entityName, entityTradeAmount) -> {
			System.out.printf(REPORT_HEADER_PRINT_FORMAT, entityName, DOLLAR_SYMBOL + df.format(entityTradeAmount));
		});
	}

	/**
	 * prints Ranking Report Header to console
	 * 
	 * @param transactionType
	 */
	private void printRankingReportHeader(TransactionType transactionType) {
		System.out.println();
		if (TransactionType.BUY.equals(transactionType)) {
			System.out.println(OUTGOING_ENTITY_RANKING_TITLE);
		} else {
			System.out.println(INCOMING_ENTITY_RANKING_TITLE);
		}
		System.out.println();
		System.out.printf(REPORT_HEADER_PRINT_FORMAT, ENTITY_NAME_HEADER, AMOUNT_HEADER);
		System.out.printf(REPORT_HEADER_PRINT_FORMAT, "---------------", "-------");
	}

}
