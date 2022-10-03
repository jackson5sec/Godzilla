package org.springframework.cglib.proxy;

import java.lang.reflect.Method;

public interface MethodInterceptor extends Callback {
  Object intercept(Object paramObject, Method paramMethod, Object[] paramArrayOfObject, MethodProxy paramMethodProxy) throws Throwable;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\MethodInterceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */