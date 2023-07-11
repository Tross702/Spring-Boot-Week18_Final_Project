package accounting.files.controller.model;

public class AccountingInvoiceItem {
	private String itemId;
	private String productId;
	private int quantity;
	private String invoice_invoiceId;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getInvoice_invoiceId() {
		return invoice_invoiceId;
	}

	public void setInvoice_invoiceId(String invoice_invoiceId) {
		this.invoice_invoiceId = invoice_invoiceId;
	}
}
