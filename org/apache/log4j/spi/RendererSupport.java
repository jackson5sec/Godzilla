package org.apache.log4j.spi;

import org.apache.log4j.or.ObjectRenderer;
import org.apache.log4j.or.RendererMap;

public interface RendererSupport {
  RendererMap getRendererMap();
  
  void setRenderer(Class paramClass, ObjectRenderer paramObjectRenderer);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\spi\RendererSupport.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */