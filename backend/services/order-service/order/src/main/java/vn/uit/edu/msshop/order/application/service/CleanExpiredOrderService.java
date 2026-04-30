package vn.uit.edu.msshop.order.application.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.out.persistence.OrderRepository;
import vn.uit.edu.msshop.order.adapter.out.persistence.mapper.OrderDataMapper;
import vn.uit.edu.msshop.order.application.port.in.CleanExpiredOrderPort;
import vn.uit.edu.msshop.order.domain.model.Order;

@Service
@RequiredArgsConstructor
public class CleanExpiredOrderService implements CleanExpiredOrderPort {
    private final OrderRepository orderRepo;
    private final OrderDataMapper mapper;
    private final CheckOrderService checkOrderService;
    
    @Override
    //@Scheduled(fixedRate=6000)
    public void clean() {
        Instant threshold=Instant.now().minus(15, ChronoUnit.MINUTES);
        //System.out.println("Call schedule clean order");
        List<Order> orders = orderRepo.findTop50ByStatusAndPaymentMethodAndUpdateAtBefore("WAITING_PAYMENT","ONLINE", threshold).stream().map(mapper::toDomain).toList();
        for(Order order:orders) {
            checkOrderService.checkAndUpdate(order);
        }
   
    

    }
    

}
