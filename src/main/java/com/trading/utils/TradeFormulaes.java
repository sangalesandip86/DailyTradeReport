package com.trading.utils;

import java.util.function.ToDoubleFunction;

import com.trading.model.TradeInstruction;

public class TradeFormulaes {
	/**
	 * USD amount of a tradeAmountInUSD = Price per unit * Units * Agreed Fx
	 */
	public static final ToDoubleFunction<TradeInstruction> TRADE_AMOUNT_IN_USD = t -> (t.getPricePerUnit()
			* t.getUnits() * t.getAgreedFx());

	private TradeFormulaes() {
		// Private Constructor
	}
}
