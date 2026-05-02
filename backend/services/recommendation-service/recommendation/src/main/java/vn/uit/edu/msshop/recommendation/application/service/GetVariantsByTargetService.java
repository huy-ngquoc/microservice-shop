package vn.uit.edu.msshop.recommendation.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.recommendation.adapter.in.web.request.PageRequestDto;
import vn.uit.edu.msshop.recommendation.adapter.in.web.response.PageResponseDto;
import vn.uit.edu.msshop.recommendation.adapter.in.web.response.VariantResponse;
import vn.uit.edu.msshop.recommendation.adapter.remote.ProductCaller;
import vn.uit.edu.msshop.recommendation.application.port.in.GetVariantsByTargetUseCase;

@Service
@RequiredArgsConstructor
public class GetVariantsByTargetService implements GetVariantsByTargetUseCase {
    private final ProductCaller productCaller;

    @Override
    public PageResponseDto<VariantResponse> getVariantResponseByTargets(List<String> targets, int pageNumber, int pageSize, String sortBy, PageRequestDto.Direction direction) {
        return productCaller.list(pageNumber, pageSize, sortBy, direction, targets).getBody();
    }

}
