package com.ssgc.springbootjwt.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssgc.springbootjwt.model.COMPLETED_BILLS_BALANCES;



@Repository
public interface CompletedBillsBalancesRepository extends JpaRepository<COMPLETED_BILLS_BALANCES, Long> {
	//@Query("SELECT acct_id, bill_id, billing_month, bill_completion_date, due_date, amount_before_due_date, amount_after_due_date FROM BILLS_BALANCES cbb WHERE cbb.acct_id = :accountId  AND ROWNUM<2 ORDER BY cbb.bill_completion_date DESC ")
	//Page<BILLS_BALANCES> findByAcctIdOrderByBillCompletionDateDesc(@Param("accountId") String accountId, Pageable pageable);

	
	@Query("SELECT cbb FROM COMPLETED_BILLS_BALANCES cbb WHERE cbb.ACCOUNT_ID = :ACCOUNT_ID AND cbb.BILLING_MONTH = :BILLING_MONTH  ORDER BY cbb.bill_completion_date DESc")
	Page<COMPLETED_BILLS_BALANCES> findByAcctId(@Param("ACCOUNT_ID") String acctId, @Param("BILLING_MONTH") String billingMnth, Pageable pageable);
}
