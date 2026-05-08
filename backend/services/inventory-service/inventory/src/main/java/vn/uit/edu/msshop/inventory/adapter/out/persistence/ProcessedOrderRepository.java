package vn.uit.edu.msshop.inventory.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedOrderRepository extends JpaRepository<ProcessedOrder, UUID> {

}
