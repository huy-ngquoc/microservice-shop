package vn.edu.uit.msshop.product.product.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.dto.command.SetAllProductRatingsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.SetAllProductRatingsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateAllProductRatingsPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.sync.ProductRatingSnapshot;

@Service
@RequiredArgsConstructor
@Slf4j
public class SetAllProductRatingsService
        implements SetAllProductRatingsUseCase {
    private final UpdateAllProductRatingsPort updateAllPort;

    @Override
    @Transactional
    public void execute(
            final SetAllProductRatingsCommand command) {
        final var ratings = command.ratings();
        if (ratings.isEmpty()) {
            return;
        }

        final var next = ratings.stream()
                .map(SetAllProductRatingsService::toNext)
                .toList();
        this.updateAllPort.updateAll(next);
    }

    private static ProductRating toNext(
            final ProductRatingSnapshot snapshot) {
        return new ProductRating(
                snapshot.productId(),
                snapshot.total(),
                snapshot.amount());
    }
}
