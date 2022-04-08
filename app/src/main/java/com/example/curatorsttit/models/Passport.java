package com.example.curatorsttit.models;

import com.google.gson.annotations.SerializedName;

public class Passport{

	@SerializedName("Series")
	private String series;


	@SerializedName("RegistrationAddressId")
	private int registrationAddressId;

	@SerializedName("Number")
	private String number;

	@SerializedName("IssueDate")
	private String issueDate;

	@SerializedName("Id")
	private int id;

	@SerializedName("IssuingAuthority")
	private String issuingAuthority;

	@SerializedName("SubdivisionCode")
	private String subdivisionCode;

	public void setSeries(String series){
		this.series = series;
	}

	public String getSeries(){
		return series;
	}

	public void setRegistrationAddressId(int registrationAddressId){
		this.registrationAddressId = registrationAddressId;
	}

	public int getRegistrationAddressId(){
		return registrationAddressId;
	}

	public void setNumber(String number){
		this.number = number;
	}

	public String getNumber(){
		return number;
	}

	public void setIssueDate(String issueDate){
		this.issueDate = issueDate;
	}

	public String getIssueDate(){
		return issueDate;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setIssuingAuthority(String issuingAuthority){
		this.issuingAuthority = issuingAuthority;
	}

	public String getIssuingAuthority(){
		return issuingAuthority;
	}

	public void setSubdivisionCode(String subdivisionCode){
		this.subdivisionCode = subdivisionCode;
	}

	public String getSubdivisionCode(){
		return subdivisionCode;
	}
}