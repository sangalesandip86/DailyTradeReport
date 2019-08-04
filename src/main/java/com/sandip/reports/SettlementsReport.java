package com.sandip.reports;

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
 * This interface provides abstract method printReport
 * 
 * It contains default method which provides implementation for common operation related to report.
 * 
 * @author sandip.p.sangale
 *
 */
public interface SettlementsReport {

	/**
	 * Calculate and set valid working day of settlementDate based on currency Type
	 * 
	 * @param tradeInstructions
	 */
	default void calculateValidWeekdayForSettlementDate(List<TradeInstruction> tradeInstructions) {
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
	default Map<InstructionType, Map<LocalDate, Double>> groupByInstructionTypeAndSettlementDate(
			List<TradeInstruction> tradeInstructions) {
		return tradeInstructions.stream()
				.collect(Collectors.groupingBy(TradeInstruction::getInstructionType,
						Collectors.groupingBy(TradeInstruction::getSettlementDate,
								Collectors.summingDouble(TradeFormulaes.TRADE_AMOUNT_IN_USD::applyAsDouble))));
	}

	/**
	 * 
	 * @param entityTradeAmountMap
	 * @return
	 */
	default LinkedHashMap<String, Double> sortMapByTradeAmountDescendingOrder(
			Map<String, Double> entityTradeAmountMap) {
		return entityTradeAmountMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	/**
	 * Group List by Instruction type and Entity, sum TradeAmount entity wise
	 */
	default Map<InstructionType, Map<String, Double>> groupByInstructionTypeAndEntityWise(
			List<TradeInstruction> tradeInstructions) {
		return tradeInstructions.stream()
				.collect(Collectors.groupingBy(TradeInstruction::getInstructionType,
						Collectors.groupingBy(TradeInstruction::getEntity,
								Collectors.summingDouble(TradeFormulaes.TRADE_AMOUNT_IN_USD::applyAsDouble))));
	}

	/**
	 * Method to generate report
	 * 
	 * @param tradeInstructions
	 */
	public void printReport(List<TradeInstruction> tradeInstructions);

}
