package vn.uit.edu.msshop.order.test;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import vn.uit.edu.msshop.order.adapter.in.web.response.InventoryResponse;
import vn.uit.edu.msshop.order.adapter.remote.InventoryChecker;
//@Service
public class TestInventoryService   {
    private final MongoTemplate mongoTemplate;
    private final InventoryChecker inventoryChecker;
public List<VariantSumDto> reader(MongoTemplate mongoTemplate) {
    Aggregation aggregation = Aggregation.newAggregation(
        Aggregation.match(Criteria.where("status").in("CONFIRMED","PENDING_PAYMENT","WAITING_PAYMENT")),
        Aggregation.unwind("details"),
        Aggregation.group("details.variantId").sum("details.amount").as("amount"),
        Aggregation.project("amount").and("_id").as("variantId")
    );

    List<VariantSumDto> results = mongoTemplate.aggregate(aggregation, "orders", VariantSumDto.class)
                                               .getMappedResults();
    
    
    return results;
}
public TestInventoryService(MongoTemplate mongoTemplate, InventoryChecker checker) {
    this.inventoryChecker = checker;
    this.mongoTemplate= mongoTemplate;
    List<VariantSumDto> dtos = reader(mongoTemplate);
    Map<UUID, VariantSumDto> dtosMap= new HashMap<>();
    for(VariantSumDto dto: dtos) {
        dtosMap.put(dto.getVariantId(),dto);
    }
    List<InventoryResponse> responses = inventoryChecker.getByVariantIds(dtos.stream().map(item->item.getVariantId()).toList()).getBody();
    for(InventoryResponse response: responses) {
        if(response.getQuantity()+response.getReservedQuantity()==1000000) {
            final var dto=dtosMap.get(response.getVariantId());
            if(dto.getAmount()==response.getReservedQuantity()) {
                continue;
            }
            else {
                System.out.println("Wrong order data at "+response.getVariantId().toString());
            }

        }
        System.out.println("Wrong inventory data at "+response.getVariantId().toString());
    }
    System.out.println("Completed");
    
}
}
