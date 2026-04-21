package vn.uit.edu.msshop.order.adapter.in.web;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.order.adapter.out.event.repositories.CodPaymentCancelledDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.CodPaymentReceivedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.IncreaseSoldCountEventDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedSuccessDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderUpdatedRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.inventory.OrderCreatedInventoryDocumentRepository;


@Component
public class EventController {
    private OrderCreatedInventoryDocumentRepository orderCreatedInventoryDocumentRepo;
    private  OrderCreatedDocumentRepository orderCreatedDocumentRepo;
    private  OrderCreatedSuccessDocumentRepository orderCreatedSuccessDocumentRepo;
    private  EventDocumentRepository eventDocumentRepo;
    
    private CodPaymentCancelledDocumentRepository codPaymentCancelledDocumentRepo;
    private CodPaymentReceivedDocumentRepository codPaymentReceivedDocumentRepo;
    
    
    
    private IncreaseSoldCountEventDocumentRepository increaseSoldCountEventRepo;
    private OrderUpdatedRepository orderUpdatedRepo;

    public EventController(OrderCreatedInventoryDocumentRepository orderCreatedInventoryDocumentRepo, OrderCreatedDocumentRepository orderCreatedDocumentRepo,
        OrderCreatedSuccessDocumentRepository orderCreatedSuccessDocumentRepo,EventDocumentRepository eventDocumentRepo,CodPaymentCancelledDocumentRepository codPaymentCancelledRepo,
        CodPaymentReceivedDocumentRepository codPaymentReceivedRepo,
       
        IncreaseSoldCountEventDocumentRepository increaseSoldCountRepo,OrderUpdatedRepository orderUpdatedRepo
    ) {
        System.out.println("Cleareddddddddddddddddddddddddddddddd");
        orderCreatedInventoryDocumentRepo.deleteAll();
        orderCreatedDocumentRepo.deleteAll();
        orderCreatedSuccessDocumentRepo.deleteAll();
        eventDocumentRepo.deleteAll();
        codPaymentCancelledRepo.deleteAll();
        codPaymentReceivedRepo.deleteAll();
       
        increaseSoldCountRepo.deleteAll();
        orderUpdatedRepo.deleteAll();
        
        
    }

}
