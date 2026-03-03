package vn.edu.uit.msshop.product.category.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataCategoryJpaRepository
        extends JpaRepository<CategoryJpaEntity, UUID> {
}
