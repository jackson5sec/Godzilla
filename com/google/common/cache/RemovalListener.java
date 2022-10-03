package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public interface RemovalListener<K, V> {
  void onRemoval(RemovalNotification<K, V> paramRemovalNotification);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\RemovalListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */