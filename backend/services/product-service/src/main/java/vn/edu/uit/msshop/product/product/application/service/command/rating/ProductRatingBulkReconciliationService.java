package vn.edu.uit.msshop.product.product.application.service.command.rating;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.ReconcileProductRatingsCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.SetAllProductRatingsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingBulkReconciliationUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingBulkUpdateUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.sync.FetchProductRatingsPort;

@Service
@RequiredArgsConstructor
public class ProductRatingBulkReconciliationService
        implements
        ProductRatingBulkReconciliationUseCase {
    private final FetchProductRatingsPort fetchPort;
    private final ProductRatingBulkUpdateUseCase setAllUseCase;

    @Override
    public void execute(
            final ReconcileProductRatingsCommand command) {
        final var ratings = fetchPort.fetchAll(
                command.rangeStartTime(),
                command.rangeEndTime());

        final var setCommand = new SetAllProductRatingsCommand(ratings);
        setAllUseCase.execute(setCommand);
    }
}
