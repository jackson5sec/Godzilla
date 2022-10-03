package org.springframework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.lang.Nullable;

public interface ParameterNameDiscoverer {
  @Nullable
  String[] getParameterNames(Method paramMethod);
  
  @Nullable
  String[] getParameterNames(Constructor<?> paramConstructor);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\ParameterNameDiscoverer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */