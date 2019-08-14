package com.trading.writers;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;

import com.trading.enums.InstructionType;

public class ConsoleTradeReportWriter implements TradeReportWriter {

	private static final DecimalFormat AMOUNT_DECIMAL_FORMAT = new DecimalFormat("0.00");
	private static final String AMOUNT_HEADER = "Amount";
	private static final String DOLLAR_SYMBOL = "$";
	private static final String ENTITY_NAME_HEADER = "Entity Name";
	private static final String INCOMING_ENTITY_RANKING_TITLE = "** Incoming Entity Ranking **";
	private static final String INCOMING_EVERYDAY_TITLE = "** Incoming Everyday **";
	private static final String LINE_BREAK = "\n";
	private static final String OUTGOING_ENTITY_RANKING_TITLE = "** Outgoing Entity Ranking **";
	private static final String OUTGOING_EVERYDAY_TITLE = "** Outgoing Everyday **";
	private static final String REPORT_HEADER_PRINT_FORMAT = "%-20s %-20s%n";
	private static final String SETTLEMENT_DATE_HEADER = "Settlement Date";
	private static final String TWO_LINE_BREAK = "\n\n";
	private StringBuilder tradeReportString = new StringBuilder();

	@Override
	public void generateReport(Map<InstructionType, Map<LocalDate, Double>> instructionTypeDateWiseTradeAmountMap,
			Map<InstructionType, Map<String, Double>> instructionTypeEntityWiseTradeAmount) {
		instructionTypeDateWiseTradeAmountMap.forEach(this::printSettlementDateWiseTradeAmount);
		instructionTypeEntityWiseTradeAmount.forEach(this::printTradeAmountWiseRankingOfEntities);
		System.out.println(tradeReportString);
	}

	/**
	 * prints Incoming/Outgoing Header to console
	 * 
	 * @param transactionType
	 */
	private void printIncomingOutgoingReportHeaders(InstructionType transactionType) {
		tradeReportString.append(LINE_BREAK);
		if (InstructionType.BUY.equals(transactionType)) {
			tradeReportString.append(OUTGOING_EVERYDAY_TITLE);
		} else {
			tradeReportString.append(INCOMING_EVERYDAY_TITLE);
		}
		tradeReportString.append(TWO_LINE_BREAK);
		tradeReportString.append(String.format(REPORT_HEADER_PRINT_FORMAT, SETTLEMENT_DATE_HEADER, AMOUNT_HEADER));
		tradeReportString.append(String.format(REPORT_HEADER_PRINT_FORMAT, "---------------", "-------"));
	}

	/**
	 * prints Ranking Report Header to console
	 * 
	 * @param transactionType
	 */
	private void printRankingReportHeader(InstructionType transactionType) {
		tradeReportString.append(LINE_BREAK);
		if (InstructionType.BUY.equals(transactionType)) {
			tradeReportString.append(OUTGOING_ENTITY_RANKING_TITLE);
		} else {
			tradeReportString.append(INCOMING_ENTITY_RANKING_TITLE);
		}
		tradeReportString.append(TWO_LINE_BREAK);
		tradeReportString.append(String.format(REPORT_HEADER_PRINT_FORMAT, ENTITY_NAME_HEADER, AMOUNT_HEADER));
		tradeReportString.append(String.format(REPORT_HEADER_PRINT_FORMAT, "---------------", "-------"));
	}

	/**
	 * 
	 * @param instructionType
	 * @param settlementDateWiseTradeAmountMap
	 */
	private void printSettlementDateWiseTradeAmount(InstructionType instructionType,
			Map<LocalDate, Double> settlementDateWiseTradeAmountMap) {
		printIncomingOutgoingReportHeaders(instructionType);
		settlementDateWiseTradeAmountMap.forEach(
				(settlementDate, tradeAmountInUSD) -> tradeReportString.append(String.format(REPORT_HEADER_PRINT_FORMAT,
						settlementDate, DOLLAR_SYMBOL + AMOUNT_DECIMAL_FORMAT.format(tradeAmountInUSD))));
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
		entityTradeAmountMap.forEach(
				(entityName, entityTradeAmount) -> tradeReportString.append(String.format(REPORT_HEADER_PRINT_FORMAT,
						entityName, DOLLAR_SYMBOL + AMOUNT_DECIMAL_FORMAT.format(entityTradeAmount))));
	}

}
