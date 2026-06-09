package vn.edu.uit.msshop.product.product.application.service.command.rating;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.rating.ReconcileProductRatingsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.rating.SetAllProductRatingsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingBulkReconciliationUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingBulkUpdateUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductRatingBulkFetchPort;

@Service
@RequiredArgsConstructor
public class ProductRatingBulkReconciliationService
        implements
        ProductRatingBulkReconciliationUseCase {
    private final ProductRatingBulkFetchPort ratingBulkFetchPort;
    private final ProductRatingBulkUpdateUseCase ratingBulkUpdateUseCase;

    @Override
    public void execute(
            final ReconcileProductRatingsCommand command) {
        final var ratings = ratingBulkFetchPort.fetchAll(
                command.rangeStartTime(),
                command.rangeEndTime());

        final var setCommand = new SetAllProductRatingsCommand(ratings);
        ratingBulkUpdateUseCase.execute(setCommand);
    }
}
