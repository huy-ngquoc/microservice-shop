package vn.edu.uit.msshop.product.brand.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataBrandJpaRepository
        extends JpaRepository<BrandJpaEntity, UUID> {
}
