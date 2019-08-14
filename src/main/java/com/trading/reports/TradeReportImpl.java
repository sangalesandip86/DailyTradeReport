package com.trading.reports;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.trading.enums.InstructionType;
import com.trading.model.TradeInstruction;
import com.trading.writers.ConsoleTradeReportWriter;
import com.trading.writers.TradeReportWriter;

/**
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
public class TradeReportImpl implements TradeReport {

	public static final TradeReportWriter TRADE_REPORT_WRITER = new ConsoleTradeReportWriter();

	private static final String TRADE_INSTRUCTION_LIST_SHOULD_NOT_BE_NULL = "TradeInstruction list should not be null";

	/**
	 * Perform calculation of valid weekday settlement date.
	 * 
	 * Calls method to print incoming,Outgoing daily TradeAmountInUSD.
	 * 
	 * Calls method to print incoming, outgoing ranking Of entities by
	 * TradeAmountInUSD
	 * 
	 * @param instructionType
	 * 
	 */
	@Override
	public void generateReport(List<TradeInstruction> tradeInstructions) {
		if (tradeInstructions == null) {
			throw new IllegalArgumentException(TRADE_INSTRUCTION_LIST_SHOULD_NOT_BE_NULL);
		}
		this.calculateValidWeekdayForSettlementDate(tradeInstructions);
		TRADE_REPORT_WRITER.generateReport(dailyTradeAmountInUSD(tradeInstructions),
				rankingOfEntitiesByTradeAmountInUSD(tradeInstructions));
	}

	/**
	 * This methods Group by TransactionType and Settlement Date, sum of tradeAmount
	 * settlement date wise
	 * 
	 * @param tradeInstructions
	 * @return
	 */
	private Map<InstructionType, Map<LocalDate, Double>> dailyTradeAmountInUSD(
			List<TradeInstruction> tradeInstructions) {
		Map<InstructionType, Map<LocalDate, Double>> instructionTypeDateWiseTradeAmountMap = groupByInstructionTypeAndSettlementDate(
				tradeInstructions);
		return instructionTypeDateWiseTradeAmountMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
						LinkedHashMap::new));

	}

	/**
	 * Group TradeInstruction entity wise and rank them in decreasing order of sum
	 * of tradeAmountInUSD
	 * 
	 * @param tradeInstructions
	 * @return
	 */
	private Map<InstructionType, Map<String, Double>> rankingOfEntitiesByTradeAmountInUSD(
			List<TradeInstruction> tradeInstructions) {
		Map<InstructionType, Map<String, Double>> instructionTypeEntityWiseTradeAmount = groupByInstructionTypeAndEntityWise(
				tradeInstructions);

		instructionTypeEntityWiseTradeAmount.entrySet()
				.forEach(entry -> entry.setValue(sortTradeAmountInDescendingOrder(entry.getValue())));
		// Note: Sorting map by keys to keep Incoming, Outgoing section in consistent
		// order on report
		return instructionTypeEntityWiseTradeAmount.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
						LinkedHashMap::new));

	}

}
