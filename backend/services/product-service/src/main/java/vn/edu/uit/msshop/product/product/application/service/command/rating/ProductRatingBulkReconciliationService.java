package vn.edu.uit.msshop.product.product.application.service.command.rating;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingBulkReconciliationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingBulkUpdateCommand;
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
            final ProductRatingBulkReconciliationCommand command) {
        final var ratings = ratingBulkFetchPort.fetchAll(
                command.rangeStartTime(),
                command.rangeEndTime());

        final var setCommand = new ProductRatingBulkUpdateCommand(ratings);
        ratingBulkUpdateUseCase.execute(setCommand);
    }
}
