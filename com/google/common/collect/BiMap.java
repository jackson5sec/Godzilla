package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@GwtCompatible
public interface BiMap<K, V> extends Map<K, V> {
  @CanIgnoreReturnValue
  V put(K paramK, V paramV);
  
  @CanIgnoreReturnValue
  V forcePut(K paramK, V paramV);
  
  void putAll(Map<? extends K, ? extends V> paramMap);
  
  Set<V> values();
  
  BiMap<V, K> inverse();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\BiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */