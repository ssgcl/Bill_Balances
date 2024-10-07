package com.ssgc.springbootjwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ssgc.springbootjwt.dao.CompletedBillsBalancesRepository;
import com.ssgc.springbootjwt.model.COMPLETED_BILLS_BALANCES;

@Service
public class BillBalancesService {

    @Autowired
    private CompletedBillsBalancesRepository repository;

    public Page<COMPLETED_BILLS_BALANCES> getBillBalancesByAcctId(String acctId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByAcctId(acctId, pageRequest);
    }
}
