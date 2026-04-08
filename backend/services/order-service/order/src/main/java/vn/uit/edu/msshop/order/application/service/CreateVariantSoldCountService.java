package vn.uit.edu.msshop.order.application.service;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.dto.command.CreateVariantSoldCountCommand;
import vn.uit.edu.msshop.order.application.dto.query.VariantSoldCountView;
import vn.uit.edu.msshop.order.application.mapper.VariantSoldCountViewMapper;
import vn.uit.edu.msshop.order.application.port.in.CreateVariantSoldCountUseCase;
import vn.uit.edu.msshop.order.application.port.out.CreateVariantSoldCountPort;
import vn.uit.edu.msshop.order.application.port.out.FindVariantSoldCountPort;
import vn.uit.edu.msshop.order.domain.model.VariantSoldCount;

@Service
@RequiredArgsConstructor
public class CreateVariantSoldCountService implements CreateVariantSoldCountUseCase {
    private final VariantSoldCountViewMapper mapper;
    private final FindVariantSoldCountPort findPort;
    private final CreateVariantSoldCountPort createPort;

    @Override
    public VariantSoldCountView create(CreateVariantSoldCountCommand command) {
        if(findPort.findById(command.id()).isPresent()) throw new RuntimeException("Document already exist");
        final var draft = VariantSoldCount.Draft.builder().id(command.id()).soldCount(command.soldCount()).build();
        final var result = createPort.create(VariantSoldCount.create(draft));
        return mapper.toView(result);
    }

}
