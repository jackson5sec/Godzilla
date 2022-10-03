package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@Beta
@GwtCompatible
public interface AsyncCallable<V> {
  ListenableFuture<V> call() throws Exception;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\AsyncCallable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */