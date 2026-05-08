package vn.uit.edu.msshop.order.adapter.in.web;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.order.adapter.out.event.repositories.EventDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderCreatedSuccessDocumentRepository;
import vn.uit.edu.msshop.order.adapter.out.event.repositories.OrderUpdatedRepository;



@Component
public class EventController {
    
    private  OrderCreatedDocumentRepository orderCreatedDocumentRepo;
    private  OrderCreatedSuccessDocumentRepository orderCreatedSuccessDocumentRepo;
    private  EventDocumentRepository eventDocumentRepo;
    
    
    
    
    
    
    private OrderUpdatedRepository orderUpdatedRepo;

    public EventController( OrderCreatedDocumentRepository orderCreatedDocumentRepo,
        OrderCreatedSuccessDocumentRepository orderCreatedSuccessDocumentRepo,EventDocumentRepository eventDocumentRepo,
        
       
        OrderUpdatedRepository orderUpdatedRepo
    ) {
        System.out.println("Cleareddddddddddddddddddddddddddddddd");
        
        orderCreatedDocumentRepo.deleteAll();
        orderCreatedSuccessDocumentRepo.deleteAll();
        eventDocumentRepo.deleteAll();
       
       
        orderUpdatedRepo.deleteAll();
        
        
    }

}
