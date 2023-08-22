package accounting.files.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import accounting.files.controller.model.AccountingCustomer;
import accounting.files.controller.model.AccountingInvoice;
import accounting.files.controller.model.AccountingInvoiceItem;
import accounting.files.controller.model.AccountingProduct;
import accounting.files.service.AccountingFilesService;

@RestController
@RequestMapping("/accounting.files") // Add base request mapping
public class AccountingFilesController {

	private final AccountingFilesService accountingFilesService;

	@Autowired
	public AccountingFilesController(AccountingFilesService accountingFilesService) {
		this.accountingFilesService = accountingFilesService;
	}

	// Invoice EndPoints

	@GetMapping("/invoices")
	public ResponseEntity<List<AccountingInvoice>> getAllInvoices() {
		List<AccountingInvoice> invoices = accountingFilesService.getAllInvoices();
		return ResponseEntity.ok(invoices);
	}

	@GetMapping("/invoices/{invoiceId}")
	public ResponseEntity<AccountingInvoice> getInvoiceById(@PathVariable String invoiceId) {
		AccountingInvoice invoice = accountingFilesService.getInvoiceById(invoiceId);
		if (invoice != null) {
			return ResponseEntity.ok(invoice);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/invoices")
	public ResponseEntity<AccountingInvoice> createInvoice(@RequestBody AccountingInvoice invoice) {
		AccountingInvoice createdInvoice = accountingFilesService.createInvoice(invoice);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
	}

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

	@DeleteMapping("/invoices/{invoiceId}")
	public ResponseEntity<Void> deleteInvoice(@PathVariable String invoiceId) {
		boolean isDeleted = accountingFilesService.deleteInvoice(invoiceId);
		if (isDeleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/invoices/{invoiceId}/items")
	public ResponseEntity<List<AccountingInvoiceItem>> getInvoiceItems(@PathVariable String invoiceId) {
		List<AccountingInvoiceItem> items = accountingFilesService.getInvoiceItems(invoiceId);
		if (items != null) {
			return ResponseEntity.ok(items);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

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

	@DeleteMapping("/invoices/{invoiceId}/items/{itemId}")
	public ResponseEntity<Void> deleteInvoiceItem1(@PathVariable String invoiceId, @PathVariable String itemId) {
		boolean isSuccess = accountingFilesService.deleteInvoiceItem(invoiceId, itemId);
		if (isSuccess) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/invoices/{invoiceId}/close")
	public ResponseEntity<Void> closeInvoice(@PathVariable String invoiceId) {
		boolean isSuccess = accountingFilesService.closeInvoice(invoiceId);
		if (isSuccess) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/invoices/{invoiceId}/items/{itemId}")
	public ResponseEntity<AccountingInvoiceItem> getInvoiceItem(@PathVariable String invoiceId,
			@PathVariable String itemId) {
		AccountingInvoiceItem item = accountingFilesService.getInvoiceItem(invoiceId, itemId);
		if (item != null) {
			return ResponseEntity.ok(item);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/invoices/{invoiceId}/items/{itemId}/updateQuantity")
	public ResponseEntity<Void> updateInvoiceItemQuantity(@PathVariable String invoiceId, @PathVariable String itemId,
			@RequestParam int quantity) {
		boolean isSuccess = accountingFilesService.updateInvoiceItemQuantity(invoiceId, itemId, quantity);
		if (isSuccess) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/invoices/{invoiceId}/items/{itemId}/delete")
	public ResponseEntity<Void> deleteInvoiceItem(@PathVariable String invoiceId, @PathVariable String itemId) {
		boolean isSuccess = accountingFilesService.deleteInvoiceItem(invoiceId, itemId);
		if (isSuccess) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Customer EndPoints

	@GetMapping("/customers")
	public ResponseEntity<List<AccountingCustomer>> getAllCustomers() {
		List<AccountingCustomer> customers = accountingFilesService.getAllCustomers();
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

	@GetMapping("/customers/{customerId}")
	public ResponseEntity<AccountingCustomer> getCustomerById(@PathVariable Long customerId) {
		AccountingCustomer customer = accountingFilesService.getCustomerById(customerId);
		if (customer != null) {
			return new ResponseEntity<>(customer, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/customers")
	public ResponseEntity<AccountingCustomer> createCustomer(@RequestBody AccountingCustomer customer) {
		AccountingCustomer createdCustomer = accountingFilesService.createCustomer(customer);
		return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
	}

	@PutMapping("/customers/{customerId}")
	public ResponseEntity<AccountingCustomer> updateCustomer(@PathVariable Long customerId,
			@RequestBody AccountingCustomer customer) {
		AccountingCustomer updatedCustomer = accountingFilesService.updateCustomer(customerId, customer);
		if (updatedCustomer != null) {
			return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/customers/{customerId}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
		boolean deleted = accountingFilesService.deleteCustomer(customerId);
		if (deleted) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Products EndPoints

	@GetMapping("/products")
	public ResponseEntity<List<AccountingProduct>> getAllProducts() {
		List<AccountingProduct> products = accountingFilesService.getAllProducts();
		return ResponseEntity.ok(products);
	}

	@GetMapping("/products/{productId}")
	public ResponseEntity<AccountingProduct> getProductById(@PathVariable Long productId) {
		AccountingProduct product = accountingFilesService.getProductById(productId);
		if (product != null) {
			return ResponseEntity.ok(product);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/products")
	public ResponseEntity<AccountingProduct> createProduct(@RequestBody AccountingProduct product) {
		AccountingProduct createdProduct = accountingFilesService.createProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
	}

	@PutMapping("/products/{productId}")
	public ResponseEntity<AccountingProduct> updateProduct(@PathVariable Long productId,
			@RequestBody AccountingProduct product) {
		AccountingProduct existingProduct = accountingFilesService.getProductById(productId);
		if (existingProduct != null) {
			product.setProductId(existingProduct.getProductId());
			AccountingProduct updatedProduct = accountingFilesService.updateProduct(productId, product);
			return ResponseEntity.ok(updatedProduct);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/products/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
		boolean isDeleted = accountingFilesService.deleteProduct(productId);
		if (isDeleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// CustomerInvoice EndPoints

	@PostMapping("/customer-invoice")
	public ResponseEntity<Void> assignCustomerToInvoice(@RequestParam Long customerId, @RequestParam String invoiceId) {
		boolean isSuccess = accountingFilesService.assignCustomerToInvoice(customerId, invoiceId);
		if (isSuccess) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/customers/{customerId}/invoices")
	public ResponseEntity<List<AccountingInvoice>> getInvoicesForCustomer(@PathVariable Long customerId) {
		List<AccountingInvoice> invoices = accountingFilesService.getInvoicesForCustomer(customerId);
		return ResponseEntity.ok(invoices);
	}

	@GetMapping("/invoices/{invoiceId}/customers")
	public ResponseEntity<List<AccountingCustomer>> getCustomersForInvoice(@PathVariable String invoiceId) {
		List<AccountingCustomer> customers = accountingFilesService.getCustomersForInvoice(invoiceId);
		return ResponseEntity.ok(customers);
	}

	@PutMapping("/customer-invoice/{customerId}/{invoiceId}")
	public ResponseEntity<Void> updateCustomerInvoiceRelationship(@PathVariable Long customerId,
			@PathVariable String invoiceId) {
		boolean isSuccess = accountingFilesService.updateCustomerInvoiceRelationship(customerId, invoiceId);
		if (isSuccess) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/customer-invoice/{customerId}/{invoiceId}")
	public ResponseEntity<Void> removeCustomerInvoiceRelationship(@PathVariable Long customerId,
			@PathVariable String invoiceId) {
		boolean isSuccess = accountingFilesService.removeCustomerInvoiceRelationship(customerId, invoiceId);
		if (isSuccess) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
