package vn.edu.uit.msshop.product.variant.adapter.in.web.mapper;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.shared.adapter.in.web.request.ChangeRequest;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.FindVariantsByIdsRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.request.UpdateVariantInfoRequest;
import vn.edu.uit.msshop.product.variant.adapter.in.web.response.VariantResponse;
import vn.edu.uit.msshop.product.variant.application.dto.command.DeleteImageCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.HardDeleteVariantCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.PostImageCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.RestoreVariantCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.SoftDeleteVariantCommand;
import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantInfoCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.ListVariantsQuery;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTarget;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTraits;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantVersion;
import vn.edu.uit.msshop.shared.adapter.out.cloudinary.CloudinaryImageUrlResolver;

@Component

@Slf4j
public class VariantWebMapper {
        
        private final CloudinaryImageUrlResolver resolver;
        
        private static final String VARIANTS_FOLDER = "variants";
        public VariantWebMapper(CloudinaryImageUrlResolver resolver) {
            this.resolver=resolver;
        }
    public ListVariantsQuery toListQuery(
            int page,

            int size,

            @Nullable
            String sortBy,

            PageRequestDto.Direction direction,

            @Nullable
            List<String> rawTargets) {
        final var pageRequest = new PageRequestDto(
                page,
                size,
                sortBy,
                direction);

        final List<VariantTarget> targets;
        if (rawTargets == null) {
            targets = List.of();
        } else {
            targets = rawTargets.stream()
                    .map(VariantTarget::new)
                    .toList();
        }

        return new ListVariantsQuery(pageRequest, targets);
    }

    public Set<VariantId> toVariantIds(
            final FindVariantsByIdsRequest request) {
        return request.ids().stream()
                .map(VariantId::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    public RestoreVariantCommand toRestoreCommand(
            final UUID id,
            final long expectedVersion) {
        final var variantId = new VariantId(id);
        final var version = new VariantVersion(expectedVersion);

        return new RestoreVariantCommand(variantId, version);
    }

    public UpdateVariantInfoCommand toUpdateInfoCommand(
            final UUID id,
            final UpdateVariantInfoRequest request) {
        final var variantId = new VariantId(id);
        final var version = new VariantVersion(request.version());

        final var price = ChangeRequest.toChange(request.price(), VariantPrice::new);
        final var traits = ChangeRequest.toChange(request.traits(), VariantTraits::of);
        final var targets = ChangeRequest.toChange(request.targets(), VariantTargets::of);

        return new UpdateVariantInfoCommand(
                variantId,
                price,
                traits,
                targets,
                version);
    }

    public SoftDeleteVariantCommand toSoftDeleteCommand(
            final UUID id,
            final long expectedVersion) {
        final var variantId = new VariantId(id);
        final var version = new VariantVersion(expectedVersion);

        return new SoftDeleteVariantCommand(
                variantId,
                version);
    }

    public HardDeleteVariantCommand toHardDeleteCommand(
            final UUID id,
            final long expectedVersion) {
        final var variantId = new VariantId(id);
        final var version = new VariantVersion(expectedVersion);

        return new HardDeleteVariantCommand(
                variantId,
                version);
    }

    public VariantId toVariantId(
            final UUID id) {
        return new VariantId(id);
    }

    public VariantResponse toResponse(
            final VariantView view) {
                String resolvedUrl = resolver.resolve(view.imageKey() != null ? view.imageKey() : "", VARIANTS_FOLDER);
        return new VariantResponse(
                view.id(),
                view.productId(),
                view.productName(),
                view.price(),
                view.soldCount(),
                view.stockCount(),
                view.traits(),
                view.targets(),
                view.imageKey(),
                resolvedUrl!=null?resolvedUrl:"",
                view.version());
    }

    public List<VariantResponse> toListResponse(
            final Collection<VariantView> views) {
        return views.stream()
                .map(this::toResponse)
                .toList();
    }
    public PostImageCommand toCommand(MultipartFile file, UUID variantId, long version) {
        return new PostImageCommand(new VariantId(variantId), file, version);
    }
    public DeleteImageCommand toCommand(UUID variantId, long version) {
        return new DeleteImageCommand(new VariantId(variantId), version);
    }
}
