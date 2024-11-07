package com.ssgc.springbootjwt.dao;

import org.springframework.data.domain.Page; // Import for pagination support
import org.springframework.data.domain.Pageable; // Import for Pageable object
import org.springframework.data.jpa.repository.JpaRepository; // Import for JPA repository
import org.springframework.data.jpa.repository.Query; // Import for defining custom queries
import org.springframework.data.repository.query.Param; // Import for parameterized queries
import org.springframework.stereotype.Repository; // Import for repository annotation

import com.ssgc.springbootjwt.model.COMPLETED_BILLS_BALANCES; // Import for the model class

/**
 * Repository interface for accessing COMPLETED_BILLS_BALANCES data.
 * It extends JpaRepository to provide CRUD operations.
 */
@Repository
public interface CompletedBillsBalancesRepository extends JpaRepository<COMPLETED_BILLS_BALANCES, Long> {

	/**
     * Finds completed bills balances by account ID with pagination.
     *
     * @param acctId the account ID to search for
     * @param pageable pagination information
     * @return a page of completed bills balances associated with the given account ID
     */
	@Query("SELECT cbb FROM COMPLETED_BILLS_BALANCES cbb WHERE cbb.ACCOUNT_ID = :ACCOUNT_ID ORDER BY cbb.bill_completion_date DESC")
	Page<COMPLETED_BILLS_BALANCES> findByAcctId(@Param("ACCOUNT_ID") String acctId, Pageable pageable);
}
