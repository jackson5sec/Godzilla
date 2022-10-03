package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public interface ListenableFuture<V> extends Future<V> {
  void addListener(Runnable paramRunnable, Executor paramExecutor);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\ListenableFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */