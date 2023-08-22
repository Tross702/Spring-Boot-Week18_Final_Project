package accounting.files.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false, unique = true)
	private String email;

	@OneToMany(mappedBy = "customer")
	private List<Invoice> relatedInvoices = new ArrayList<>();

	// Constructors, getters, and setters

	public Customer() {
	}

	public Customer(String firstName, String lastName, String email) {
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

	public List<Invoice> getRelatedInvoices() {
		return relatedInvoices;
	}

	public void setRelatedInvoices(List<Invoice> relatedInvoices) {
		this.relatedInvoices = relatedInvoices;
	}

	public boolean contains(Customer customer) {
		for (Invoice invoice : relatedInvoices) {
			if (invoice.getCustomer().equals(customer)) {
				return true;
			}
		}
		return false;
	}

	public void add(Invoice invoice) {
		relatedInvoices.add(invoice);
		invoice.setCustomer(this);
	}

	public List<Invoice> getInvoices() {
		return relatedInvoices;
	}

	public void remove(Invoice invoice) {
		relatedInvoices.remove(invoice);
		invoice.setCustomer(null);
	}

	public void add(Customer customer) {
		if (!this.equals(customer) && !relatedInvoices.containsAll(customer.getRelatedInvoices())) {
			for (Invoice invoice : customer.getRelatedInvoices()) {
				invoice.setCustomer(this);
				relatedInvoices.add(invoice);
			}
		}
	}

	public void remove(Customer customer) {
		if (!this.equals(customer) && relatedInvoices.containsAll(customer.getRelatedInvoices())) {
			for (Invoice invoice : customer.getRelatedInvoices()) {
				invoice.setCustomer(null);
				relatedInvoices.remove(invoice);
			}
		}
	}
}
