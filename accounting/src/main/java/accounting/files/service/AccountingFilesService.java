package accounting.files.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import accounting.files.controller.model.AccountingCustomer;
import accounting.files.controller.model.AccountingInvoice;
import accounting.files.controller.model.AccountingInvoiceItem;
import accounting.files.controller.model.AccountingProduct;
import accounting.files.dao.CustomerDao;
import accounting.files.dao.InvoiceDao;
import accounting.files.dao.ProductDao;
import accounting.files.entity.Customer;
import accounting.files.entity.Invoice;
import accounting.files.entity.InvoiceItem;
import accounting.files.entity.Product;

@Service
public class AccountingFilesService {

	private final InvoiceDao invoiceDao;
	private final CustomerDao customerDao;
	private final ProductDao productDao;

	@Autowired
	public AccountingFilesService(InvoiceDao invoiceDao, CustomerDao customerDao, ProductDao productDao) {
		this.invoiceDao = invoiceDao;
		this.customerDao = customerDao;
		this.productDao = productDao;
	}

	// Customer methods

	public List<AccountingCustomer> getAllCustomers() {
		List<Customer> customers = customerDao.findAll();
		List<AccountingCustomer> accountingCustomers = new ArrayList<>();
		for (Customer customer : customers) {
			accountingCustomers.add(convertToAccountingCustomer(customer));
		}
		return accountingCustomers;
	}

	public AccountingCustomer getCustomerById(Long customerId) {
		Optional<Customer> customerOptional = customerDao.findById(customerId);
		return customerOptional.map(this::convertToAccountingCustomer).orElse(null);
	}

	public AccountingCustomer createCustomer(AccountingCustomer accountingCustomer) {
		Customer customer = convertToEntity(accountingCustomer);
		customer = customerDao.save(customer);
		return convertToAccountingCustomer(customer);
	}

	private Customer convertToEntity(AccountingCustomer accountingCustomer) {
		Customer customer = new Customer();
		customer.setCustomerId(accountingCustomer.getCustomerId());
		customer.setFirstName(accountingCustomer.getFirstName());
		customer.setLastName(accountingCustomer.getLastName());
		customer.setEmail(accountingCustomer.getEmail());
		return customer;
	}

	public AccountingCustomer updateCustomer(Long customerId, AccountingCustomer accountingCustomer) {
		Optional<Customer> existingCustomerOptional = customerDao.findById(customerId);
		if (existingCustomerOptional.isPresent()) {
			Customer existingCustomer = existingCustomerOptional.get();
			Customer updatedCustomer = convertToEntity(accountingCustomer);
			updatedCustomer.setCustomerId(existingCustomer.getCustomerId());
			updatedCustomer = customerDao.save(updatedCustomer);
			return convertToAccountingCustomer(updatedCustomer);
		}
		return null;
	}

	public boolean deleteCustomer(Long customerId) {
		Optional<Customer> customerOptional = customerDao.findById(customerId);
		if (customerOptional.isPresent()) {
			customerDao.deleteById(customerId);
			return true;
		}
		return false;
	}

	// Invoice methods

	public List<AccountingInvoice> getAllInvoices() {
		List<Invoice> invoices = invoiceDao.findAll();
		List<AccountingInvoice> accountingInvoices = new ArrayList<>();
		for (Invoice invoice : invoices) {
			accountingInvoices.add(convertToAccountingInvoice(invoice));
		}
		return accountingInvoices;
	}

	public AccountingInvoice getInvoiceById(String invoiceId) {
		Optional<Invoice> invoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));
		return invoiceOptional.map(this::convertToAccountingInvoice).orElse(null);
	}

	public AccountingInvoice createInvoice(AccountingInvoice accountingInvoice) {
		Invoice invoice = convertToEntity(accountingInvoice);
		invoice = invoiceDao.save(invoice);
		return convertToAccountingInvoice(invoice);
	}

	public AccountingInvoice updateInvoice(String invoiceId, AccountingInvoice accountingInvoice) {
		Optional<Invoice> existingInvoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));
		if (existingInvoiceOptional.isPresent()) {
			Invoice existingInvoice = existingInvoiceOptional.get();
			Invoice updatedInvoice = convertToEntity(accountingInvoice);
			updatedInvoice.setInvoiceId(existingInvoice.getInvoiceId());
			updatedInvoice = invoiceDao.save(updatedInvoice);
			return convertToAccountingInvoice(updatedInvoice);
		}
		return null;
	}

	public boolean deleteInvoice(String invoiceId) {
		Optional<Invoice> invoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));
		if (invoiceOptional.isPresent()) {
			invoiceDao.deleteById(Long.parseLong(invoiceId));
			return true;
		}
		return false;
	}

	public List<AccountingInvoiceItem> getInvoiceItems(String invoiceId) {
		Optional<Invoice> invoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));
		if (invoiceOptional.isPresent()) {
			Invoice invoice = invoiceOptional.get();
			List<AccountingInvoiceItem> accountingItems = new ArrayList<>();
			for (InvoiceItem item : invoice.getItems()) {
				accountingItems.add(convertToAccountingInvoiceItem(item));
			}
			return accountingItems;
		}
		return null;
	}

	public boolean addInvoiceItem(String invoiceId, AccountingInvoiceItem accountingItem) {
		Optional<Invoice> invoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));
		if (invoiceOptional.isPresent()) {
			Invoice invoice = invoiceOptional.get();
			InvoiceItem item = convertToEntity(accountingItem);
			item.setInvoice(invoice);
			invoice.getItems().add(item);
			invoiceDao.save(invoice);
			return true;
		}
		return false;
	}

	public boolean updateInvoiceItem(String invoiceId, String itemId, int quantity) {
		Optional<Invoice> invoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));
		if (invoiceOptional.isPresent()) {
			Invoice invoice = invoiceOptional.get();
			List<InvoiceItem> items = invoice.getItems();
			for (InvoiceItem item : items) {
				if (item.getItemId().equals(Long.parseLong(itemId))) {
					item.setQuantity(quantity);
					invoiceDao.save(invoice);
					return true;
				}
			}
		}
		return false;
	}

	public boolean deleteInvoiceItem(String invoiceId, String itemId) {
		Optional<Invoice> invoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));
		if (invoiceOptional.isPresent()) {
			Invoice invoice = invoiceOptional.get();
			List<InvoiceItem> items = invoice.getItems();
			items.removeIf(item -> item.getItemId().equals(Long.parseLong(itemId)));
			invoiceDao.save(invoice);
			return true;
		}
		return false;
	}

	public boolean closeInvoice(String invoiceId) {
		Optional<Invoice> invoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));
		if (invoiceOptional.isPresent()) {
			Invoice invoice = invoiceOptional.get();
			if (!invoice.isClosed()) {
				invoice.setClosed(true);
				invoiceDao.save(invoice);
				return true;
			}
		}
		return false;
	}

	public AccountingInvoiceItem getInvoiceItem(String invoiceId, String itemId) {
		Optional<Invoice> invoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));
		if (invoiceOptional.isPresent()) {
			Invoice invoice = invoiceOptional.get();
			for (InvoiceItem item : invoice.getItems()) {
				if (item.getItemId().equals(Long.parseLong(itemId))) {
					return convertToAccountingInvoiceItem(item);
				}
			}
		}
		return null;
	}

	public boolean updateInvoiceItemQuantity(String invoiceId, String itemId, int quantity) {
		Optional<Invoice> invoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));
		if (invoiceOptional.isPresent()) {
			Invoice invoice = invoiceOptional.get();
			for (InvoiceItem item : invoice.getItems()) {
				if (item.getItemId().equals(Long.parseLong(itemId))) {
					item.setQuantity(quantity);
					invoiceDao.save(invoice);
					return true;
				}
			}
		}
		return false;
	}

	// Product methods

	public List<AccountingProduct> getAllProducts() {
		List<Product> products = productDao.findAll();
		List<AccountingProduct> accountingProducts = new ArrayList<>();
		for (Product product : products) {
			accountingProducts.add(convertToAccountingProduct(product));
		}
		return accountingProducts;
	}

	public AccountingProduct getProductById(Long productId) {
		Optional<Product> productOptional = productDao.findById(productId);
		return productOptional.map(this::convertToAccountingProduct).orElse(null);
	}

	public AccountingProduct createProduct(AccountingProduct accountingProduct) {
		Product product = convertToEntity(accountingProduct);
		product = productDao.save(product);
		return convertToAccountingProduct(product);
	}

	public AccountingProduct updateProduct(Long productId, AccountingProduct accountingProduct) {
		Optional<Product> existingProductOptional = productDao.findById(productId);
		if (existingProductOptional.isPresent()) {
			Product existingProduct = existingProductOptional.get();
			Product updatedProduct = convertToEntity(accountingProduct);
			updatedProduct.setProductId(existingProduct.getProductId());
			updatedProduct = productDao.save(updatedProduct);
			return convertToAccountingProduct(updatedProduct);
		}
		return null;
	}

	public boolean deleteProduct(Long productId) {
		Optional<Product> productOptional = productDao.findById(productId);
		if (productOptional.isPresent()) {
			productDao.deleteById(productId);
			return true;
		}
		return false;
	}

	// Financial Reports

	public double calculateTotalRevenue() {
		double totalRevenue = 0.0;
		List<AccountingInvoice> allInvoices = getAllInvoices();
		for (AccountingInvoice invoice : allInvoices) {
			totalRevenue += invoice.getTotalAmount();
		}
		return totalRevenue;
	}

	public BigDecimal calculateTotalExpenses() {
		BigDecimal totalExpenses = BigDecimal.ZERO;
		List<AccountingInvoice> allInvoices = getAllInvoices();
		for (AccountingInvoice invoice : allInvoices) {
			totalExpenses = totalExpenses.add(calculateInvoiceExpenses(invoice));
		}
		return totalExpenses;
	}

	private BigDecimal calculateInvoiceExpenses(AccountingInvoice invoice) {
		BigDecimal totalExpenses = BigDecimal.ZERO;
		for (AccountingInvoiceItem item : invoice.getItems()) {
			AccountingProduct product = getProductById(item.getProductId());
			if (product != null) {
				BigDecimal price = product.getPrice();
				BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
				totalExpenses = totalExpenses.add(price.multiply(quantity));
			}
		}
		return totalExpenses;
	}

	public BigDecimal calculateTotalAssets() {
		BigDecimal totalAssets = BigDecimal.ZERO;
		List<AccountingCustomer> allCustomers = getAllCustomers();
		for (AccountingCustomer customer : allCustomers) {
			totalAssets = totalAssets.add(calculateCustomerTotalAssets(customer));
		}
		return totalAssets;
	}

	public BigDecimal calculateNetIncome() {
		BigDecimal totalRevenue = BigDecimal.valueOf(calculateTotalRevenue());
		BigDecimal totalExpenses = calculateTotalExpenses();
		return totalRevenue.subtract(totalExpenses);
	}

	private BigDecimal calculateCustomerTotalAssets(AccountingCustomer customer) {
		BigDecimal totalAssets = BigDecimal.ZERO;
		List<AccountingInvoice> customerInvoices = getInvoicesForCustomer(customer.getCustomerId());
		for (AccountingInvoice invoice : customerInvoices) {
			totalAssets = totalAssets.add(calculateInvoiceAssets(invoice));
		}
		return totalAssets;
	}

	private BigDecimal calculateInvoiceAssets(AccountingInvoice invoice) {
		BigDecimal invoiceAssets = BigDecimal.ZERO;
		for (AccountingInvoiceItem item : invoice.getItems()) {
			AccountingProduct product = getProductById(item.getProductId());
			if (product != null) {
				BigDecimal price = product.getPrice();
				BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
				invoiceAssets = invoiceAssets.add(price.multiply(quantity));
			}
		}
		return invoiceAssets;
	}

	private AccountingProduct getProductById(String productId) {
		Optional<AccountingProduct> productOptional = productDao.findById(productId);
		return productOptional.orElse(null);
	}

	private BigDecimal calculateUnpaidInvoicesLiabilities() {
		BigDecimal totalLiabilities = BigDecimal.ZERO;
		List<AccountingInvoice> unpaidInvoices = getUnpaidInvoices();
		for (AccountingInvoice invoice : unpaidInvoices) {
			BigDecimal invoiceTotal = BigDecimal.valueOf(invoice.getTotalAmount());
			totalLiabilities = totalLiabilities.add(invoiceTotal);
		}
		return totalLiabilities;
	}

	private List<AccountingInvoice> getUnpaidInvoices() {
	    List<AccountingInvoice> unpaidInvoices = new ArrayList<>();
	    List<AccountingInvoice> allInvoices = getAllInvoices();
	    for (AccountingInvoice invoice : allInvoices) {
	        if (!invoice.isPaid()) {
	            unpaidInvoices.add(invoice);
	        }
	    }
	    return unpaidInvoices;
	}


	public double calculateTotalLiabilities() {
		BigDecimal totalLiabilities = calculateUnpaidInvoicesLiabilities();
		return totalLiabilities.doubleValue();

	}

	public BigDecimal calculateTotalEquity() {
		BigDecimal totalAssets = calculateTotalAssets();
		BigDecimal totalLiabilities = BigDecimal.valueOf(calculateTotalLiabilities());

		return totalAssets.subtract(totalLiabilities);
	}

	public String generateBalanceSheet() {
		StringBuilder balanceSheet = new StringBuilder();
		balanceSheet.append("Balance Sheet\n");
		balanceSheet.append("---------------\n");
		balanceSheet.append("Assets: ").append(calculateTotalAssets()).append("\n");
		balanceSheet.append("Liabilities: ").append(calculateTotalLiabilities()).append("\n");
		balanceSheet.append("Equity: ").append(calculateTotalEquity()).append("\n");
		balanceSheet.append("---------------\n");
		return balanceSheet.toString();
	}

	public String generateIncomeStatement() {
		StringBuilder incomeStatement = new StringBuilder();
		incomeStatement.append("Income Statement\n");
		incomeStatement.append("---------------\n");
		incomeStatement.append("Total Revenue: ").append(calculateTotalRevenue()).append("\n");
		incomeStatement.append("Total Expenses: ").append(calculateTotalExpenses()).append("\n");
		incomeStatement.append("Net Income: ").append(calculateNetIncome()).append("\n");
		incomeStatement.append("---------------\n");
		return incomeStatement.toString();
	}

	public String generateCashFlowStatement() {
		// TODO:  
		// Tracking cash flow from operations, investing, and
		// financing activities
		return "";
	}

	// Helper methods for conversion between Entity and Accounting models

	private AccountingCustomer convertToAccountingCustomer(Customer customer) {
		return new AccountingCustomer(customer.getCustomerId(), customer.getFirstName(), customer.getLastName(),
				customer.getEmail());
	}

	private Customer convertToEntity(Customer customer) {
		return new Customer(customer.getFirstName(), customer.getLastName(), customer.getEmail());
	}

	private AccountingInvoice convertToAccountingInvoice(Invoice invoice) {
		List<AccountingInvoiceItem> accountingItems = new ArrayList<>();
		for (InvoiceItem item : invoice.getItems()) {
			accountingItems.add(convertToAccountingInvoiceItem(item));
		}
		return new AccountingInvoice();
	}

	private Invoice convertToEntity(AccountingInvoice accountingInvoice) {
		Invoice invoice = new Invoice();
		if (accountingInvoice.getInvoiceId() != null) {
			invoice.setInvoiceId(Long.parseLong(accountingInvoice.getInvoiceId()));
		}
		invoice.setCustomer(convertToEntity(accountingInvoice.getCustomer()));
		invoice.setInvoiceDate(accountingInvoice.getInvoiceDate());
		invoice.setTotalAmount(accountingInvoice.getTotalAmount());
		invoice.setClosed(accountingInvoice.isClosed());
		return invoice;
	}

	private AccountingInvoiceItem convertToAccountingInvoiceItem(InvoiceItem item) {
		return new AccountingInvoiceItem();
	}

	private InvoiceItem convertToEntity(AccountingInvoiceItem accountingItem) {
		InvoiceItem item = new InvoiceItem();
		if (accountingItem.getItemId() != null) {
			item.setItemId(Long.parseLong(accountingItem.getItemId()));
		}
		item.setProductId(Long.parseLong(accountingItem.getProductId()));
		item.setQuantity(accountingItem.getQuantity());
		return item;
	}

	private AccountingProduct convertToAccountingProduct(Product product) {
		return new AccountingProduct(product.getProductId(), product.getName(), product.getCategory(),
				product.getPrice());
	}

	private Product convertToEntity(AccountingProduct accountingProduct) {
		return new Product();
	}

	public boolean assignCustomerToInvoice(Long customerId, String invoiceId) {
		Optional<Customer> customerOptional = customerDao.findById(customerId);
		Optional<Invoice> invoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));

		if (customerOptional.isPresent() && invoiceOptional.isPresent()) {
			Customer customer = customerOptional.get();
			Invoice invoice = invoiceOptional.get();

			// Check if the customer is already assigned to the invoice
			if (!invoice.getCustomer().contains(customer)) {
				invoice.getCustomer().add(customer);
				invoiceDao.save(invoice);
				return true;
			}
		}
		return false;
	}

	public List<AccountingInvoice> getInvoicesForCustomer(Long customerId) {
		Optional<Customer> customerOptional = customerDao.findById(customerId);
		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			List<AccountingInvoice> accountingInvoices = new ArrayList<>();
			for (Invoice invoice : customer.getInvoices()) {
				accountingInvoices.add(convertToAccountingInvoice(invoice));
			}
			return accountingInvoices;
		}
		return null;
	}

	public boolean updateCustomerInvoiceRelationship(Long customerId, String invoiceId) {
		Optional<Customer> customerOptional = customerDao.findById(customerId);
		Optional<Invoice> invoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));

		if (customerOptional.isPresent() && invoiceOptional.isPresent()) {
			Customer customer = customerOptional.get();
			Invoice invoice = invoiceOptional.get();

			// Check if the customer is already assigned to the invoice
			if (!invoice.getCustomer().contains(customer)) {
				invoice.getCustomer().add(customer);
				invoiceDao.save(invoice);
				return true;
			}
		}
		return false;
	}

	public List<AccountingCustomer> getCustomersForInvoice(String invoiceId) {
		Optional<Invoice> invoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));
		if (invoiceOptional.isPresent()) {
			Invoice invoice = invoiceOptional.get();
			List<AccountingCustomer> accountingCustomers = new ArrayList<>();
			for (Customer customer : invoice.getCustomers()) {
				accountingCustomers.add(convertToAccountingCustomer(customer));
			}
			return accountingCustomers;
		}
		return null;
	}

	public boolean removeCustomerInvoiceRelationship(Long customerId, String invoiceId) {
		Optional<Customer> customerOptional = customerDao.findById(customerId);
		Optional<Invoice> invoiceOptional = invoiceDao.findById(Long.parseLong(invoiceId));

		if (customerOptional.isPresent() && invoiceOptional.isPresent()) {
			Customer customer = customerOptional.get();
			Invoice invoice = invoiceOptional.get();

			// Check if the customer is assigned to the invoice
			if (invoice.getCustomer().contains(customer)) {
				invoice.getCustomer().remove(customer);
				invoiceDao.save(invoice);
				return true;
			}
		}
		return false;
	}

}
