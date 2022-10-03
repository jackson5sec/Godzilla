package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;

@Beta
public interface TypeToInstanceMap<B> extends Map<TypeToken<? extends B>, B> {
  <T extends B> T getInstance(Class<T> paramClass);
  
  <T extends B> T getInstance(TypeToken<T> paramTypeToken);
  
  @CanIgnoreReturnValue
  <T extends B> T putInstance(Class<T> paramClass, T paramT);
  
  @CanIgnoreReturnValue
  <T extends B> T putInstance(TypeToken<T> paramTypeToken, T paramT);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\reflect\TypeToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */