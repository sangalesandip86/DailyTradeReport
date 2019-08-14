package com.trading;

import java.io.IOException;
import java.time.format.DateTimeParseException;

import com.trading.readers.TextTradeInstructionReader;
import com.trading.readers.TradeInstructionReader;
import com.trading.reports.TradeReport;
import com.trading.reports.TradeReportImpl;

/**
 * 
 * @author sandip.p.sangale
 *
 */
public class DailyTradeReportApplication {
	public static void main(String[] args) {
		TradeReport settlementsReport = new TradeReportImpl();
		try {
			TradeInstructionReader tradeInstrcutionReader = new TextTradeInstructionReader("tradeInstructions.txt");
			settlementsReport.generateReport(tradeInstrcutionReader.readTradeInstructions());
		} catch (IllegalArgumentException | DateTimeParseException | IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
