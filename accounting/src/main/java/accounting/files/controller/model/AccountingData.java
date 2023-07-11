package accounting.files.controller.model;

import java.util.List;

public class AccountingData {
	private AccountingInvoice invoice;
	private List<AccountingInvoiceItem> items;

	public AccountingInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(AccountingInvoice invoice) {
		this.invoice = invoice;
	}

	public List<AccountingInvoiceItem> getItems() {
		return items;
	}

	public void setItems(List<AccountingInvoiceItem> items) {
		this.items = items;
	}
}