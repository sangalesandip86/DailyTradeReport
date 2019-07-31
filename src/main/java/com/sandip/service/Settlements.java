package com.sandip.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.sandip.enums.TransactionType;
import com.sandip.model.TradeInstruction;
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
	 * @param tradeInstructions
	 */
	public void settleTradeInstructions(List<TradeInstruction> tradeInstructions) {

		tradeInstructions.stream().forEach(tradeInstruction -> {
			tradeInstruction.setSettlementDate(TradeReportUtil.workingDayOfSettlementDate(
					tradeInstruction.getSettlementDate(), tradeInstruction.getCurrencyType()));
		});

		Map<TransactionType, Double> trade = tradeInstructions.stream()
				.collect(Collectors.groupingBy(TradeInstruction::getTransactionType,
						Collectors.summingDouble(TradeInstruction::calculateTradeAmountInUSD)));

		Map<TransactionType, Map<LocalDate, Double>> tradeEachDay = tradeInstructions.stream()
				.collect(Collectors.groupingBy(TradeInstruction::getTransactionType,
						Collectors.groupingBy(TradeInstruction::getSettlementDate,
								Collectors.summingDouble(TradeInstruction::calculateTradeAmountInUSD))));

		displayDaywiseIncomingOutgoingTrade(tradeEachDay, TransactionType.BUY);
		displayDaywiseIncomingOutgoingTrade(tradeEachDay, TransactionType.SELL);

		rankingOfEntities(tradeInstructions);
	}

	private void displayDaywiseIncomingOutgoingTrade(Map<TransactionType, Map<LocalDate, Double>> tradeEachDay,
			TransactionType transactionType) {
		incomingOutgoingReportHeaders(transactionType);
		tradeEachDay.get(transactionType).forEach((settlementDate, amount) -> {
			System.out.printf(REPORT_HEADER_PRINT_FORMAT, settlementDate, "$" + df.format(amount));
		});
	}

	private void incomingOutgoingReportHeaders(TransactionType transactionType) {
		System.out.println("");
		if (TransactionType.BUY.equals(transactionType)) {

			System.out.println("** Outgoing Everyday **");
		} else {
			System.out.println("** Incoming Everyday **");
		}

		System.out.printf(REPORT_HEADER_PRINT_FORMAT, "Settlement Date", "Amount");
		System.out.printf(REPORT_HEADER_PRINT_FORMAT, "---------------", "-------");
	}

	public List<TradeInstruction> rankingOfEntities(List<TradeInstruction> tradeInstructions) {
		Map<TransactionType, List<TradeInstruction>> rankingMap = tradeInstructions.stream()
				.collect(Collectors.groupingBy(TradeInstruction::getTransactionType));

		for (Entry<TransactionType, List<TradeInstruction>> entityList : rankingMap.entrySet()) {
			entityList.getValue().stream().collect(Collectors.groupingBy(TradeInstruction::getEntity,
					Collectors.summingDouble(TradeInstruction::calculateTradeAmountInUSD)));
		}

		/*
		 * tradeInstructions.stream().collect(Collectors.groupingBy(TradeInstruction::
		 * getTransactionType, Collectors.groupingBy( TradeInstruction::getEntity,
		 * Collectors.summingDouble(TradeInstruction::calculateUSDAmountOfTrade))));
		 */
		rankingMap.get(TransactionType.BUY);

		System.out.println(rankingMap);
		return null;
	}

	public LocalDate nextWorkingDay() {
		return null;
	}
}
