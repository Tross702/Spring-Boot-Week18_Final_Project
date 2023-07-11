package accounting.files.controller.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class AccountingCustomer {
	private Long customerId;
	private String firstName;
	private String lastName;
	private String email;

	public AccountingCustomer() {
	}

	public AccountingCustomer(Long customerId, String firstName, String lastName, String email) {
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
