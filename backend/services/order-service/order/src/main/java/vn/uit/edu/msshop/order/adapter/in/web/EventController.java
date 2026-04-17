package vn.uit.edu.msshop.order.adapter.in.web;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.order.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedSuccessDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.inventory.OrderCreatedInventoryDocumentRepository;

@Component
public class EventController {
    private OrderCreatedInventoryDocumentRepository orderCreatedInventoryDocumentRepo;
    private  OrderCreatedDocumentRepository orderCreatedDocumentRepo;
    private  OrderCreatedSuccessDocumentRepository orderCreatedSuccessDocumentRepo;
    private  EventDocumentRepository eventDocumentRepo;

    public EventController(OrderCreatedInventoryDocumentRepository orderCreatedInventoryDocumentRepo, OrderCreatedDocumentRepository orderCreatedDocumentRepo,
        OrderCreatedSuccessDocumentRepository orderCreatedSuccessDocumentRepo,EventDocumentRepository eventDocumentRepo
    ) {
        System.out.println("Cleareddddddddddddddddddddddddddddddd");
        orderCreatedInventoryDocumentRepo.deleteAll();
        orderCreatedDocumentRepo.deleteAll();
        orderCreatedSuccessDocumentRepo.deleteAll();
        eventDocumentRepo.deleteAll();
        
        
    }

}
