package com.sandip.reports;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.sandip.enums.InstructionType;
import com.sandip.model.TradeInstruction;
import com.sandip.utils.TradeFormulaes;
import com.sandip.utils.TradeReportUtil;

/**
 *
 * This class capable of generating following details in report
 * 
 * <li>Amount in USD settled incoming everyday</li>
 * <li>Amount in USD settled outgoing everyday</li>
 * <li>Ranking of entities based on incoming and outgoing amount. Eg: If entity
 * foo instructs the highest amount for a buy instruction, then foo is rank 1
 * for outgoing</li>
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
	private static final DecimalFormat AMOUNT_DECIMAL_FORMAT = new DecimalFormat("0.00");

	public static void main(String[] args) {

		SettlementsReport settlementsReport = new SettlementsReport();

		try {
			settlementsReport.printReport(TradeReportUtil.populateTradeInstructions());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Perform calculation of valid weekday settlement date.
	 * 
	 * Calls method to print incoming,Outgoing daily TradeAmountInUSD.
	 * 
	 * Calls method to print incoming, outgoing ranking Of entities by
	 * TradeAmountInUSD
	 * 
	 * @param instructionType
	 */
	public void printReport(List<TradeInstruction> tradeInstructions) {
		if (tradeInstructions == null) {
			throw new IllegalArgumentException(TRADE_INSTRUCTION_LIST_SHOULD_NOT_BE_NULL);
		}
		this.calculateValidWeekdayForSettlementDate(tradeInstructions);
		this.dailyTradeAmountInUSD(tradeInstructions);
		this.rankingOfEntitiesByTradeAmountInUSD(tradeInstructions);
	}

	/**
	 * This methods Group by TransactionType and Settlement Date, sum of tradeAmount
	 * settlement date wise, calls printSettlementDateWiseTradeAmount method to
	 * print report to console
	 * 
	 * @param tradeInstructions
	 */
	private void dailyTradeAmountInUSD(List<TradeInstruction> tradeInstructions) {
		Map<InstructionType, Map<LocalDate, Double>> instructionTypeDateWiseTradeAmountMap = groupByInstructionTypeAndSettlementDate(
				tradeInstructions);
		//Note : Map keys are sorted to maintain Incoming, Outgoing section in consistent order on report
		instructionTypeDateWiseTradeAmountMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.forEach(entry -> printSettlementDateWiseTradeAmount(entry.getKey(), entry.getValue()));
	}

	/**
	 * Calculate and set valid working day of settlementDate based on currency Type
	 * 
	 * @param tradeInstructions
	 */
	private void calculateValidWeekdayForSettlementDate(List<TradeInstruction> tradeInstructions) {
		// For each TradeInstruction If settlement date falls on weekend then set it to
		// Next working day
		tradeInstructions.stream().forEach(tradeInstruction -> tradeInstruction.setSettlementDate(TradeReportUtil
				.workingDayOfSettlementDate(tradeInstruction.getSettlementDate(), tradeInstruction.getCurrencyType())));
	}

	/**
	 * Group List by Instruction type and Settlement Date, sum TradeAmount
	 * settlement date wise.
	 * 
	 * Does not perform grouping operation again if similar report generated
	 * already, uses earlier populated instructionTypeDateWiseTradeAmountMap
	 * 
	 * @param tradeInstructions
	 */
	private Map<InstructionType, Map<LocalDate, Double>> groupByInstructionTypeAndSettlementDate(
			List<TradeInstruction> tradeInstructions) {
		return tradeInstructions.stream()
				.collect(Collectors.groupingBy(TradeInstruction::getInstructionType,
						Collectors.groupingBy(TradeInstruction::getSettlementDate,
								Collectors.summingDouble(TradeFormulaes.TRADE_AMOUNT_IN_USD::applyAsDouble))));
	}

	/**
	 * 
	 * @param instructionType
	 * @param settlementDateWiseTradeAmountMap
	 */
	private void printSettlementDateWiseTradeAmount(InstructionType instructionType,
			Map<LocalDate, Double> settlementDateWiseTradeAmountMap) {
		printIncomingOutgoingReportHeaders(instructionType);
		settlementDateWiseTradeAmountMap
				.forEach((settlementDate, tradeAmountInUSD) -> System.out.printf(REPORT_HEADER_PRINT_FORMAT,
						settlementDate, DOLLAR_SYMBOL + AMOUNT_DECIMAL_FORMAT.format(tradeAmountInUSD)));
	}

	/**
	 * prints Incoming/Outgoing Header to console
	 * 
	 * @param transactionType
	 */
	private void printIncomingOutgoingReportHeaders(InstructionType transactionType) {
		System.out.println();
		if (InstructionType.BUY.equals(transactionType)) {

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
	private void rankingOfEntitiesByTradeAmountInUSD(List<TradeInstruction> tradeInstructions) {
		Map<InstructionType, Map<String, Double>> instructionTypeEntityWiseTradeAmount = groupByInstructionTypeAndEntityWise(
				tradeInstructions);
		instructionTypeEntityWiseTradeAmount.forEach(this::printTradeAmountWiseRankingOfEntities);
	}

	/**
	 * Group List by Instruction type and Entity, sum TradeAmount entity wise
	 */
	private Map<InstructionType, Map<String, Double>> groupByInstructionTypeAndEntityWise(
			List<TradeInstruction> tradeInstructions) {
		return tradeInstructions.stream()
				.collect(Collectors.groupingBy(TradeInstruction::getInstructionType,
						Collectors.groupingBy(TradeInstruction::getEntity,
								Collectors.summingDouble(TradeFormulaes.TRADE_AMOUNT_IN_USD::applyAsDouble))));
	}

	/**
	 * Sort Entity tradeAmountInUSD in descending order and print values to console
	 * 
	 * @param transactionType
	 * @param entityTradeAmountMap
	 */
	private void printTradeAmountWiseRankingOfEntities(InstructionType transactionType,
			Map<String, Double> entityTradeAmountMap) {
		printRankingReportHeader(transactionType);
		LinkedHashMap<String, Double> entityAmountRanking = sortMapByTradeAmountDescendingOrder(entityTradeAmountMap);
		entityAmountRanking.forEach((entityName, entityTradeAmount) -> System.out.printf(REPORT_HEADER_PRINT_FORMAT,
				entityName, DOLLAR_SYMBOL + AMOUNT_DECIMAL_FORMAT.format(entityTradeAmount)));
	}

	/**
	 * 
	 * @param entityTradeAmountMap
	 * @return
	 */
	private LinkedHashMap<String, Double> sortMapByTradeAmountDescendingOrder(
			Map<String, Double> entityTradeAmountMap) {
		return entityTradeAmountMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	/**
	 * prints Ranking Report Header to console
	 * 
	 * @param transactionType
	 */
	private void printRankingReportHeader(InstructionType transactionType) {
		System.out.println();
		if (InstructionType.BUY.equals(transactionType)) {
			System.out.println(OUTGOING_ENTITY_RANKING_TITLE);
		} else {
			System.out.println(INCOMING_ENTITY_RANKING_TITLE);
		}
		System.out.println();
		System.out.printf(REPORT_HEADER_PRINT_FORMAT, ENTITY_NAME_HEADER, AMOUNT_HEADER);
		System.out.printf(REPORT_HEADER_PRINT_FORMAT, "---------------", "-------");
	}

}
