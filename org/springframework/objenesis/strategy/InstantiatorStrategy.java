package org.springframework.objenesis.strategy;

import org.springframework.objenesis.instantiator.ObjectInstantiator;

public interface InstantiatorStrategy {
  <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> paramClass);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\strategy\InstantiatorStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */