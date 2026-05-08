package vn.uit.edu.msshop.order.application.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.exception.VariantNotFoundException;
import vn.uit.edu.msshop.order.adapter.in.web.request.FindVariantsByIdsRequest;
import vn.uit.edu.msshop.order.adapter.in.web.request.OrderDetailRequest;
import vn.uit.edu.msshop.order.adapter.in.web.response.VariantResponse;
import vn.uit.edu.msshop.order.adapter.out.persistence.VariantInfo;
import vn.uit.edu.msshop.order.adapter.out.persistence.VariantInfoRepository;
import vn.uit.edu.msshop.order.adapter.remote.VariantChecker;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderDetailPort;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;

@Service
@RequiredArgsConstructor
public class LoadOrderDetailService implements LoadOrderDetailPort  {
    private final VariantInfoRepository variantInfoRepo;
    private final VariantChecker variantChecker;
    

    @Override
    public OrderDetail loadOrderDetail(UUID variantId, int quantity) {
        VariantInfo info = variantInfoRepo.findById(variantId).orElseThrow(()->new VariantNotFoundException(variantId));

        return new OrderDetail(info.getVariantId(),info.getProductId(), info.getProductName(), info.getImageKey(), quantity , info.getPrice(), info.getTraits());
    }

    @Override
    public List<OrderDetail> loadByListDetail(List<OrderDetailRequest> requests) {
       Set<UUID> variantIds =new HashSet<>(requests.stream().map(item->item.variantId()).toList());
       List<VariantResponse> responses = variantChecker.findAllByIds(new FindVariantsByIdsRequest(variantIds)).getBody();
       Map<UUID,VariantResponse> responseMap = new HashMap<>();
       List<OrderDetail> result = new ArrayList<>();
       for(VariantResponse response: responses) {
        responseMap.put(response.id(), response);
       }
       for(OrderDetailRequest detailRequest:requests) {
        VariantResponse response = responseMap.get(detailRequest.variantId());
        if(detailRequest==null) {
            throw new VariantNotFoundException(detailRequest.variantId());
        }
        result.add(toOrderDetail(response, detailRequest.quantity()));

       }
       return result;

    }
    private OrderDetail toOrderDetail(VariantResponse response, int amount) {
        return new OrderDetail(response.id(), response.productId(),response.productName(), response.imageKey(), amount,response.price(), response.traits());
    }
    
}
