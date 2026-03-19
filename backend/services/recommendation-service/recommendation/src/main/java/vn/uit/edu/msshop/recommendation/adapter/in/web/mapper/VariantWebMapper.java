package vn.uit.edu.msshop.recommendation.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.recommendation.adapter.in.web.response.VariantResponse;
import vn.uit.edu.msshop.recommendation.application.dto.command.CreateVariantCommand;
import vn.uit.edu.msshop.recommendation.application.dto.command.DeleteVariantCommand;
import vn.uit.edu.msshop.recommendation.application.dto.command.UpdateVariantCommand;
import vn.uit.edu.msshop.recommendation.application.dto.query.VariantView;
import vn.uit.edu.msshop.recommendation.domain.event.CreateVariantEvent;
import vn.uit.edu.msshop.recommendation.domain.event.DeleteVariantEvent;
import vn.uit.edu.msshop.recommendation.domain.event.UpdateVariantEvent;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantId;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantImages;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantName;
import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantTargets;

@Component
public class VariantWebMapper {
    public CreateVariantCommand toCommand(CreateVariantEvent event) {
        return new CreateVariantCommand(new VariantId(event.getVariantId()), new VariantImages(event.getImages()), new VariantName(event.getName()), new VariantTargets(event.getTargets()));
    }
    public UpdateVariantCommand toCommand(UpdateVariantEvent event) {
        return new UpdateVariantCommand(new VariantId(event.getVariantId()),new VariantImages(event.getImages()), new VariantName(event.getName()), new VariantTargets(event.getTargets()));
    }
    public VariantResponse toResponse(VariantView view) {
        return new VariantResponse(view.variantId(), view.images(), view.targets(), view.name());
    }
    public DeleteVariantCommand toCommand(DeleteVariantEvent event) {
        return new DeleteVariantCommand(new VariantId(event.getVariantId()));
    }
}
