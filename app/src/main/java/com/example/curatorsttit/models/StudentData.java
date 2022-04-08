package com.example.curatorsttit.models;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentData{

	@SerializedName("Dormitory")
	private boolean dormitory;

	@SerializedName("ITN")
	private String iTN;

	@SerializedName("Passport")
	private Passport passport;

	@SerializedName("MedPolicy")
	private String medPolicy;

	@SerializedName("EducationStatus")
	private String educationStatus;

	@SerializedName("Perens")
	private List<Person> perens;

	@SerializedName("GroupNumber")
	private String groupNumber;

	@SerializedName("InsurPolicy")
	private String insurPolicy;

	@SerializedName("Birthday")
	private Date birthday;

	@SerializedName("ResidentialAddress")
	private Addresses residentialAddress;

	@SerializedName("Person")
	private Person person;

	@SerializedName("RegistrationAddress")
	private Addresses registrationAddress;


	public boolean isDormitory() {
		return dormitory;
	}

	public StudentData setDormitory(boolean dormitory) {
		this.dormitory = dormitory;
		return this;
	}

	public String getiTN() {
		return iTN;
	}

	public StudentData setiTN(String iTN) {
		this.iTN = iTN;return this;
	}

	public Passport getPassport() {
		return passport;
	}

	public StudentData setPassport(Passport passport) {
		this.passport = passport;
		return this;
	}

	public String getMedPolicy() {
		return medPolicy;
	}

	public StudentData setMedPolicy(String medPolicy) {
		this.medPolicy = medPolicy;
		return this;
	}

	public String getEducationStatus() {
		return educationStatus;
	}

	public StudentData setEducationStatus(String educationStatus) {
		this.educationStatus = educationStatus;
		return this;
	}

	public List<Person> getPerens() {
		return perens;
	}

	public StudentData setPerens(List<Person> perens) {
		this.perens = perens;
		return this;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public StudentData setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
		return this;
	}

	public String getInsurPolicy() {
		return insurPolicy;
	}

	public StudentData setInsurPolicy(String insurPolicy) {
		this.insurPolicy = insurPolicy;
		return this;
	}

	public Date getBirthday() {
		return birthday;
	}

	public StudentData setBirthday(Date birthday) {
		this.birthday = birthday;
		return this;
	}

	public Addresses getResidentialAddress() {
		return residentialAddress;
	}

	public StudentData setResidentialAddress(Addresses residentialAddress) {
		this.residentialAddress = residentialAddress;
		return this;
	}

	public Person getPerson() {
		return person;
	}

	public StudentData setPerson(Person person) {
		this.person = person;
		return this;
	}

	public Addresses getRegistrationAddress() {
		return registrationAddress;
	}

	public StudentData setRegistrationAddress(Addresses registrationAddress) {
		this.registrationAddress = registrationAddress;
		return this;
	}
}