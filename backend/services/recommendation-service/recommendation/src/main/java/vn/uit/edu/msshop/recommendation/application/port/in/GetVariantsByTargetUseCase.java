package vn.uit.edu.msshop.recommendation.application.port.in;

import java.util.List;

import vn.uit.edu.msshop.recommendation.adapter.in.web.request.PageRequestDto;
import vn.uit.edu.msshop.recommendation.adapter.in.web.response.PageResponseDto;
import vn.uit.edu.msshop.recommendation.adapter.in.web.response.VariantResponse;

public interface GetVariantsByTargetUseCase {
    public PageResponseDto<VariantResponse> getVariantResponseByTargets(List<String> targets, int pageNumber, int pageSize, String sortBy, PageRequestDto.Direction direction);
}
