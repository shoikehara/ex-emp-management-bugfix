package jp.co.sample.emp_management.form;

import org.springframework.web.multipart.MultipartFile;

public class InsertEmployeeForm {
	private String name;
	private MultipartFile image;
	private String gender;
	private String hireDate;
	private String mailAddress;
	private String zipCode;
	private String Address;
	private String telephone;
	private String salary;
	private String characteristics;
	private String dependentsCount;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MultipartFile getImage() {
		return image;
	}
	public void setImage(MultipartFile image) {
		this.image = image;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getHireDate() {
		return hireDate;
	}
	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getCharacteristics() {
		return characteristics;
	}
	public void setCharacteristics(String characteristics) {
		this.characteristics = characteristics;
	}
	public String getDependentsCount() {
		return dependentsCount;
	}
	public void setDependentsCount(String dependentsCount) {
		this.dependentsCount = dependentsCount;
	}
	@Override
	public String toString() {
		return "InsertEmployeeForm [name=" + name + ", img=" + image + ", gender=" + gender + ", hireDate=" + hireDate
				+ ", mailAddress=" + mailAddress + ", zipCode=" + zipCode + ", Address=" + Address + ", telephone="
				+ telephone + ", salary=" + salary + ", characteristics=" + characteristics + ", dependentsCount="
				+ dependentsCount + "]";
	}
}
