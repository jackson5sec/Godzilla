package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public interface Weigher<K, V> {
  int weigh(K paramK, V paramV);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\Weigher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */