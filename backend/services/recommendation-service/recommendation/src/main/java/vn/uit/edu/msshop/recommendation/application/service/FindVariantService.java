package vn.uit.edu.msshop.recommendation.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.recommendation.application.dto.query.VariantView;
import vn.uit.edu.msshop.recommendation.application.mapper.VariantViewMapper;
import vn.uit.edu.msshop.recommendation.application.port.in.FindVariantUseCase;
import vn.uit.edu.msshop.recommendation.application.port.out.LoadVariantPort;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantId;
@Service
@RequiredArgsConstructor
public class FindVariantService implements FindVariantUseCase {
    private final LoadVariantPort loadPort;
    private final VariantViewMapper mapper;
    @Override
    public VariantView findById(VariantId id) {
        final var result = loadPort.loadById(id);
        return mapper.toView(result);
    }

    @Override
    public List<VariantView> findByTarget(String age, String gender, String shape, String bodyShape) {
       final var result = loadPort.loadByTarget(age, gender, shape, bodyShape);
       return result.stream().map(mapper::toView).toList();
    }

}
