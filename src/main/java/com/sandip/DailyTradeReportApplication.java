package com.sandip;

import java.io.IOException;
import java.time.format.DateTimeParseException;

import com.sandip.input.TradeInstructionReader;
import com.sandip.input.TextTradeInstructionReader;
import com.sandip.reports.SettlementsReport;
import com.sandip.reports.ConsoleSettlementsReport;
/**
 * 
 * @author sandip.p.sangale
 *
 */
public class DailyTradeReportApplication {
	public static void main(String[] args) {
		SettlementsReport settlementsReport = new ConsoleSettlementsReport();
		try {
			TradeInstructionReader inputProcessor = new TextTradeInstructionReader("tradeInstructions.txt");
			settlementsReport.printReport(inputProcessor.processTradeInstructions());
		} catch (IllegalArgumentException | DateTimeParseException | IOException e) {
			System.out.println(e.getMessage());
		}
	
	}
}
