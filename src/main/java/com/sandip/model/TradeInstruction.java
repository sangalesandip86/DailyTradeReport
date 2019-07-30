package com.sandip.model;

import java.time.LocalDate;

import com.sandip.enums.BuySell;
import com.sandip.enums.Currency;

public class TradeInstruction {
	private String entity;
	private BuySell buySell;
	private Currency currency;
	private Double agreedFx;
	private LocalDate instructionDate;
	private LocalDate settlementDate;
	private int units;
	private Double pricePerUnit;

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Double getAgreedFx() {
		return agreedFx;
	}

	public void setAgreedFx(Double agreedFx) {
		this.agreedFx = agreedFx;
	}

	public LocalDate getInstructionDate() {
		return instructionDate;
	}

	public void setInstructionDate(LocalDate instructionDate) {
		this.instructionDate = instructionDate;
	}

	public LocalDate getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(LocalDate settlementDate) {
		this.settlementDate = settlementDate;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}

	public Double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public BuySell getBuySell() {
		return buySell;
	}

	public void setBuySell(BuySell buySell) {
		this.buySell = buySell;
	}

	/**
	 * USD amount of a trade = Price per unit * Units * Agreed Fx
	 * 
	 * @return
	 */
	public Double calculateUSDAmountOfTrade() {
		return this.pricePerUnit * units * agreedFx;
	}
}