package javassist.util.proxy;

public interface ProxyObject extends Proxy {
  void setHandler(MethodHandler paramMethodHandler);
  
  MethodHandler getHandler();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassis\\util\proxy\ProxyObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */