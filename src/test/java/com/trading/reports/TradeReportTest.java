package com.trading.reports;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.trading.enums.CurrencyType;
import com.trading.enums.InstructionType;
import com.trading.model.TradeInstruction;

public class TradeReportTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;
	private TradeReport settlementsReport = new TradeReportImpl();;

	private static final LocalDate INSTRUCTION_DATE = LocalDate.of(2019, Month.JULY, 31);
	private static final LocalDate SETTLEMENT_DATE = LocalDate.of(2019, Month.JULY, 31);
	private static final LocalDate SETTLEMENT_DATE_SATURDAY = LocalDate.of(2019, Month.JULY, 27);
	private static final LocalDate SETTLEMENT_DATE_MONDAY = LocalDate.of(2019, Month.AUGUST, 01);

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void restoreStreams() {
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Test
	public void testSettlementsReportArgumentNull() {

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("TradeInstruction list should not be null");

		settlementsReport.generateReport(null);
	}

	@Test
	public void testPrintReport() {
		// When
		settlementsReport.generateReport(populateTradeInstructions());
		// Then
		String expectedReport = "\n" + "** Outgoing Everyday **\n\n" + "Settlement Date      Amount              \r\n"
				+ "---------------      -------             \r\n" + "2019-07-31           $225.60             \r\n"
				+ "2019-07-29           $150.00             \r\n" + "\n" + "** Incoming Everyday **\n\n"
				+ "Settlement Date      Amount              \r\n" + "---------------      -------             \r\n"
				+ "2019-07-31           $190.00             \r\n" + "2019-08-01           $116.00             \r\n"
				+ "\n" + "** Outgoing Entity Ranking **\n\n" + "Entity Name          Amount              \r\n"
				+ "---------------      -------             \r\n" + "Doo                  $225.60             \r\n"
				+ "Too                  $150.00             \r\n" + "\n" + "** Incoming Entity Ranking **\n\n"
				+ "Entity Name          Amount              \r\n" + "---------------      -------             \r\n"
				+ "Roo                  $190.00             \r\n" + "Foo                  $116.00             \r\n"
				+ "\r\n";
		assertEquals(expectedReport.trim(), outContent.toString().trim());
	}

	public static List<TradeInstruction> populateTradeInstructions() {
		List<TradeInstruction> tradeInstructions = new ArrayList<>();

		TradeInstruction tradeInstruction = new TradeInstruction("Doo", InstructionType.BUY, CurrencyType.INR, 1.0,
				INSTRUCTION_DATE, SETTLEMENT_DATE, 1, 100.0);
		tradeInstructions.add(tradeInstruction);

		tradeInstruction = new TradeInstruction("Doo", InstructionType.BUY, CurrencyType.INR, 1.0, INSTRUCTION_DATE,
				SETTLEMENT_DATE, 1, 125.6);
		tradeInstructions.add(tradeInstruction);

		tradeInstruction = new TradeInstruction("Too", InstructionType.BUY, CurrencyType.INR, 1.0, INSTRUCTION_DATE,
				SETTLEMENT_DATE_SATURDAY, 1, 150.0);
		tradeInstructions.add(tradeInstruction);

		tradeInstruction = new TradeInstruction("Roo", InstructionType.SELL, CurrencyType.INR, 1.0, INSTRUCTION_DATE,
				SETTLEMENT_DATE, 1, 190.0);
		tradeInstructions.add(tradeInstruction);

		tradeInstruction = new TradeInstruction("Foo", InstructionType.SELL, CurrencyType.INR, 1.0, INSTRUCTION_DATE,
				SETTLEMENT_DATE_MONDAY, 1, 116.0);
		tradeInstructions.add(tradeInstruction);
		return tradeInstructions;
	}
}
