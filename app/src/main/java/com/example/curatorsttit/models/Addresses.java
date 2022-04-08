package com.example.curatorsttit.models;

import com.google.gson.annotations.SerializedName;

public class Addresses{

	@SerializedName("Flat")
	private String flat;

	@SerializedName("PostalCode")
	private String postalCode;

	@SerializedName("Region")
	private String region;

	@SerializedName("Street")
	private String street;

	@SerializedName("Country")
	private String country;

	@SerializedName("House")
	private String house;

	@SerializedName("Id")
	private int id;

	@SerializedName("City")
	private String city;

	public void setFlat(String flat){
		this.flat = flat;
	}

	public String getFlat(){
		return flat;
	}

	public void setPostalCode(String postalCode){
		this.postalCode = postalCode;
	}

	public String getPostalCode(){
		return postalCode;
	}

	public void setRegion(String region){
		this.region = region;
	}

	public String getRegion(){
		return region;
	}

	public void setStreet(String street){
		this.street = street;
	}

	public String getStreet(){
		return street;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setHouse(String house){
		this.house = house;
	}

	public String getHouse(){
		return house;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}
}