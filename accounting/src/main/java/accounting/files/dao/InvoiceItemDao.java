package accounting.files.dao;

import accounting.files.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceItemDao extends JpaRepository<InvoiceItem, Long> {
}
