package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.function.Function;

@FunctionalInterface
@GwtCompatible
public interface Function<F, T> extends Function<F, T> {
  @CanIgnoreReturnValue
  T apply(F paramF);
  
  boolean equals(Object paramObject);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Function.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */