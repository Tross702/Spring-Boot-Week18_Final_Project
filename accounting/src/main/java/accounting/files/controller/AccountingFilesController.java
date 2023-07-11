package accounting.files.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import accounting.files.controller.model.AccountingCustomer;
import accounting.files.controller.model.AccountingInvoice;
import accounting.files.controller.model.AccountingInvoiceItem;
import accounting.files.service.AccountingFilesService;

@RestController
@RequestMapping("/accounting.files") // Add base request mapping
public class AccountingFilesController {

	private final AccountingFilesService accountingFilesService;

	@Autowired
	public AccountingFilesController(AccountingFilesService accountingFilesService) {
		this.accountingFilesService = accountingFilesService;
	}

	// Get all invoices
	@GetMapping("/invoices")
	public ResponseEntity<List<AccountingInvoice>> getAllInvoices() {
		List<AccountingInvoice> invoices = accountingFilesService.getAllInvoices();
		return ResponseEntity.ok(invoices);
	}

	// Get invoice by ID
	@GetMapping("/invoices/{invoiceId}")
	public ResponseEntity<AccountingInvoice> getInvoiceById(@PathVariable String invoiceId) {
		AccountingInvoice invoice = accountingFilesService.getInvoiceById(invoiceId);
		if (invoice != null) {
			return ResponseEntity.ok(invoice);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Create a new invoice
	@PostMapping("/invoices")
	public ResponseEntity<AccountingInvoice> createInvoice(@RequestBody AccountingInvoice invoice) {
		AccountingInvoice createdInvoice = accountingFilesService.createInvoice(invoice);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
	}

	// Update an existing invoice
	@PutMapping("/invoices/{invoiceId}")
	public ResponseEntity<AccountingInvoice> updateInvoice(@PathVariable String invoiceId,
			@RequestBody AccountingInvoice invoice) {
		AccountingInvoice existingInvoice = accountingFilesService.getInvoiceById(invoiceId);
		if (existingInvoice != null) {
			invoice.setInvoiceId(existingInvoice.getInvoiceId());
			AccountingInvoice updatedInvoice = accountingFilesService.updateInvoice(invoiceId, invoice);
			return ResponseEntity.ok(updatedInvoice);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Delete an invoice
	@DeleteMapping("/invoices/{invoiceId}")
	public ResponseEntity<Void> deleteInvoice(@PathVariable String invoiceId) {
		boolean isDeleted = accountingFilesService.deleteInvoice(invoiceId);
		if (isDeleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Get items for an invoice
	@GetMapping("/invoices/{invoiceId}/items")
	public ResponseEntity<List<AccountingInvoiceItem>> getInvoiceItems(@PathVariable String invoiceId) {
		List<AccountingInvoiceItem> items = accountingFilesService.getInvoiceItems(invoiceId);
		if (items != null) {
			return ResponseEntity.ok(items);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Add an item to an invoice
	@PostMapping("/invoices/{invoiceId}/items")
	public ResponseEntity<Void> addInvoiceItem(@PathVariable String invoiceId,
			@RequestBody AccountingInvoiceItem item) {
		boolean isSuccess = accountingFilesService.addInvoiceItem(invoiceId, item);
		if (isSuccess) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Update an item in an invoice
	@PutMapping("/invoices/{invoiceId}/items/{itemId}")
	public ResponseEntity<Void> updateInvoiceItem(@PathVariable String invoiceId, @PathVariable String itemId,
			@RequestParam int quantity) {
		boolean isSuccess = accountingFilesService.updateInvoiceItem(invoiceId, itemId, quantity);
		if (isSuccess) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Delete an item from an invoice
	@DeleteMapping("/invoices/{invoiceId}/items/{itemId}")
	public ResponseEntity<Void> deleteInvoiceItem(@PathVariable String invoiceId, @PathVariable String itemId) {
		boolean isSuccess = accountingFilesService.deleteInvoiceItem(invoiceId, itemId);
		if (isSuccess) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Close an invoice
	@PutMapping("/invoices/{invoiceId}/close")
	public ResponseEntity<Void> closeInvoice(@PathVariable String invoiceId) {
		boolean isSuccess = accountingFilesService.closeInvoice(invoiceId);
		if (isSuccess) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Get all customers
	@GetMapping("/customers")
	public ResponseEntity<List<AccountingCustomer>> getAllCustomers() {
		List<AccountingCustomer> customers = accountingFilesService.getAllCustomers();
		return ResponseEntity.ok(customers);
	}

	// Get customer by ID
	@GetMapping("/customers/{customerId}")
	public ResponseEntity<AccountingCustomer> getCustomerById(@PathVariable Long customerId) {
		AccountingCustomer customer = accountingFilesService.getCustomerById(customerId);
		if (customer != null) {
			return ResponseEntity.ok(customer);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Create a new customer
	@PostMapping("/customers")
	public ResponseEntity<AccountingCustomer> createCustomer(@RequestBody AccountingCustomer customer) {
		AccountingCustomer createdCustomer = accountingFilesService.createCustomer(customer);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
	}

	// Update an existing customer
	@PutMapping("/customers/{customerId}")
	public ResponseEntity<AccountingCustomer> updateCustomer(@PathVariable Long customerId,
			@RequestBody AccountingCustomer customer) {
		AccountingCustomer existingCustomer = accountingFilesService.getCustomerById(customerId);
		if (existingCustomer != null) {
			customer.setCustomerId(existingCustomer.getCustomerId());
			AccountingCustomer updatedCustomer = accountingFilesService.updateCustomer(customerId, customer);
			return ResponseEntity.ok(updatedCustomer);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Delete a customer
	@DeleteMapping("/customers/{customerId}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
		boolean isDeleted = accountingFilesService.deleteCustomer(customerId);
		if (isDeleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
