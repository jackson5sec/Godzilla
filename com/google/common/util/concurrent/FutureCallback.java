package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface FutureCallback<V> {
  void onSuccess(V paramV);
  
  void onFailure(Throwable paramThrowable);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\FutureCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */