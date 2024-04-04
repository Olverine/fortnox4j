package org.notima.api.fortnox.entities3;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.notima.api.fortnox.FortnoxConstants;

public class WriteOff {

	private Double	amount;
	private String	accountNumber;
	private String	costCenter;
	private String	currency;
	private String	description;
	private String	transactionInformation;
	private String	project;

	@XmlElement(name="Amount")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@XmlElement(name="AccountNumber")
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	@XmlElement(name="CostCenter")
	public String getCostCenter() {
		return costCenter;
	}
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}
	
	@XmlElement(name="Currency")
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@XmlElement(name="Description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@XmlElement(name="TransactionInformation")
	public String getTransactionInformation() {
		return transactionInformation;
	}
	public void setTransactionInformation(String transactionInformation) {
		this.transactionInformation = transactionInformation;
	}
	
	public void appendTransactionInformation(String appendTrxInfo) {
		if (transactionInformation==null || transactionInformation.trim().length()==0) {
			setTransactionInformation(appendTrxInfo);
		} else {
			transactionInformation += ", " + appendTrxInfo;
		}
	}
	
	@XmlElement(name="Project")
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	
	@XmlTransient
	public boolean isDefaultAccountingCurrency() {
		return currency==null || FortnoxConstants.DEFAULT_ACCOUNTING_CURRENCY.equalsIgnoreCase(currency);
	}
	
}
