package vn.edu.uit.msshop.product.shared.adapter.out.cloudinary;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CloudinaryImageUrlResolver {
    private final Cloudinary cloudinary;

    public @Nullable String resolve(
            @Nullable
            String key,
            String folder) {
        if (key == null) {
            return null;
        }
        return this.cloudinary.url().generate(folder + "/" + key);
    }
}
