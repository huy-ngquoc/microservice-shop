package vn.uit.edu.msshop.cart.adapter.out.persistence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.application.port.out.VariantToUserPort;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
public class VariantToUserPersistenceAdapter implements VariantToUserPort {
    private final VariantToUserRepository variantToUserRepo;

    @Override
    public void addMapping(
            VariantId variantId,
            UserId userId) {
        VariantToUserRedisModel variantToUserRedisModel = variantToUserRepo.findById(variantId.value().toString()).orElse(null);
        if(variantToUserRedisModel==null) {
            List<String> userIds = new ArrayList<>();
            userIds.add(userId.value().toString());
            variantToUserRepo.save(new VariantToUserRedisModel(variantId.value().toString(),userIds));
            return;
        }
        if(variantToUserRedisModel.getUserIds().contains(userId.value().toString())) return;
        variantToUserRedisModel.getUserIds().add(userId.value().toString());
        variantToUserRepo.save(variantToUserRedisModel);
    }

    @Override
    public void removeMapping(
            VariantId variantId,
            UserId userId) {
         VariantToUserRedisModel variantToUserRedisModel = variantToUserRepo.findById(variantId.value().toString()).orElse(null);
         if(variantToUserRedisModel==null) return;
         variantToUserRedisModel.getUserIds().remove(userId.value().toString());
         variantToUserRepo.save(variantToUserRedisModel);
    }

    @Override
    public Set<String> getByVariantId(
            VariantId id) {
        VariantToUserRedisModel variantToUserRedisModel = variantToUserRepo.findById(id.value().toString()).orElse(null);
        if(variantToUserRedisModel==null) return new HashSet<>();
        return new HashSet<>(variantToUserRedisModel.getUserIds());
    }

}
