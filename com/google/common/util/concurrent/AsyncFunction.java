package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public interface AsyncFunction<I, O> {
  ListenableFuture<O> apply(I paramI) throws Exception;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\AsyncFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */