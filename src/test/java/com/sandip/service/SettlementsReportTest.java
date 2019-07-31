package com.sandip.service;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sandip.model.TradeInstruction;
import com.sandip.utils.TradeReportUtil;

public class SettlementsReportTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
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

	private static SettlementsReport settlementsReport;
	private List<TradeInstruction> tradeInstructions = TradeReportUtil.populateTradeInstructions();;

	@Test
	public void testSettlementsReportArgumentNull() {

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("TradeInstruction list should not be null");

		settlementsReport = new SettlementsReport(null);
	}

	@Test
	public void testDailyIncomingOutgoingReport() {
		// Given
		settlementsReport = new SettlementsReport(this.tradeInstructions);
		// When
		settlementsReport.dailyIncomingOutgoingReport();
		//Then
		String expectedReport ="\r\n" + 
				"** Outgoing Everyday **\r\n" + 
				"\r\n" + 
				"Settlement Date      Amount              \r\n" + 
				"---------------      -------             \r\n" + 
				"2019-07-31           $225.60             \r\n" + 
				"2019-07-29           $150.00             \r\n" + 
				"\r\n" + 
				"** Incoming Everyday **\r\n" + 
				"\r\n" + 
				"Settlement Date      Amount              \r\n" + 
				"---------------      -------             \r\n" + 
				"2019-07-31           $190.00             \r\n" + 
				"2019-08-01           $116.00             \r\n" + 
				"";
		
		assertEquals(expectedReport, outContent.toString());
	}

	@Test
	public void testRankingOfEntitiesReport() {
		// Given
		settlementsReport = new SettlementsReport(this.tradeInstructions);
		// When
		settlementsReport.rankingOfEntitiesReport();
		String expectedRankingReport = "\r\n" + 
				"** Outgoing Entity Ranking **\r\n" + 
				"\r\n" + 
				"Entity Name          Amount              \r\n" + 
				"---------------      -------             \r\n" + 
				"Doo                  $225.60             \r\n" + 
				"Too                  $150.00             \r\n" + 
				"\r\n" + 
				"** Incoming Entity Ranking **\r\n" + 
				"\r\n" + 
				"Entity Name          Amount              \r\n" + 
				"---------------      -------             \r\n" + 
				"Roo                  $190.00             \r\n" + 
				"Foo                  $116.00             \r\n" + 
				"";
		assertEquals(expectedRankingReport, outContent.toString());
	}


	@Test
	public void testRankingOfEntitiesReportEmptyList() {
		// Given
		settlementsReport = new SettlementsReport(new ArrayList<>());
		// When
		settlementsReport.rankingOfEntitiesReport();
		assertEquals("", outContent.toString());
	}
	
	@Test
	public void testDailyIncomingOutgoingReportEmptyList() {
		// Given
		settlementsReport = new SettlementsReport(new ArrayList<>());
		// When
		settlementsReport.dailyIncomingOutgoingReport();
		assertEquals("", outContent.toString());
	}


}
