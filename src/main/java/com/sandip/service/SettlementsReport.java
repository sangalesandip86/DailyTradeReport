package com.sandip.service;

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
 * This class capable of generating following reports
 * 
 * <li>Incoming USD TradeAmount</li>
 * {@link SettlementsReport#dailyIncomingOutgoingReport()}
 * 
 * <li>Ranking of entities by Trade Amount</li>
 * {@link SettlementsReport#rankingOfEntitiesReport()}
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
	private Map<InstructionType, Map<LocalDate, Double>> instructionTypeDateWiseTradeAmountMap;
	private Map<InstructionType, Map<String, Double>> instructionTypeEntityWiseTradeAmount;

	public static void main(String[] args) {
		try {
			SettlementsReport settlementsReport = new SettlementsReport(TradeReportUtil.populateTradeInstructions());
			settlementsReport.dailyTradeAmountReport(InstructionType.BUY);
			settlementsReport.dailyTradeAmountReport(InstructionType.SELL);
			settlementsReport.rankingOfEntitiesReport();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
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
		this.tradeInstructions = Collections.unmodifiableList(this.tradeInstructions);
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
	 * 
	 * @param instructionType
	 */
	public void dailyTradeAmountReport(InstructionType instructionType) {
		groupByInstructionTypeAndSettlementDate();
		if (instructionTypeDateWiseTradeAmountMap.size() > 0) {
			printIncomingOutgoingReportHeaders(instructionType);
			this.instructionTypeDateWiseTradeAmountMap.get(instructionType)
					.forEach(this::printSettlementDateWiseTradeAmount);
		}
	}

	/**
	 * Group List by Instruction type and Settlement Date, sum TradeAmount
	 * settlement date wise
	 */
	private void groupByInstructionTypeAndSettlementDate() {
		if (this.instructionTypeDateWiseTradeAmountMap == null) {
			this.instructionTypeDateWiseTradeAmountMap = this.tradeInstructions.stream()
					.collect(Collectors.groupingBy(TradeInstruction::getInstructionType,
							Collectors.groupingBy(TradeInstruction::getSettlementDate,
									Collectors.summingDouble(TradeFormulaes.TRADE_AMOUNT_IN_USD::applyAsDouble))));
		}
	}

	/**
	 * 
	 * @param datewiseTradeAmountMap
	 * @param instructionType
	 */
	private void printSettlementDateWiseTradeAmount(LocalDate settlementDate, Double tradeAmountInUSD) {
		System.out.printf(REPORT_HEADER_PRINT_FORMAT, settlementDate, DOLLAR_SYMBOL + df.format(tradeAmountInUSD));
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
	public void rankingOfEntitiesReport() {
		groupByInstructionTypeAndEntityWise();
		this.instructionTypeEntityWiseTradeAmount.forEach(this::tradeAmountWiseRankingOfEntities);
	}

	/**
	 * Group List by Instruction type and Entity, sum TradeAmount entity wise
	 */
	private void groupByInstructionTypeAndEntityWise() {
		if (this.instructionTypeEntityWiseTradeAmount == null) {
			this.instructionTypeEntityWiseTradeAmount = this.tradeInstructions.stream()
					.collect(Collectors.groupingBy(TradeInstruction::getInstructionType,
							Collectors.groupingBy(TradeInstruction::getEntity,
									Collectors.summingDouble(TradeFormulaes.TRADE_AMOUNT_IN_USD::applyAsDouble))));
		}
	}

	/**
	 * Sort Entity tradeAmountInUSD in descending order and print values to console
	 * 
	 * @param transactionType
	 * @param entityTradeAmountMap
	 */
	private void tradeAmountWiseRankingOfEntities(InstructionType transactionType,
			Map<String, Double> entityTradeAmountMap) {
		printRankingReportHeader(transactionType);
		LinkedHashMap<String, Double> entityAmountRanking = sortMapByTradeAmountDescendingOrder(entityTradeAmountMap);
		entityAmountRanking.forEach((entityName, entityTradeAmount) -> {
			System.out.printf(REPORT_HEADER_PRINT_FORMAT, entityName, DOLLAR_SYMBOL + df.format(entityTradeAmount));
		});
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
