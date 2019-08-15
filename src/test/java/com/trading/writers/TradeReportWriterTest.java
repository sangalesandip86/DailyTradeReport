package com.trading.writers;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.trading.enums.InstructionType;

public class TradeReportWriterTest {
	private static final String EMPTY = "";
	private TradeReportWriter tradeReportWriter = new ConsoleTradeReportWriter();
	private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

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
	public void testGenerateReportNullInputs() {
		// Given
		Map<InstructionType, Map<LocalDate, Double>> instructionTypeDateWiseTradeAmountMap = null;
		Map<InstructionType, Map<String, Double>> instructionTypeEntityWiseTradeAmount = null;
		// When
		tradeReportWriter.generateReport(instructionTypeDateWiseTradeAmountMap, instructionTypeEntityWiseTradeAmount);
		// Then
		String expectedReport = EMPTY;
		assertEquals(expectedReport, outContent.toString().trim());
	}

	@Test
	public void testGenerateReportValidInputs() {
		// Given
		Map<InstructionType, Map<LocalDate, Double>> instructionTypeDateWiseTradeAmountMap = new HashMap<>();
		Map<LocalDate, Double> dateWiseTradeAmountMap = new HashMap<>();
		dateWiseTradeAmountMap.put(LocalDate.of(2019, 8, 15), 100.0);
		dateWiseTradeAmountMap.put(LocalDate.of(2019, 8, 14), 100.0);
		instructionTypeDateWiseTradeAmountMap.put(InstructionType.BUY, dateWiseTradeAmountMap);

		Map<InstructionType, Map<String, Double>> instructionTypeEntityWiseTradeAmount = new HashMap<>();
		Map<String, Double> entityWiseTradeAmountMap = new LinkedHashMap<>();
		entityWiseTradeAmountMap.put("Foo", 125.0);
		entityWiseTradeAmountMap.put("Roo", 120.0);
		instructionTypeEntityWiseTradeAmount.put(InstructionType.SELL, entityWiseTradeAmountMap);
		// When
		tradeReportWriter.generateReport(instructionTypeDateWiseTradeAmountMap, instructionTypeEntityWiseTradeAmount);
		// Then
		String expectedReport = "** Outgoing Everyday **\n\n" + "Settlement Date      Amount              \r\n"
				+ "---------------      -------             \r\n" + "2019-08-15           $100.00             \r\n"
				+ "2019-08-14           $100.00             \r\n\n" + "** Incoming Entity Ranking **\n\n"
				+ "Entity Name          Amount              \r\n" + "---------------      -------             \r\n"
				+ "Foo                  $125.00             \r\n" + "Roo                  $120.00";

		assertEquals(expectedReport, outContent.toString().trim());
	}

	@Test
	public void testGenerateReportValidInput() {
		// Given
		Map<InstructionType, Map<LocalDate, Double>> instructionTypeDateWiseTradeAmountMap = new HashMap<>();
		Map<LocalDate, Double> dateWiseTradeAmountMap = new HashMap<>();
		dateWiseTradeAmountMap.put(LocalDate.of(2019, 8, 15), 100.0);
		dateWiseTradeAmountMap.put(LocalDate.of(2019, 8, 14), 100.0);
		instructionTypeDateWiseTradeAmountMap.put(InstructionType.SELL, dateWiseTradeAmountMap);

		Map<InstructionType, Map<String, Double>> instructionTypeEntityWiseTradeAmount = new HashMap<>();
		Map<String, Double> entityWiseTradeAmountMap = new LinkedHashMap<>();
		entityWiseTradeAmountMap.put("Foo", 125.0);
		entityWiseTradeAmountMap.put("Roo", 120.0);
		instructionTypeEntityWiseTradeAmount.put(InstructionType.BUY, entityWiseTradeAmountMap);
		// When
		tradeReportWriter.generateReport(instructionTypeDateWiseTradeAmountMap, instructionTypeEntityWiseTradeAmount);
		// Then
		String expectedReport = "** Incoming Everyday **\n\n" + "Settlement Date      Amount              \r\n"
				+ "---------------      -------             \r\n" + "2019-08-15           $100.00             \r\n"
				+ "2019-08-14           $100.00             \r\n\n" + "** Outgoing Entity Ranking **\n\n"
				+ "Entity Name          Amount              \r\n" + "---------------      -------             \r\n"
				+ "Foo                  $125.00             \r\n" + "Roo                  $120.00";

		assertEquals(expectedReport, outContent.toString().trim());
	}
}
