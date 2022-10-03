package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.concurrent.ScheduledFuture;

@Beta
@GwtCompatible
public interface ListenableScheduledFuture<V> extends ScheduledFuture<V>, ListenableFuture<V> {}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\ListenableScheduledFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */