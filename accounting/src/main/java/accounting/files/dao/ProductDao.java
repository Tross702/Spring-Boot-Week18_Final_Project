package accounting.files.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import accounting.files.controller.model.AccountingProduct;
import accounting.files.entity.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {

	Optional<AccountingProduct> findById(String productId);

}
