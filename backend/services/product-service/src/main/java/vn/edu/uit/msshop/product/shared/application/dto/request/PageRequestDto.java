package vn.edu.uit.msshop.product.shared.application.dto.request;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public record PageRequestDto(int page,int size,@Nullable String sortBy,Direction direction){public static final int DEFAULT_PAGE=0;public static final int DEFAULT_SIZE=20;public static final int MAX_SIZE=100;public static final Direction DEFAULT_DIRECTION=Direction.ASC;

public static final String DEFAULT_PAGE_STRING="0";public static final String DEFAULT_SIZE_STRING="20";public static final String DEFAULT_DIRECTION_STRING="ASC";

public enum Direction {
  ASC, DESC

  }

  public PageRequestDto{if(page<0){throw new IllegalArgumentException("Page must be >= 0");}

  if(size<=0){throw new IllegalArgumentException("Size must be > 0");}

  if(size>MAX_SIZE){throw new IllegalArgumentException("Size must be <= "+MAX_SIZE);}

  if((sortBy==null)||sortBy.isBlank()){sortBy=null;}else{sortBy=sortBy.trim();}

  direction=Objects.requireNonNullElse(direction,Direction.ASC);}

  public PageRequestDto(
            final int page,
            final int size) {
        this(page, size, null, Direction.ASC);
    }

  public int offset() {
    return Math.multiplyExact(page, size);
  }
}
