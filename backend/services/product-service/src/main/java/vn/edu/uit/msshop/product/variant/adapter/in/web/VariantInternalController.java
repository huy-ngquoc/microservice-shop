package vn.edu.uit.msshop.product.variant.adapter.in.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.in.web.mapper.VariantWebMapper;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.FindVariantsByIdsRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantResponse;
import vn.edu.uit.msshop.product.variant.application.port.in.query.lookup.VariantActiveBulkLookupByIdsUseCase;

@RestController
@RequestMapping("/variants")
@RequiredArgsConstructor
public class VariantInternalController {
    private final VariantActiveBulkLookupByIdsUseCase activeBulkLookupByIdsUseCase;
    private final VariantWebMapper mapper;

    @PostMapping("/order-search")
    public ResponseEntity<List<VariantResponse>> findAllByIds(
            @RequestBody
            @Valid
            FindVariantsByIdsRequest request) {
        // TODO: create "query" record
        // final var variantIds = this.mapper.toVariantIds(request);
        final var variantById = this.activeBulkLookupByIdsUseCase.findAllByIds(request.ids());

        final var responses = this.mapper.toListResponse(variantById.values());
        return ResponseEntity.ok(responses);
    }
}
