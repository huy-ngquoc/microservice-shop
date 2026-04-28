package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface VariantInfoRepository extends MongoRepository<VariantInfo, UUID> {
    public List<VariantInfo> findByProductId(UUID productId);
    public List<VariantInfo> findByVariantIdIn(List<UUID> variantIds);
    public Page<VariantInfo> findAll(Pageable pageable);
}
