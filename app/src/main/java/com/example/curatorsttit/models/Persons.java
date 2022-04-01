package com.example.curatorsttit.models;

import com.google.gson.annotations.SerializedName;

public class Persons{

	@SerializedName("Email")
	private String email;

	@SerializedName("FirstName")
	private String firstName;

	@SerializedName("Phone")
	private String phone;

	@SerializedName("Id")
	private int id;

	@SerializedName("LastName")
	private String lastName;

	@SerializedName("MiddleName")
	private String middleName;

	public Persons(String email, String firstName, String phone, int id, String lastName, String middleName) {
		this.email = email;
		this.firstName = firstName;
		this.phone = phone;
		this.id = id;
		this.lastName = lastName;
		this.middleName = middleName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setMiddleName(String middleName){
		this.middleName = middleName;
	}

	public String getMiddleName(){
		return middleName;
	}
}