package com.ssgc.springbootjwt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;


@Entity
@Table(name = "refresh_token") 
public class RefreshToken {

	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private long id;
	
	 @Column(nullable = false, unique = true)
	  private String token;
   
//    @OneToOne
//    @JoinColumn(name = "username", referencedColumnName = "username")
	private String username;
    
    @Column(nullable = false)
    private Date expiryDate;

    // Getters and setters...
    public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

}
