package com.sandip.input;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sandip.model.TradeInstruction;

public class TradeInstructionReaderTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	TradeInstructionReader inputProcessor = new TextTradeInstructionReader("tradeInstructions.txt");

	@Test
	public void testMapToTradeInstruction() throws IOException {
		List<TradeInstruction> tradeInstructions = inputProcessor.processTradeInstructions();
		assertNotNull(tradeInstructions);
		assertFalse(tradeInstructions.isEmpty());
	}
}
