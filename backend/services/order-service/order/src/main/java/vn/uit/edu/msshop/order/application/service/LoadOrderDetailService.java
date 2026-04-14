package vn.uit.edu.msshop.order.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.adapter.exception.VariantNotFoundException;
import vn.uit.edu.msshop.order.adapter.in.web.request.OrderDetailRequest;
import vn.uit.edu.msshop.order.adapter.out.persistence.VariantInfo;
import vn.uit.edu.msshop.order.adapter.out.persistence.VariantInfoRepository;
import vn.uit.edu.msshop.order.application.port.out.LoadOrderDetailPort;
import vn.uit.edu.msshop.order.domain.model.valueobject.OrderDetail;

@Service
@RequiredArgsConstructor
public class LoadOrderDetailService implements LoadOrderDetailPort  {
    private final VariantInfoRepository variantInfoRepo;
    

    @Override
    public OrderDetail loadOrderDetail(UUID variantId, int quantity) {
        VariantInfo info = variantInfoRepo.findById(variantId).orElseThrow(()->new VariantNotFoundException(variantId));

        return new OrderDetail(info.getVariantId(),info.getProductId(), info.getProductName(), info.getImageKey(), quantity , info.getPrice(), info.getTraits());
    }

    @Override
    public List<OrderDetail> loadByListDetail(List<OrderDetailRequest> requests) {
        List<VariantInfo> infos = variantInfoRepo.findByVariantIdIn(requests.stream().map(item->item.variantId()).toList());
        List<OrderDetail> result = new ArrayList<>();
        for(OrderDetailRequest orderDetail: requests) {
            VariantInfo info = findInList(orderDetail.variantId(), infos);
            if(info==null) throw new VariantNotFoundException(orderDetail.variantId());
            result.add(new OrderDetail(info.getVariantId(),info.getProductId(), info.getProductName(), info.getImageKey(), orderDetail.quantity() , info.getPrice(), info.getTraits()));
        }
        return result;

    }
    private VariantInfo findInList(UUID id , List<VariantInfo> infos) {
        for(VariantInfo i: infos) {
            if(id.equals(i.getVariantId())) return i;
        }
        return null;
    }
    
}
