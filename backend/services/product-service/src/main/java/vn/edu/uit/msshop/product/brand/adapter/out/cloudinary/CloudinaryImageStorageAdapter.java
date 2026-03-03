package vn.edu.uit.msshop.product.brand.adapter.out.cloudinary;

import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.brand.application.port.out.DeleteBrandLogoPort;
import vn.edu.uit.msshop.product.brand.application.port.out.UploadBrandLogoPort;
import vn.edu.uit.msshop.product.brand.domain.model.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogo;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoKey;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoSize;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoUrl;
import vn.edu.uit.msshop.product.shared.adapter.out.cloudinary.CloudinaryImageStorageAdapterBase;

@Component
@Slf4j
public class CloudinaryImageStorageAdapter
        extends CloudinaryImageStorageAdapterBase
        implements UploadBrandLogoPort, DeleteBrandLogoPort {

    protected CloudinaryImageStorageAdapter(
            final Cloudinary cloudinary) {
        super(cloudinary);
    }

    @Override
    public BrandLogo upload(
            final BrandId id,
            final byte[] bytes,
            final String originalFilename,
            final String contentType) {

        final var uploaded = this.uploadImage(bytes, "brands", id.value().toString());

        return new BrandLogo(
                new BrandLogoUrl(uploaded.url()),
                new BrandLogoKey(uploaded.publicId()),
                new BrandLogoSize(uploaded.width(), uploaded.height()));
    }

    @Override
    public void deleteByKey(
            final BrandLogoKey key) {
        this.deleteByKey(key.value());
    }
}
