package com.trading;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger LOG = LoggerFactory.getLogger(DailyTradeReportApplication.class);

	public static void main(String[] args) {
		TradeReport settlementsReport = new TradeReportImpl();
		try {
			ResourceBundle resources = ResourceBundle.getBundle("config");
			TradeInstructionReader tradeInstrcutionReader = new TextTradeInstructionReader(
					resources.getString("tradeinstructions.file.path"));
			settlementsReport.generateReport(tradeInstrcutionReader.readTradeInstructions());
		} catch (IllegalArgumentException | DateTimeParseException | IOException e) {
			LOG.error(e.getMessage());
		}
	}

}
