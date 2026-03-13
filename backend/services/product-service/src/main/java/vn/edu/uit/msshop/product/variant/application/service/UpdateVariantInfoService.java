package vn.edu.uit.msshop.product.variant.application.service;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.application.dto.Change;
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantInfoCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.UpdateVariantInfoUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.SaveVariantPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantUpdated;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTraits;

@Service
@RequiredArgsConstructor
public class UpdateVariantInfoService implements UpdateVariantInfoUseCase {
    private final LoadVariantPort loadPort;
    private final SaveVariantPort savePort;
    private final PublishVariantEventPort eventPort;

    @Override
    public void updateInfo(
            final UpdateVariantInfoCommand command) {
        final var priceSet = command.price().getSet();
        final var traitsSet = command.traits().getSet();

        if ((priceSet == null) && (traitsSet == null)) {
            return;
        }

        final var variant = this.loadPort.loadById(command.id())
                .orElseThrow(() -> new VariantNotFoundException(command.id()));

        final var next = this.applyChanges(variant, priceSet, traitsSet);
        if (next == null) {
            return;
        }

        final var saved = this.savePort.save(next);
        this.eventPort.publish(new VariantUpdated(saved.getId()));
    }

    private @Nullable Variant applyChanges(
            final Variant current,
            final Change.@Nullable Set<VariantPrice> priceSet,
            final Change.@Nullable Set<VariantTraits> traitsSet) {
        final VariantPrice newPrice;
        final boolean priceUnchanged;
        if ((priceSet != null) && !priceSet.value().equals(current.getPrice())) {
            newPrice = priceSet.value();
            priceUnchanged = false;
        } else {
            newPrice = current.getPrice();
            priceUnchanged = true;
        }

        final VariantTraits newTraits;
        final boolean traitsUnchanged;
        if ((traitsSet != null) && !traitsSet.value().equals(current.getTraits())) {
            newTraits = traitsSet.value();
            traitsUnchanged = false;
        } else {
            newTraits = current.getTraits();
            traitsUnchanged = true;
        }

        if (priceUnchanged && traitsUnchanged) {
            return null;
        }

        return new Variant(
                current.getId(),
                current.getProductId(),
                current.getImageKey(),
                newPrice,
                current.getSold(),
                newTraits);
    }
}
