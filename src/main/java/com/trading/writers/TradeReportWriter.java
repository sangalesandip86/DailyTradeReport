package com.trading.writers;

import java.time.LocalDate;
import java.util.Map;

import com.trading.enums.InstructionType;

/**
 * 
 * @author sandip.p.sangale
 *
 */
public interface TradeReportWriter {
	public void generateReport(Map<InstructionType, Map<LocalDate, Double>> instructionTypeDateWiseTradeAmountMap,
			Map<InstructionType, Map<String, Double>> instructionTypeEntityWiseTradeAmount);
}
