package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record VariantTargets(List<VariantTarget>values){public static final int MAX_AMOUNT=7;

public VariantTargets{if(values==null){throw new DomainException("Variant targets list CANNOT be null");}

if(values.size()>MAX_AMOUNT){throw new DomainException("Variant targets list can only have maximum "+MAX_AMOUNT+" targets");}

final var uniqueValues=HashSet.<String>newHashSet(values.size());for(final var trait:values){if(trait==null){throw new DomainException("Variant target CANNOT be null");}

final var lowercaseTrait=trait.value().toLowerCase(Locale.ROOT);if(!uniqueValues.add(lowercaseTrait)){throw new DomainException("Duplicate product variant target found: "+trait.value());}}

values=List.copyOf(values);}

public static VariantTargets of(final Collection<String>rawStrings){final var traitsList=rawStrings.stream().map(VariantTarget::new).toList();return new VariantTargets(traitsList);}

public List<String>unwrap(){return this.values.stream().map(VariantTarget::value).toList();}}
