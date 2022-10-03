package org.springframework.util.concurrent;

import org.springframework.lang.Nullable;

@FunctionalInterface
public interface SuccessCallback<T> {
  void onSuccess(@Nullable T paramT);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\concurrent\SuccessCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */