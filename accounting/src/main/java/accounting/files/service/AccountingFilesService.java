package accounting.files.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import accounting.files.controller.model.AccountingCustomer;
import accounting.files.controller.model.AccountingInvoice;
import accounting.files.controller.model.AccountingInvoiceItem;
import accounting.files.dao.CustomerDao;
import accounting.files.dao.InvoiceDao;
import accounting.files.dao.InvoiceItemDao;
import accounting.files.entity.Customer;
import accounting.files.entity.Invoice;
import accounting.files.entity.InvoiceItem;

@Service
public class AccountingFilesService {

	private final InvoiceDao invoiceDao;
	private final CustomerDao customerDao;
	@SuppressWarnings("unused")
	private final InvoiceItemDao invoiceItemDao;

	@Autowired
	public AccountingFilesService(InvoiceDao invoiceDao, CustomerDao customerDao, InvoiceItemDao invoiceItemDao) {
		this.invoiceDao = invoiceDao;
		this.customerDao = customerDao;
		this.invoiceItemDao = invoiceItemDao;
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
		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			return convertToAccountingCustomer(customer);
		}
		return null;
	}

	public AccountingCustomer createCustomer(AccountingCustomer accountingCustomer) {
		Customer customer = convertToEntity(accountingCustomer);
		customer = customerDao.save(customer);
		return convertToAccountingCustomer(customer);
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
		if (invoiceOptional.isPresent()) {
			Invoice invoice = invoiceOptional.get();
			return convertToAccountingInvoice(invoice);
		}
		return null;
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

	// Invoice Item methods

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

	// Conversion methods

	private AccountingInvoice convertToAccountingInvoice(Invoice invoice) {
		AccountingInvoice accountingInvoice = new AccountingInvoice();
		accountingInvoice.setInvoiceId(invoice.getInvoiceId().toString());
		accountingInvoice.setCustomer(invoice.getCustomer());
		accountingInvoice.setInvoiceDate(invoice.getInvoiceDate());
		accountingInvoice.setTotalAmount(invoice.getTotalAmount());
		accountingInvoice.setItems(getInvoiceItems(invoice.getInvoiceId().toString()));
		return accountingInvoice;
	}

	private AccountingInvoiceItem convertToAccountingInvoiceItem(InvoiceItem item) {
		AccountingInvoiceItem accountingItem = new AccountingInvoiceItem();
		accountingItem.setItemId(item.getItemId().toString());
		accountingItem.setProductId(item.getProductId());
		accountingItem.setQuantity(item.getQuantity());
		return accountingItem;
	}

	private Invoice convertToEntity(AccountingInvoice accountingInvoice) {
		Invoice invoice = new Invoice();
		String invoiceId = accountingInvoice.getInvoiceId();
		if (invoiceId != null && invoiceId.matches("\\d+")) {
			invoice.setInvoiceId(Long.parseLong(invoiceId));
		} else {
			// Handle the case when the invoiceId is not valid
		}
		invoice.setCustomer(accountingInvoice.getCustomer());
		invoice.setInvoiceDate(accountingInvoice.getInvoiceDate());
		invoice.setTotalAmount(accountingInvoice.getTotalAmount());
		return invoice;
	}

	private InvoiceItem convertToEntity(AccountingInvoiceItem accountingItem) {
		InvoiceItem item = new InvoiceItem();
		item.setItemId(Long.parseLong(accountingItem.getItemId()));
		item.setProductId(accountingItem.getProductId());
		item.setQuantity(accountingItem.getQuantity());
		return item;
	}

	private AccountingCustomer convertToAccountingCustomer(Customer customer) {
		AccountingCustomer accountingCustomer = new AccountingCustomer();
		accountingCustomer.setCustomerId(customer.getCustomerId());
		accountingCustomer.setFirstName(customer.getFirstName());
		accountingCustomer.setLastName(customer.getLastName());
		accountingCustomer.setEmail(customer.getEmail());
		return accountingCustomer;
	}

	private Customer convertToEntity(AccountingCustomer accountingCustomer) {
		Customer customer = new Customer();
		customer.setCustomerId(accountingCustomer.getCustomerId());
		customer.setFirstName(accountingCustomer.getFirstName());
		customer.setLastName(accountingCustomer.getLastName());
		customer.setEmail(accountingCustomer.getEmail());
		return customer;
	}

	public AccountingInvoice updateInvoice(AccountingInvoice invoice) {
		Optional<Invoice> existingInvoiceOptional = invoiceDao.findById(Long.parseLong(invoice.getInvoiceId()));
		if (existingInvoiceOptional.isPresent()) {
			Invoice existingInvoice = existingInvoiceOptional.get();
			existingInvoice.setCustomer(invoice.getCustomer());
			existingInvoice.setInvoiceDate(invoice.getInvoiceDate());
			existingInvoice.setTotalAmount(invoice.getTotalAmount());
			existingInvoice.setItems(convertToEntityItemList(invoice.getItems()));
			existingInvoice.setClosed(invoice.isClosed());
			existingInvoice = invoiceDao.save(existingInvoice);
			return convertToAccountingInvoice(existingInvoice);
		}
		return null;
	}

	private List<InvoiceItem> convertToEntityItemList(List<AccountingInvoiceItem> accountingItems) {
		List<InvoiceItem> items = new ArrayList<>();
		for (AccountingInvoiceItem accountingItem : accountingItems) {
			InvoiceItem item = convertToEntity(accountingItem);
			items.add(item);
		}
		return items;
	}
}