package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@Beta
@GwtIncompatible
public interface Interner<E> {
  @CanIgnoreReturnValue
  E intern(E paramE);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Interner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */