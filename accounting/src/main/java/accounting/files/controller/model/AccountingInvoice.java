package accounting.files.controller.model;

import java.util.Date;
import java.util.List;

import accounting.files.entity.Customer;

public class AccountingInvoice {
	private String invoiceId;
	private Customer customer;
	private Date invoiceDate;
	private double totalAmount;
	private List<AccountingInvoiceItem> items;
	private boolean closed;

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<AccountingInvoiceItem> getItems() {
		return items;
	}

	public void setItems(List<AccountingInvoiceItem> items) {
		this.items = items;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public boolean isPaid() {
		return totalAmount > 0.0;

	}
}
