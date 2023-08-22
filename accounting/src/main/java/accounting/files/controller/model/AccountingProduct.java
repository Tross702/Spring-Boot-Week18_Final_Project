package accounting.files.controller.model;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;

@Embeddable
public class AccountingProduct {
	private Long productId;
	private String name;
	private String category;
	private BigDecimal price;

	public AccountingProduct() {
	}

	public AccountingProduct(Long productId, String name, String category, BigDecimal price) {
		this.productId = productId;
		this.name = name;
		this.category = category;
		this.price = price;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
