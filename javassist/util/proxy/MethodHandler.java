package javassist.util.proxy;

import java.lang.reflect.Method;

public interface MethodHandler {
  Object invoke(Object paramObject, Method paramMethod1, Method paramMethod2, Object[] paramArrayOfObject) throws Throwable;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassis\\util\proxy\MethodHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */