package com.ssgc.springbootjwt.controller;


import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssgc.springbootjwt.dao.CompletedBillsBalancesRepository;
import com.ssgc.springbootjwt.model.COMPLETED_BILLS_BALANCES;
import com.ssgc.springbootjwt.service.BillBalancesService;


@RestController

public class BillBalancesController {
	@Autowired
	private CompletedBillsBalancesRepository repository;  // Autowire the repository
	
	@ResponseBody
	@RequestMapping(value = "/bill", method = RequestMethod.POST)
	public HashMap<String, String> billBalanceReturn(@RequestParam String acct_id, @RequestParam String billingMnth){
		
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		  
		System.out.println("acct_id:"+acct_id);
		System.out.println();
		//PageRequest pageRequest = PageRequest.of(0, 1); // page = 0, size = 1
		//System.out.println("repository.findByAcctIdOrderByBillCompletionDateDesc(acct_id, pageRequest):" +repository.findByAcctId(acct_id, pageRequest));
		Pageable pageable = PageRequest.of(0, 1); 
		Page<COMPLETED_BILLS_BALANCES> page = repository.findByAcctId(acct_id, billingMnth, pageable);
		// Check if the page has content
		System.out.println("page.getSize(): "+page.getNumberOfElements());
		
	        if (page.hasContent()) {
	        	COMPLETED_BILLS_BALANCES latestBill = page.getContent().get(0);
	           System.out.println("Latest Bill Completion Date: " + latestBill.getBillCompletionDate());
	           map.put("Response Code", "200");
	           map.put("CUSTOMER NUMBER",latestBill.getAcctId());
	           map.put("BILL ID",latestBill.getBillId());
	           map.put("NAME", latestBill.getEntityName());
	           map.put("BILLING MONTH", latestBill.getBillingMonth());
	           map.put("DUE DATE",latestBill.getDueDate()+"");          
	           map.put("TOTAL AMOUNT DUE", latestBill.getAmountBeforeDueDate()+"");
	           map.put("AFTER DUE DATE AMOUNT", latestBill.getAmountAfterDueDate()+"");
	          // return map;
	        } else {
	            System.out.println("No bills found for account ID: " + acct_id);
	            map.put("RESPONSE CODE", "420");
	            map.put("Message","No bills found for account ID: " + acct_id);           
	        }
				
		
        return map;

//		String oracleDriverName = "oracle.jdbc.driver.OracleDriver"; 
//	    String oracleURL = "jdbc:oracle:thin:@10.1.5.72:1521:ORCL";	
//	    String oracleUsername = "naima";   
//	    String oraclePassword = "12345678";
//	    Connection conn = null;
//	    HashMap<String, String> map = new LinkedHashMap<String, String>();
//		
//		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//			conn = DriverManager.getConnection(oracleURL, oracleUsername, oraclePassword);
//			
//			Statement stmt = conn.createStatement();
//		    ResultSet rs;
//		    ResultSet resultSet;
//		    
//		    String query = "SELECT ACCT_ID, BILL_ID, BILLING_MONTH, AMOUNT_BEFORE_DUE_DATE, AMOUNT_AFTER_DUE_DATE, DUE_DATE\r\n" + 
//		    		"FROM\r\n" + 
//		    		"(SELECT ACCT_ID, BILL_ID, BILLING_MONTH, AMOUNT_BEFORE_DUE_DATE, AMOUNT_AFTER_DUE_DATE, DUE_DATE \r\n" + 
//		    		"FROM COMPLETED_BILLS_BALANCES\r\n" + 
//		    		"WHERE ACCT_ID = '"+ acct_id +"' \r\n" + 
//		    		"ORDER BY BILL_COMPLETION_DATE DESC ) \r\n" + 
//		    		"WHERE rownum = 1";
//		    stmt = conn.createStatement();
//    		resultSet = stmt.executeQuery(query);
//    		if(resultSet.isBeforeFirst()){
//    		while (resultSet.next()) {				  			
//    				
//    				map.put("Response Code", "420");
//    				map.put("ACCOUNT ID", resultSet.getString("ACCT_ID"));
//					map.put("BILL ID", resultSet.getString("BILL_ID"));
//					map.put("BILLING MONTH", resultSet.getString("BILLING_MONTH"));
//					map.put("AMOUNT BEFORE DUE DATE", resultSet.getString("AMOUNT_BEFORE_DUE_DATE"));
//					map.put("AMOUNT AFTER DUE DATE", resultSet.getString("AMOUNT_AFTER_DUE_DATE"));
//					map.put("DUE DATE", resultSet.getString("DUE_DATE"));
//					
//					System.out.println(map);
//					
////					csvFileWrite(request.getRemoteAddr(), acct_id , fa_type_cd , complainant_phone , complaint_source , comments , date1 + ""  , false + "" , "420" , "" , "A complaint is already outstanding. Please await resolution or contact SSGC customer service at 1199.", complaint_reason);   					 
//					
//    		}}else{
//    			map.put("Response Code", "420");
//				map.put("Message", "Account bill is not available");
//    		}
//    			
//    			
//    			return ResponseEntity.status(HttpStatus.OK).body(map);
//    		
//		}catch(SQLException e){
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			
//		}
//		
//		return null;
		
	}
	
//	    @Autowired
//	    private BillBalancesService billBalancesService;
//	  	@ResponseBody
//		@RequestMapping(value = "/billbalances", method = RequestMethod.POST)
//	  //  @PostMapping("/billbalances")
//	    public void getBillBalances(
//	        @RequestParam String acctId) {
//	        
//	    	Page<COMPLETED_BILLS_BALANCES> page = billBalancesService.getBillBalancesByAcctId(acctId, 1, 1);
//	    	if (page.hasContent()) {
//	        	COMPLETED_BILLS_BALANCES latestBill = page.getContent().get(0);
//	            System.out.println("Latest Bill Completion Date: " + latestBill.getBillCompletionDate());
//	            //return latestBill;
//	        } else {
//	            System.out.println("No bills found for account ID: " + acctId);
//	        }
//	         
//	    }
	
}
