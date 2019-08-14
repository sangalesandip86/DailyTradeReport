package com.trading.readers;

import java.io.IOException;
import java.util.List;

import com.trading.model.TradeInstruction;

/**
 * 
 * @author sandip.p.sangale
 *
 */
public interface TradeInstructionReader {
	List<TradeInstruction> readTradeInstructions() throws IOException;
}
