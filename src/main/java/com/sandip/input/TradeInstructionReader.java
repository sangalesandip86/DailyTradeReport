package com.sandip.input;

import java.io.IOException;
import java.util.List;

import com.sandip.model.TradeInstruction;
/**
 * 
 * @author sandip.p.sangale
 *
 */
public interface TradeInstructionReader {
	List<TradeInstruction> processTradeInstructions() throws IOException;
}
