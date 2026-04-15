package vn.edu.uit.msshop.product.shared.application.port.out;

import vn.edu.uit.msshop.product.shared.event.document.ProductCreatedDocument;
import vn.edu.uit.msshop.product.shared.event.document.ProductDeletedDocument;
import vn.edu.uit.msshop.product.shared.event.document.ProductUpdateDocument;
import vn.edu.uit.msshop.product.shared.event.document.VariantDeletedDocument;
import vn.edu.uit.msshop.product.shared.event.document.VariantPurgeDocument;
import vn.edu.uit.msshop.product.shared.event.document.VariantRestoreDocument;
import vn.edu.uit.msshop.product.shared.event.document.VariantUpdateDocument;

public interface PublishProductEventPort {
    public void publishProductCreated(ProductCreatedDocument eventDocument);
    public void publishProductUpdated(ProductUpdateDocument eventDocument);
    public void publishVariantUpdated(VariantUpdateDocument eventDocument);
    public void publishVariantDeleted(VariantDeletedDocument eventDocument);
    public void publishProductDeleted(ProductDeletedDocument eventDocument);
    public void publishVariantRestore(VariantRestoreDocument eventDocument);
    public void publishVariantPurge(VariantPurgeDocument eventDocument);
}
