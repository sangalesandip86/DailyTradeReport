package com.sandip.model;

import java.time.LocalDate;

import com.sandip.enums.CurrencyType;
import com.sandip.enums.TransactionType;
import com.sandip.utils.TradeReportUtil;
/**
 * This class holds transaction instruction information sent by clients
 * 
 * @author sandip.p.sangale
 *
 */
public class TradeInstruction {
	private String entity;
	private TransactionType transactionType;
	private CurrencyType currencyType;
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

	public CurrencyType getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType) {
		this.currencyType = currencyType;
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

	/**
	 * 
	 * 
	 * @param entity
	 * @param transactionType
	 * @param currency
	 * @param agreedFx
	 * @param instructionDate
	 * @param settlementDate
	 * @param units
	 * @param pricePerUnit
	 */
	public TradeInstruction(String entity, TransactionType transactionType, CurrencyType currency, Double agreedFx,
			LocalDate instructionDate, LocalDate settlementDate, int units, Double pricePerUnit) {
		super();
		TradeReportUtil.shouldBeNonNegative(agreedFx, (double) units, pricePerUnit);
		this.entity = entity;
		this.transactionType = transactionType;
		this.currencyType = currency;
		this.agreedFx = agreedFx;
		this.instructionDate = instructionDate;
		this.settlementDate = settlementDate;
		this.units = units;
		this.pricePerUnit = pricePerUnit;
	}

	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

}