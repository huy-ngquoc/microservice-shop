package vn.edu.uit.msshop.product.category.adapter.out.cloudinary;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.category.application.port.out.DeleteCategoryImagePort;
import vn.edu.uit.msshop.product.category.application.port.out.UploadCategoryImagePort;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImage;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageKey;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageSize;
import vn.edu.uit.msshop.product.category.domain.model.CategoryImageUrl;
import vn.edu.uit.msshop.product.shared.adapter.out.cloudinary.CloudinaryImageStorageAdapterBase;

@Component
@Slf4j
public class CloudinaryImageStorageAdapter
        extends CloudinaryImageStorageAdapterBase
        implements UploadCategoryImagePort, DeleteCategoryImagePort {

    protected CloudinaryImageStorageAdapter(
            final Cloudinary cloudinary) {
        super(cloudinary);
    }

    @Override
    public CategoryImage upload(
            final CategoryId id,
            final byte[] bytes,
            final String originalFilename,
            final String contentType) {

        final var uploaded = this.uploadImage(bytes, "categories", id.value().toString());

        return new CategoryImage(
                new CategoryImageUrl(uploaded.url()),
                new CategoryImageKey(uploaded.publicId()),
                new CategoryImageSize(uploaded.width(), uploaded.height()));
    }

    @Override
    public void deleteByKey(
            final CategoryImageKey key) {
        this.deleteByKey(key.value());
    }
}
