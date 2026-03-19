package vn.uit.edu.msshop.recommendation.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.recommendation.application.dto.command.CreateVariantCommand;
import vn.uit.edu.msshop.recommendation.application.dto.query.VariantView;
import vn.uit.edu.msshop.recommendation.application.mapper.VariantViewMapper;
import vn.uit.edu.msshop.recommendation.application.port.in.CreateVariantUseCase;
import vn.uit.edu.msshop.recommendation.application.port.out.LoadVariantPort;
import vn.uit.edu.msshop.recommendation.application.port.out.SaveVariantPort;
import vn.uit.edu.msshop.recommendation.domain.model.Variant;

@Service
@RequiredArgsConstructor
public class CreateVariantService implements CreateVariantUseCase {
    private final SaveVariantPort savePort;
    private final LoadVariantPort loadPort;
    private final VariantViewMapper mapper;

    @Override
    public VariantView create(CreateVariantCommand command) {
        
        try {
            loadPort.loadById(command.id());
        }
        catch(Exception e) {
            final var d = Variant.Draft.builder().id(command.id()).name(command.name()).targets(command.targets()).images(command.images()).build();
            final var result = savePort.save(Variant.create(d));
            return mapper.toView(result);
        }
        throw new RuntimeException("Invalid command");
    }

    
}
