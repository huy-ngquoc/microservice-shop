package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import vn.edu.uit.msshop.product.product.domain.model.ProductOptions;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductVariantTraits(List<ProductVariantTrait>values){public static final int MAX_TRAITS_AMOUNT=ProductOptions.MAX_AMOUNT;

private static final ProductVariantTraits EMPTY=new ProductVariantTraits(List.of());

public ProductVariantTraits{if(values==null){throw new DomainException("Product variant trait list CANNOT be null");}

if(values.size()>MAX_TRAITS_AMOUNT){throw new DomainException("Product variant trait list can have maximum "+MAX_TRAITS_AMOUNT+" traits");}

final var uniqueValues=HashSet.<String>newHashSet(values.size());for(final var trait:values){if(trait==null){throw new DomainException("Variant trait CANNOT be null");}

final var lowercaseTrait=trait.value().toLowerCase(Locale.ROOT);if(!uniqueValues.add(lowercaseTrait)){throw new DomainException("Duplicate product variant trait found: "+trait.value());}}

values=List.copyOf(values);}

public static ProductVariantTraits of(final Collection<String>rawTraitsList){final var traitsList=rawTraitsList.stream().map(ProductVariantTrait::new).toList();return new ProductVariantTraits(traitsList);}

public static ProductVariantTraits empty(){return ProductVariantTraits.EMPTY;}

public List<String>unwrap(){return this.values.stream().map(ProductVariantTrait::value).toList();}

public boolean isEmpty(){return this.values.isEmpty();}

public int size(){return this.values.size();}

public ProductVariantTraits add(final ProductVariantTrait trait){final var newValues=new ArrayList<>(this.values);newValues.add(trait);return new ProductVariantTraits(newValues);}

public ProductVariantTraits removeAt(final int index){if((index<0)||(index>=this.values.size())){throw new DomainException("Trait index out of bounds: "+index);}

final var newValues=new ArrayList<>(this.values);newValues.remove(index);return new ProductVariantTraits(newValues);}}
