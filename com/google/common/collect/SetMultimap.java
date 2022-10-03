package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@GwtCompatible
public interface SetMultimap<K, V> extends Multimap<K, V> {
  Set<V> get(K paramK);
  
  @CanIgnoreReturnValue
  Set<V> removeAll(Object paramObject);
  
  @CanIgnoreReturnValue
  Set<V> replaceValues(K paramK, Iterable<? extends V> paramIterable);
  
  Set<Map.Entry<K, V>> entries();
  
  Map<K, Collection<V>> asMap();
  
  boolean equals(Object paramObject);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\SetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */