package com.ssgc.springbootjwt.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COMPLETED_BILLS_BALANCES" )
public class COMPLETED_BILLS_BALANCES {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPLETED_BILLS_BALANCES_ID", nullable = false)
    private Long completed_bills_balances_id; // Assuming there's an ID field
    
    @Column(name = "ACCT_ID") 
    private String ACCOUNT_ID;
    @Column(name = "entity_name") 
    private String CUSTOMER_NAME;
    @Column(name = "bill_id") 
    private String BILL_ID;
    @Column(name = "billing_month") 
    private String BILLING_MONTH;
    @Column(name = "amount_before_due_date")
    private BigDecimal AMOUNT_BEFORE_DUE_DATE;
    @Column(name = "amount_after_due_date")
    private BigDecimal AMOUNT_AFTER_DUE_DATE;
    @Column(name = "due_date")
    private Date DUE_DATE;
    @Column(name = "BILL_COMPLETION_DATE")
    private Date bill_completion_date;
   // private Date record_inserted_date;

    public String getEntityName() {
		return CUSTOMER_NAME;
	}
	public void setEntityName(String entityName) {
		this.CUSTOMER_NAME = entityName;
	}
//	public Date getInserted_date() {
//		return record_inserted_date;
//	}
//	public void setInserted_date(Date inserted_date) {
//		this.record_inserted_date = inserted_date;
//	}
	// getters and setters
	public String getAcctId() {
		return ACCOUNT_ID;
	}
	public void setAcctId(String acctId) {
		this.ACCOUNT_ID = acctId;
	}
	public String getBillId() {
		return BILL_ID;
	}
	public void setBillId(String billId) {
		this.BILL_ID = billId;
	}
	public String getBillingMonth() {
		return BILLING_MONTH;
	}
	public void setBillingMonth(String billingMonth) {
		this.BILLING_MONTH = billingMonth;
	}
	public BigDecimal getAmountBeforeDueDate() {
		return AMOUNT_BEFORE_DUE_DATE;
	}
	public void setAmountBeforeDueDate(BigDecimal amountBeforeDueDate) {
		this.AMOUNT_BEFORE_DUE_DATE = amountBeforeDueDate;
	}
	public BigDecimal getAmountAfterDueDate() {
		return AMOUNT_AFTER_DUE_DATE;
	}
	public void setAmountAfterDueDate(BigDecimal amountAfterDueDate) {
		this.AMOUNT_AFTER_DUE_DATE = amountAfterDueDate;
	}
	public Date getDueDate() {
		return DUE_DATE;
	}
	public void setDueDate(Date dueDate) {
		this.DUE_DATE = dueDate;
	}
	
	 public Date getBillCompletionDate() {
			return bill_completion_date;
	}
	public void setBillCompletionDate(Date billCompletionDate) {
		this.bill_completion_date = billCompletionDate;
	}

   
}