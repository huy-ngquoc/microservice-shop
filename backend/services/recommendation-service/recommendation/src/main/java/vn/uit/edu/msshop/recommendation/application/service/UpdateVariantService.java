package vn.uit.edu.msshop.recommendation.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.recommendation.application.dto.command.UpdateVariantCommand;
import vn.uit.edu.msshop.recommendation.application.dto.query.VariantView;
import vn.uit.edu.msshop.recommendation.application.mapper.VariantViewMapper;
import vn.uit.edu.msshop.recommendation.application.port.in.UpdateVariantUseCase;
import vn.uit.edu.msshop.recommendation.application.port.out.LoadVariantPort;
import vn.uit.edu.msshop.recommendation.application.port.out.SaveVariantPort;
import vn.uit.edu.msshop.recommendation.domain.model.Variant;

@Service
@RequiredArgsConstructor
public class UpdateVariantService implements UpdateVariantUseCase {
    private final VariantViewMapper mapper;
    private final LoadVariantPort loadPort;
    private final SaveVariantPort savePort;

    @Override
    public VariantView update(UpdateVariantCommand command) {
        final var toUpdate =loadPort.loadById(command.id());
        final var u = Variant.UpdateInfo.builder().id(toUpdate.getId())
        .name(command.name())
        .images(command.images())
        .targets(command.targets())
        .build();
        final var toSave = savePort.save(toUpdate.applyUpdateInfo(u));
        return mapper.toView(toSave);
    }

    
}
