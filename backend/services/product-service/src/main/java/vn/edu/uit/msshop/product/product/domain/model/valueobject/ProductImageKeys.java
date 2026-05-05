package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductImageKeys(List<ProductImageKey>values){public static final int MAX_AMOUNT=10;

public ProductImageKeys{if(values==null){throw new DomainException("Product images list CANNOT be null");}

if(values.size()>MAX_AMOUNT){throw new DomainException("Product CANNOT exceed "+MAX_AMOUNT+" images");}

values=List.copyOf(values);}

public static ProductImageKeys of(final Collection<String>listRawKeys){final var imageKeysList=listRawKeys.stream().map(ProductImageKey::new).toList();return new ProductImageKeys(imageKeysList);}

public List<String>unwrap(){return this.values.stream().map(ProductImageKey::value).toList();}

public Optional<ProductImageKey>getCoverImageKey(){if(values.isEmpty()){return Optional.empty();}return Optional.of(values.getFirst());}}
