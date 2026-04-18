package vn.uit.edu.msshop.order.adapter.in.web;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.order.adapter.out.event.repositories.CodPaymentCancelledDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.CodPaymentReceivedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.IncreaseSoldCountEventDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedSuccessDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.inventory.OrderCancelledDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.inventory.OrderCreatedInventoryDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.inventory.OrderShippedDocumentRepository;

@Component
public class EventController {
    private OrderCreatedInventoryDocumentRepository orderCreatedInventoryDocumentRepo;
    private  OrderCreatedDocumentRepository orderCreatedDocumentRepo;
    private  OrderCreatedSuccessDocumentRepository orderCreatedSuccessDocumentRepo;
    private  EventDocumentRepository eventDocumentRepo;
    
    private CodPaymentCancelledDocumentRepository codPaymentCancelledDocumentRepo;
    private CodPaymentReceivedDocumentRepository codPaymentReceivedDocumentRepo;
    
    
    private OrderCancelledDocumentRepository orderCancelledDocumentRepo;
    private OrderShippedDocumentRepository orderShippedDocumentRepository;
    private IncreaseSoldCountEventDocumentRepository increaseSoldCountEventRepo;

    public EventController(OrderCreatedInventoryDocumentRepository orderCreatedInventoryDocumentRepo, OrderCreatedDocumentRepository orderCreatedDocumentRepo,
        OrderCreatedSuccessDocumentRepository orderCreatedSuccessDocumentRepo,EventDocumentRepository eventDocumentRepo,CodPaymentCancelledDocumentRepository codPaymentCancelledRepo,
        CodPaymentReceivedDocumentRepository codPaymentReceivedRepo,
        OrderCancelledDocumentRepository orderCancelledRepo,
        OrderShippedDocumentRepository orderShippedRepo,
        IncreaseSoldCountEventDocumentRepository increaseSoldCountRepo
    ) {
        System.out.println("Cleareddddddddddddddddddddddddddddddd");
        orderCreatedInventoryDocumentRepo.deleteAll();
        orderCreatedDocumentRepo.deleteAll();
        orderCreatedSuccessDocumentRepo.deleteAll();
        eventDocumentRepo.deleteAll();
        codPaymentCancelledRepo.deleteAll();
        codPaymentReceivedRepo.deleteAll();
        orderCancelledRepo.deleteAll();
        orderShippedRepo.deleteAll();
        increaseSoldCountRepo.deleteAll();
        
        
    }

}
