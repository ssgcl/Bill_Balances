package com.ssgc.springbootjwt.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssgc.springbootjwt.dao.CompletedBillsBalancesRepository;
import com.ssgc.springbootjwt.model.COMPLETED_BILLS_BALANCES;

@RestController
public class BillBalancesController {

    // Inject the repository dependency to handle database operations for completed bills
    @Autowired
    private CompletedBillsBalancesRepository repository;

    // Define an endpoint to fetch bill balance details for a given account ID
    @ResponseBody
    @RequestMapping(value = "/bill", method = RequestMethod.POST)
    public HashMap<String, String> billBalanceReturn(@RequestParam String acct_id) {

        // Use LinkedHashMap to maintain the insertion order of response data
        HashMap<String, String> map = new LinkedHashMap<>();

        // Set pagination for fetching only the latest two records
        Pageable pageable = PageRequest.of(0, 2); 
        Page<COMPLETED_BILLS_BALANCES> page = repository.findByAcctId(acct_id, pageable);

        // Check if the page has any bill data
        if (page.hasContent()) {

            // Fetch the latest bill entry
            COMPLETED_BILLS_BALANCES latestBill = page.getContent().get(0);

            System.out.println("Latest Bill Completion Date: " + latestBill.getBillCompletionDate());

            // Populate the response map with details from the latest bill entry
            map.put("Response Code", "200");
            map.put("CUSTOMER NUMBER", latestBill.getAcctId());
            map.put("BILL ID", latestBill.getBillId());
            map.put("NAME", latestBill.getEntityName());
            map.put("BILLING MONTH", getBillingMonth(latestBill.getBillingMonth()));
            map.put("DUE DATE", latestBill.getDueDate() + "");
            map.put("TOTAL AMOUNT DUE", latestBill.getAmountBeforeDueDate() + "");
            map.put("AFTER DUE DATE AMOUNT", latestBill.getAmountAfterDueDate() + "");

        } else {
            // If no bills are found, return a message and a response code
            System.out.println("No bills found for account ID: " + acct_id);
            map.put("RESPONSE CODE", "420");
            map.put("Message", "No bills found for account ID: " + acct_id);
        }

        return map;  // Return the response map as JSON
    }

    // Helper method to format the billing month in "MMM YYYY" format
    private String getBillingMonth(String billMonth) {
        String billingMonth = null;

        // Extract month as an integer and match it to the month name
        switch (Integer.parseInt(billMonth.substring(4, 6))) {
            case 1:  billingMonth = "Jan " + billMonth.substring(0, 4); break;
            case 2:  billingMonth = "Feb " + billMonth.substring(0, 4); break;
            case 3:  billingMonth = "Mar " + billMonth.substring(0, 4); break;
            case 4:  billingMonth = "Apr " + billMonth.substring(0, 4); break;
            case 5:  billingMonth = "May " + billMonth.substring(0, 4); break;
            case 6:  billingMonth = "Jun " + billMonth.substring(0, 4); break;
            case 7:  billingMonth = "Jul " + billMonth.substring(0, 4); break;
            case 8:  billingMonth = "Aug " + billMonth.substring(0, 4); break;
            case 9:  billingMonth = "Sep " + billMonth.substring(0, 4); break;
            case 10: billingMonth = "Oct " + billMonth.substring(0, 4); break;
            case 11: billingMonth = "Nov " + billMonth.substring(0, 4); break;
            case 12: billingMonth = "Dec " + billMonth.substring(0, 4); break;
        }

        return billingMonth;  // Return the formatted billing month
    }
}
