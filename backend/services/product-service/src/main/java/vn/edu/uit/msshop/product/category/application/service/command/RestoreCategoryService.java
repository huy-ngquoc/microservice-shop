package vn.edu.uit.msshop.product.category.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.command.RestoreCategoryCommand;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.port.in.command.RestoreCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.event.PublishCategoryEventPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadSoftDeletedCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.domain.event.CategoryRestored;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class RestoreCategoryService implements RestoreCategoryUseCase {
  private final LoadSoftDeletedCategoryPort loadCategoryPort;
  private final UpdateCategoryPort updateCategoryPort;
  private final PublishCategoryEventPort eventPort;

  @Override
  @Transactional
  public void restore(final RestoreCategoryCommand command) {
    final var categoryId = command.id();
    final var category = this.loadCategoryPort.loadSoftDeletedById(categoryId)
        .orElseThrow(() -> new CategoryNotFoundException(categoryId));

    final var expectedVersion = command.expectedVersion();
    final var currentVersion = category.getVersion();
    if (!expectedVersion.equals(currentVersion)) {
      throw new OptimisticLockException(expectedVersion.value(), currentVersion.value());
    }

    final var next = new Category(category.getId(), category.getName(), category.getImageKey(),
        category.getVersion(), null);

    final var saved = this.updateCategoryPort.update(next);
    this.eventPort.publish(new CategoryRestored(saved.getId()));
  }

}
