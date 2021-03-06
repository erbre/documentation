package com.erbre.documentation.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ADDRESS")
@SequenceGenerator(name = "ADDRESS_SEQ", sequenceName = "ADDRESS_ID_SEQ")
public class AddressEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADDRESS_SEQ")
	private Long id;

	private String street;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	private String city;
	@Column(name = "ZIP_CODE")
	private String zipCode;
	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID")
	private CountryEntity country;

	public CountryEntity getCountry() {
		return country;
	}

	public void setCountry(CountryEntity country) {
		this.country = country;
	}
}
