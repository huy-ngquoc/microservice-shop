package vn.edu.uit.msshop.product.product.application.service.command.rating;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.data.ProductRatingData;
import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingBulkReconciliationCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingBulkUpdateCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingBulkReconciliationUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingBulkUpdateUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductRatingBulkFetchPort;

@Service
@RequiredArgsConstructor
public class ProductRatingBulkReconciliationService
        implements ProductRatingBulkReconciliationUseCase {
    private final ProductRatingBulkFetchPort ratingBulkFetchPort;
    private final ProductRatingBulkUpdateUseCase ratingBulkUpdateUseCase;

    @Override
    public void execute(
            final ProductRatingBulkReconciliationCommand cmd) {
        final var snapshots = this.ratingBulkFetchPort.fetchAll(
                cmd.rangeStartTime(),
                cmd.rangeEndTime());

        final var ratingDataList = new ArrayList<ProductRatingData>(snapshots.size());
        for (final var snapshot : snapshots) {
            final var ratingData = new ProductRatingData(
                    snapshot.productId().value(),
                    snapshot.total().value(),
                    snapshot.amount().value());
            ratingDataList.add(ratingData);
        }

        final var updateCommand = new ProductRatingBulkUpdateCommand(ratingDataList);
        this.ratingBulkUpdateUseCase.execute(updateCommand);
    }
}
